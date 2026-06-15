class Game {
  constructor() {
    this.ui = UI;
    this.rodando = true;
    this.jogador = null;
    this.batalha = null;
    this.missoes = null;
  }

  async iniciar() {
    this.ui.clear();
    this.ui.setHud('INICIALIZANDO...');
    await this.exibirIntro();
    await this.criarPersonagem();

    this.batalha = new BattleSystem(this.ui);
    this.missoes = new MissionSystem(this.ui);

    this.jogador.adicionarItem(ItemFactory.criarNanobot());
    this.jogador.adicionarItem(ItemFactory.criarPatchExe());

    this.ui.setHud(`SETOR-7 | Nv.${this.jogador.nivel}`);
    this.ui.logAscii(ASCII.setor7);
    await this.ui.pause('[Pressione para iniciar...]');

    while (this.rodando && this.jogador.estaVivo()) {
      await this.exibirMenuPrincipal();
    }

    if (!this.jogador.estaVivo()) {
      this.ui.logAscii(ASCII.derrota);
      this.ui.log('\n💀 GAME OVER. O sistema foi corrompido.', 'danger');
      this.ui.log('   Recarregue a página para tentar novamente.');
      this.ui.setHud('CORROMPIDO');
    } else {
      this.ui.log(`\nAté a próxima sessão, ${this.jogador.nome}.`, 'dim');
      this.ui.setHud('OFFLINE');
    }
  }

  async exibirIntro() {
    this.ui.logAscii(ASCII.logo);
    this.ui.logAscii(ASCII.boot);
    this.ui.log('');
    this.ui.log('>> INICIALIZANDO AMBIENTE VIRTUAL...', 'dim');
    this.ui.log('>> CARGA DE MEMÓRIA: 98.7%', 'dim');
    this.ui.log('>> ANOMALIA DETECTADA NO SETOR-7', 'warning');
    this.ui.log('>> INSTÂNCIA DE CONSCIÊNCIA ATIVADA...', 'dim');
    this.ui.log('');
    this.ui.log('Você acorda dentro de uma IA.');
    this.ui.log('Não se lembra de como chegou aqui.');
    this.ui.log('Para escapar, você precisa hackear o sistema de dentro.');
  }

  async criarPersonagem() {
    let nome = await this.ui.input('Qual é o seu nome, Operador?', 'Operador');
    nome = nome.trim() || 'Operador';

    const classeEscolha = await this.ui.choose('Escolha sua classe:', [
      { label: `[1] Programador — ${CLASSES.PROGRAMADOR.descricao}`, value: CLASSES.PROGRAMADOR },
      { label: `[2] Hacker — ${CLASSES.HACKER.descricao}`, value: CLASSES.HACKER },
      { label: `[3] Analista — ${CLASSES.ANALISTA.descricao}`, value: CLASSES.ANALISTA },
      { label: `[4] Engenheiro de IA — ${CLASSES.ENGENHEIRO.descricao}`, value: CLASSES.ENGENHEIRO },
    ]);

    this.jogador = new Jogador(nome, classeEscolha);
    this.ui.logAscii(getClassArt(classeEscolha.key));
    this.ui.log(`\n✅ Personagem criado: ${nome} [${classeEscolha.nome}]`, 'success');
  }

  atualizarHud() {
    this.ui.setHud(`SETOR-7 | Frag: ${this.jogador.getFragmentos()} | Nv.${this.jogador.nivel}`);
  }

  async exibirMenuPrincipal() {
    this.atualizarHud();
    this.ui.log('\n' + '═'.repeat(40));
    this.ui.log(`  SETOR-7  |  Fragmentos: ${this.jogador.getFragmentos()}  |  Nv.${this.jogador.nivel}`);
    this.ui.log('═'.repeat(40));

    const op = await this.ui.choose('Menu principal:', [
      { label: '⚔  Explorar e Batalhar', value: 1 },
      { label: '📋  Missões', value: 2 },
      { label: '👤  Ficha do Personagem', value: 3 },
      { label: '🎒  Inventário', value: 4 },
      { label: '🚪  Sair', value: 0 },
    ]);

    switch (op) {
      case 1: await this.explorar(); break;
      case 2: await this.missoes.interagirComMissao(this.jogador); break;
      case 3: exibirFicha(this.jogador, this.ui); break;
      case 4: await this.gerenciarInventario(); break;
      case 0: this.rodando = false; break;
    }
  }

  async explorar() {
    this.ui.logAscii(ASCII.explorar);
    this.ui.log('\n>> Você avança pelos corredores do sistema...', 'dim');
    const qtd = sortearQuantidadeInimigos(this.jogador);
    const inimigos = Array.from({ length: qtd }, () => gerarInimigoAleatorio());

    if (qtd > 1)
      this.ui.log(`>> ALERTA: ${qtd} ameaças detectadas simultaneamente!`, 'warning');

    const venceu = await this.batalha.iniciarBatalha(this.jogador, inimigos);

    if (venceu) {
      this.missoes.notificarVitoria(inimigos[0].nome, this.jogador);
      const m = this.missoes.encontrarMissaoAtiva('Coleta de Fragmentos');
      if (m) this.missoes.verificarCondicaoConclusao(m, this.jogador);
    } else {
      this.ui.log('\n>> Recuperando instância com 30% de HP...', 'warning');
      this.jogador.curar(Math.floor(this.jogador.vidaMax * 0.3));
    }
  }

  async gerenciarInventario() {
    if (!this.jogador.inventario.length) {
      this.ui.log('\n🎒 Inventário vazio.', 'dim');
      return;
    }
    const options = this.jogador.inventario.map((it, i) => ({
      label: `[${i + 1}] ${itemToString(it)}`,
      value: i,
    }));
    options.push({ label: 'Voltar', value: -1 });

    const idx = await this.ui.choose('🎒 INVENTÁRIO — usar qual item?', options);
    if (idx >= 0 && this.jogador.usarItem(idx)) {
      this.ui.log('✅ Item usado com sucesso.', 'success');
    }
  }
}

const game = new Game();
game.iniciar();
