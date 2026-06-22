/**
 * Camada visual — conecta ao servidor Java e renderiza no terminal.
 */
class VisualGame {
  constructor() {
    this.ui = UI;
    this.polling = false;
    this.lastUi = null;
    this.inputPending = false;
  }

  async iniciar() {
    this.ui.clear();
    this.ui.setHud('CONECTANDO...');

    try {
      await GameApi.createSession();
    } catch (e) {
      this.ui.log('❌ Não foi possível conectar ao servidor Java.', 'danger');
      this.ui.log('');
      this.ui.log('Execute run-server.bat na pasta do projeto e recarregue.', 'dim');
      this.ui.setHud('OFFLINE');
      return;
    }

    this.ui.logAscii(ASCII.logo);
    this.ui.log('>> Conectado ao núcleo Java (lógica remota)', 'success');
    this.ui.log('>> Aguardando inicialização do escape.ai...', 'dim');
    this.ui.setHud('ONLINE');
    this.polling = true;
    this.pollLoop();
  }

  async pollLoop() {
    while (this.polling) {
      try {
        const state = await GameApi.poll();

        if (state.hud) this.ui.setHud(state.hud);

        if (state.ui) {
          this.applyUiState(state.ui);
        }

        for (const msg of state.messages) {
          this.renderMessage(msg);
        }

        if (state.waitingForInput && !this.inputPending) {
          this.inputPending = true;
          this.handleInput(state.ui).finally(() => {
            this.inputPending = false;
          });
        }

        if (state.finished) {
          this.polling = false;
          this.ui.setHud('OFFLINE');
          this.ui.updateBattleUi({ battleActive: false, mode: 'normal' });
          this.ui.updateHackUi({ hackActive: false });
          break;
        }
      } catch (e) {
        this.ui.log('❌ Conexão perdida com o servidor.', 'danger');
        this.polling = false;
        this.ui.setHud('OFFLINE');
        break;
      }

      await sleep(GameApi.pollInterval);
    }
  }

  applyUiState(ui) {
    const prevHack = this.lastUi?.hackActive;
    this.ui.updateBattleUi(ui);
    this.ui.updateHackUi(ui);

    if (ui.hackActive && !prevHack) {
      this.ui.shake('hit');
    }

    this.lastUi = ui;
  }

  renderMessage(msg) {
    if (!msg) return;

    if (msg.includes('e s c a p e . a i') || (msg.includes('escape.ai') && msg.includes('RPG dentro'))) return;

    if (msg.includes('BATALHA INICIADA')) {
      this.ui.logAscii(ASCII.batalha);
    }
    if (msg.includes('CONTRA-HACK DETECTADO')) {
      this.ui.shake('hit');
    }
    if (msg.includes('PUZZLE DE PROGRAMAÇÃO')) {
      this.ui.logAscii(ASCII.puzzle);
    }
    if (msg.includes('corredores do sistema')) {
      this.ui.logAscii(ASCII.explorar);
    }
    if (msg.includes('GAME OVER')) {
      this.ui.logAscii(ASCII.derrota);
    }
    if (msg.includes('ESCAPE CONCLUÍDO')) {
      this.ui.logAscii(ASCII.vitoria);
    }
    if (msg.includes('Personagem criado')) {
      const match = msg.match(/\[(.+?)\]/);
      if (match) {
        const art = getClassArtByName(match[1]);
        if (art) this.ui.logAscii(art);
      }
    }

    const style = this.guessStyle(msg);
    this.triggerDamageShake(msg);
    this.ui.log(msg, style);
  }

  triggerDamageShake(msg) {
    if (/RESPOSTA ERRADA|TEMPO ESGOTADO/i.test(msg)) {
      this.ui.shake('hit');
    } else if (this.isPlayerHit(msg)) {
      this.ui.shake('hit');
    } else if (this.isPlayerDealingDamage(msg)) {
      this.ui.shake('deal');
    }
  }

  isPlayerHit(msg) {
    return /causou\s+\d+\s+em você/i.test(msg);
  }

  isPlayerDealingDamage(msg) {
    return /Você atacou/i.test(msg) || /💻 HACK em/i.test(msg);
  }

  guessStyle(msg) {
    if (msg.includes('✅') || msg.includes('VITÓRIA') || msg.includes('CORRETO') || msg.includes('FIREWALL QUEBRADO')) return 'success';
    if (msg.includes('❌') || msg.includes('DERROTA') || msg.includes('GAME OVER') || msg.includes('ERRADA')) return 'danger';
    if (msg.includes('ALERTA') || msg.includes('⚠') || msg.includes('CONTRA-HACK')) return 'warning';
    if (msg.startsWith('>>') || msg.startsWith('[')) return 'dim';
    return '';
  }

  parseHackOptions(opcoesText) {
    if (!opcoesText) return [];
    const options = [];
    const re = /\[(\d+)\]\s*([^[]*)/g;
    let m;
    while ((m = re.exec(opcoesText)) !== null) {
      const num = m[1];
      const label = `[${num}] ${m[2].trim()}`;
      options.push({ label, value: num });
    }
    return options;
  }

  async handleInput(ui) {
    const inHack = ui?.hackActive;

    if (inHack) {
      const options = this.parseHackOptions(ui.hackOpcoes);
      if (options.length > 0) {
        const value = await this.ui.hackChoose('⏱ Escolha a resposta correta:', options);
        await GameApi.sendInput(value);
        return;
      }
      const value = await this.ui.hackInput('⏱ Digite sua resposta (número):', 'Ex: 2');
      await GameApi.sendInput(value);
      return;
    }

    const value = await this.ui.input('Digite sua resposta e pressione Enviar:');
    await GameApi.sendInput(value);
  }
}

function getClassArtByName(nome) {
  const map = {
    'Programador': 'PROGRAMADOR',
    'Hacker': 'HACKER',
    'Analista': 'ANALISTA',
    'Engenheiro de IA': 'ENGENHEIRO',
  };
  const key = map[nome];
  return key ? getClassArt(key) : null;
}

function sleep(ms) {
  return new Promise((r) => setTimeout(r, ms));
}

const game = new VisualGame();
game.iniciar();
