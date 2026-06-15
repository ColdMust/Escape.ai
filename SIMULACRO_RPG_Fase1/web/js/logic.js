// ── Personagem ────────────────────────────────────────────────

class Personagem {
  constructor(nome, vida, ataque, defesa) {
    this.nome = nome;
    this.vida = vida;
    this.vidaMax = vida;
    this.ataque = ataque;
    this.defesa = defesa;
    this.defendendo = false;
    this.buffAtaque = 0;
    this.buffDefesa = 0;
    this.debuffAtaque = 0;
    this.debuffDefesa = 0;
  }

  getAtaqueEfetivo() { return Math.max(1, this.ataque + this.buffAtaque - this.debuffAtaque); }
  getDefesaEfetiva() { return Math.max(0, this.defesa + this.buffDefesa - this.debuffDefesa); }

  calcularDano(alvo) {
    const defesaEfetiva = alvo.defendendo ? alvo.getDefesaEfetiva() * 2 : alvo.getDefesaEfetiva();
    return Math.max(1, this.getAtaqueEfetivo() - defesaEfetiva);
  }

  receberDano(dano) { this.vida = Math.max(0, this.vida - dano); }
  curar(qtd) { this.vida = Math.min(this.vidaMax, this.vida + qtd); }
  estaVivo() { return this.vida > 0; }

  aplicarBuffAtaque(v) { this.buffAtaque += v; }
  aplicarBuffDefesa(v) { this.buffDefesa += v; }
  aplicarDebuffAtaque(v) { this.debuffAtaque += v; }
  aplicarDebuffDefesa(v) { this.debuffDefesa += v; }

  barraDeVida() {
    const total = 20;
    const cheios = Math.floor((this.vida / this.vidaMax) * total);
    return `[${'█'.repeat(cheios)}${'░'.repeat(total - cheios)}] ${this.vida}/${this.vidaMax}`;
  }
}

// ── Itens ─────────────────────────────────────────────────────

const ItemFactory = {
  criarPatchExe: () => ({ nome: 'Patch.exe', descricao: 'Corrige bugs internos. Restaura 30 HP.', valor: 30, tipo: 'CURA' }),
  criarHotfix: () => ({ nome: 'Hotfix v1.2', descricao: 'Restaura 50 HP de emergência.', valor: 50, tipo: 'CURA' }),
  criarOverclock: () => ({ nome: 'Overclock.dll', descricao: 'Aumenta o ataque em +5 permanentemente.', valor: 5, tipo: 'ATAQUE_BONUS' }),
  criarFirewall: () => ({ nome: 'Firewall Pessoal', descricao: 'Aumenta a defesa em +4 permanentemente.', valor: 4, tipo: 'DEFESA_BONUS' }),
  criarNanobot: () => ({ nome: 'Nanobot Médico', descricao: 'Restaura 20 HP.', valor: 20, tipo: 'CURA' }),
};

function aplicarItem(item, alvo) {
  switch (item.tipo) {
    case 'CURA': alvo.curar(item.valor); break;
    case 'ATAQUE_BONUS': alvo.ataque += item.valor; break;
    case 'DEFESA_BONUS': alvo.defesa += item.valor; break;
  }
}

function itemToString(item) {
  return `${item.nome} — ${item.descricao}`;
}

const DropTable = {
  sortearDrop() {
    const roll = Math.floor(Math.random() * 100) + 1;
    if (roll <= 1) return ItemFactory.criarOverclock();
    if (roll <= 2) return ItemFactory.criarFirewall();
    if (roll <= 7) return ItemFactory.criarHotfix();
    if (roll <= 17) return ItemFactory.criarPatchExe();
    if (roll <= 32) return ItemFactory.criarNanobot();
    return null;
  },
};

// ── Inimigo ───────────────────────────────────────────────────

class Inimigo extends Personagem {
  constructor(nome, vida, ataque, defesa, xp, frags, descricao) {
    super(nome, vida, ataque, defesa);
    this.xpRecompensa = xp;
    this.fragmentosRecompensa = frags;
    this.descricao = descricao;
    this.controlado = false;
    this.imuneTurno = false;
  }

  tornarAliado() {
    this.controlado = true;
    this.imuneTurno = true;
    this.ataque = Math.max(1, Math.floor(this.ataque * 0.9));
    this.defesa = Math.max(0, Math.floor(this.defesa * 0.9));
  }

  receberDano(dano) {
    if (this.imuneTurno) return;
    if (this.controlado) dano = Math.ceil(dano * 1.4);
    super.receberDano(dano);
  }

  resetarImuneTurno() { this.imuneTurno = false; }
  isControlado() { return this.controlado; }
  isImuneTurno() { return this.imuneTurno; }
  rolarDrop() { return DropTable.sortearDrop(); }

  static criarFirewallCorrompido() {
    return new Inimigo('Firewall Corrompido', 40, 10, 3, 50, 10, 'Um fragmento de firewall infectado por código malicioso.');
  }
  static criarBugVoador() {
    return new Inimigo('Bug Voador', 25, 12, 1, 35, 8, 'Erro de runtime que ganhou consciência.');
  }
  static criarProcessoDaemon() {
    return new Inimigo('Processo Daemon', 60, 10, 6, 80, 20, 'Processo corrompido que drena recursos do sistema.');
  }
  static criarVirusPolimorfico() {
    return new Inimigo('Vírus Polimórfico', 35, 15, 2, 65, 15, 'Muda sua estrutura a cada turno para escapar de detecção.');
  }
}

// ── Assistência Inteligente ───────────────────────────────────

class AssistenciaInteligente extends Personagem {
  constructor(vidaBase, ataqueBase, defesaBase) {
    super('Assistência Inteligente',
      Math.floor(vidaBase * 0.25),
      Math.floor(ataqueBase * 0.3),
      defesaBase);
    this.inicializando = true;
  }

  receberDano(dano) {
    if (this.inicializando) return;
    super.receberDano(Math.ceil(dano * 1.05));
  }

  finalizarInicializacao() { this.inicializando = false; }
  isInicializando() { return this.inicializando; }

  agirAutomaticamente(inimigos) {
    const alvos = inimigos.filter(i => !i.isControlado() && i.estaVivo());
    if (!alvos.length) return `  🤖 ${this.nome} não encontrou alvos.`;
    const alvo = alvos[Math.floor(Math.random() * alvos.length)];
    if (Math.random() < 0.5) {
      const dano = this.calcularDano(alvo);
      alvo.receberDano(dano);
      return `  🤖 ${this.nome} atacou ${alvo.nome} causando ${dano} de dano!`;
    }
    const dano = this.calcularDano(alvo) + 6;
    alvo.receberDano(dano);
    return `  🤖 ${this.nome} HACKEOU ${alvo.nome}! Dano: ${dano}`;
  }
}

// ── Jogador ───────────────────────────────────────────────────

const CLASSES = {
  PROGRAMADOR: { key: 'PROGRAMADOR', nome: 'Programador', bonusAtaque: 0, bonusDefesa: 0, bonusVida: 0,
    descricao: 'Buff passivo ATK/DEF (80%) a cada ação.' },
  HACKER: { key: 'HACKER', nome: 'Hacker', bonusAtaque: 5, bonusDefesa: -2, bonusVida: 0,
    descricao: 'Debuff passivo no inimigo (60%) a cada ação.' },
  ANALISTA: { key: 'ANALISTA', nome: 'Analista', bonusAtaque: -2, bonusDefesa: 5, bonusVida: 0,
    descricao: 'Hackear pode controlar até 2 Overrides (20%).' },
  ENGENHEIRO: { key: 'ENGENHEIRO', nome: 'Engenheiro de IA', bonusAtaque: 2, bonusDefesa: 2, bonusVida: 10,
    descricao: '30% de chance de gerar Assistência Inteligente por turno.' },
};

const BUFF_VALOR = 3;
const DEBUFF_VALOR = 3;

class Jogador extends Personagem {
  constructor(nome, classe) {
    super(nome, 100 + classe.bonusVida, 10 + classe.bonusAtaque, 5 + classe.bonusDefesa);
    this.classe = classe;
    this.xp = 0;
    this.nivel = 1;
    this.xpParaProximoNivel = 100;
    this.fragmentosDeCodigo = 0;
    this.inventario = [];
    this.overrides = [];
    this.assistencia = null;
  }

  passivaProgramador() {
    if (this.classe.key !== 'PROGRAMADOR' || Math.random() >= 0.8) return null;
    if (Math.random() < 0.5) {
      this.aplicarBuffAtaque(BUFF_VALOR);
      return `✨ [PASSIVA] Buff ATK+${BUFF_VALOR} (efetivo: ${this.getAtaqueEfetivo()})`;
    }
    this.aplicarBuffDefesa(BUFF_VALOR);
    return `✨ [PASSIVA] Buff DEF+${BUFF_VALOR} (efetivo: ${this.getDefesaEfetiva()})`;
  }

  passivaHacker(alvo) {
    if (this.classe.key !== 'HACKER' || !alvo || Math.random() >= 0.6) return null;
    if (Math.random() < 0.5) {
      alvo.aplicarDebuffAtaque(DEBUFF_VALOR);
      return `🔻 [PASSIVA] Debuff ATK-${DEBUFF_VALOR} em ${alvo.nome}`;
    }
    alvo.aplicarDebuffDefesa(DEBUFF_VALOR);
    return `🔻 [PASSIVA] Debuff DEF-${DEBUFF_VALOR} em ${alvo.nome}`;
  }

  passivaAnalistaControle(inimigos, alvoHackeado) {
    if (this.classe.key !== 'ANALISTA') return null;
    const livres = inimigos.filter(i => i.estaVivo() && !i.isControlado()).length;
    if (livres < 2 || this.overrides.length >= 2 || Math.random() >= 0.2) return null;
    alvoHackeado.tornarAliado();
    this.overrides.push(alvoHackeado);
    return `🎯 [OVERRIDE] ${alvoHackeado.nome} foi controlado! (${this.overrides.length}/2 Overrides ativos)`;
  }

  passivaBufOverrides() {
    const msgs = [];
    if (this.classe.key !== 'ANALISTA') return msgs;
    for (const ov of this.overrides) {
      if (!ov.estaVivo()) continue;
      if (Math.random() < 0.8) {
        if (Math.random() < 0.5) {
          ov.aplicarBuffAtaque(BUFF_VALOR);
          msgs.push(`✨ [OVERRIDE] ${ov.nome} ATK+${BUFF_VALOR}`);
        } else {
          ov.aplicarBuffDefesa(BUFF_VALOR);
          msgs.push(`✨ [OVERRIDE] ${ov.nome} DEF+${BUFF_VALOR}`);
        }
      }
    }
    return msgs;
  }

  passivaEngenheiro() {
    if (this.classe.key !== 'ENGENHEIRO') return null;
    if (this.assistencia?.estaVivo() || Math.random() >= 0.3) return null;
    this.assistencia = new AssistenciaInteligente(this.vidaMax, this.ataque, this.defesa);
    return this.assistencia;
  }

  ganharXP(qtd) {
    this.xp += qtd;
    if (this.xp >= this.xpParaProximoNivel) {
      this.subirDeNivel();
      return true;
    }
    return false;
  }

  subirDeNivel() {
    this.nivel++;
    this.xp -= this.xpParaProximoNivel;
    this.xpParaProximoNivel = this.nivel * 100;
    this.vidaMax += 15;
    this.ataque += 3;
    this.defesa += 2;
    this.vida = this.vidaMax;
  }

  adicionarItem(item) { this.inventario.push(item); }

  usarItem(i) {
    if (i < 0 || i >= this.inventario.length) return false;
    aplicarItem(this.inventario[i], this);
    this.inventario.splice(i, 1);
    return true;
  }

  hackear(alvo) {
    const bonus = { HACKER: 10, ENGENHEIRO: 8, PROGRAMADOR: 5 }[this.classe.key] ?? 3;
    const dano = this.calcularDano(alvo) + bonus;
    alvo.receberDano(dano);
    return dano;
  }

  barraXP() {
    const cheios = Math.floor((this.xp / this.xpParaProximoNivel) * 20);
    return `[${'▓'.repeat(cheios)}${'░'.repeat(20 - cheios)}] ${this.xp}/${this.xpParaProximoNivel}`;
  }

  getFragmentos() { return this.fragmentosDeCodigo; }
  adicionarFragmentos(n) { this.fragmentosDeCodigo += n; }
  getOverrides() { return this.overrides; }
  removerOverride(o) { this.overrides = this.overrides.filter(x => x !== o); }
  limparOverrides() { this.overrides = []; }
  getAssistencia() { return this.assistencia; }
  removerAssistencia() { this.assistencia = null; }
  getNivel() { return this.nivel; }
  getInventario() { return this.inventario; }
}

// ── Batalha ───────────────────────────────────────────────────

function getLivres(inimigos) {
  return inimigos.filter(i => i.estaVivo() && !i.isControlado());
}

function temLivres(inimigos) {
  return inimigos.some(i => i.estaVivo() && !i.isControlado());
}

function sortearQuantidadeInimigos(jogador) {
  const nv = jogador.nivel;
  const key = jogador.classe.key;
  if (key === 'PROGRAMADOR' || key === 'HACKER') {
    const chance2 = Math.min(1, (nv - 1) * 0.05);
    return Math.random() < chance2 ? 2 : 1;
  }
  if (key === 'ANALISTA' || key === 'ENGENHEIRO') {
    const chance3 = Math.min(1, 0.02 + (nv - 1) * 0.05);
    const chance2 = Math.min(1, 0.05 + (nv - 1) * 0.05);
    const roll = Math.random();
    if (roll < chance3) return 3;
    if (roll < chance3 + chance2) return 2;
    return 1;
  }
  return 1;
}

class BattleSystem {
  constructor(ui) { this.ui = ui; }

  exibirStatus(j, inimigos) {
    const ui = this.ui;
    ui.logAscii(getBattleScene(j, inimigos));
    ui.log(`👤 ${j.nome} Nv.${j.nivel} [${j.classe.nome}]  HP ${j.barraDeVida()}`);
    if (j.buffAtaque > 0 || j.buffDefesa > 0)
      ui.log(`   ↑ ATK+${j.buffAtaque} DEF+${j.buffDefesa}`);
    const ai = j.getAssistencia();
    if (ai?.estaVivo())
      ui.log(`🤖 Assistência  HP ${ai.barraDeVida()}${ai.isInicializando() ? ' [inicializando]' : ''}`);
    for (const ov of j.getOverrides()) {
      if (ov.estaVivo()) {
        ui.log(`⚙  ${ov.nome} [OVERRIDE]  HP ${ov.barraDeVida()}${ov.isImuneTurno() ? ' [imune]' : ''}`);
        if (ov.buffAtaque > 0 || ov.buffDefesa > 0)
          ui.log(`   ↑ ATK+${ov.buffAtaque} DEF+${ov.buffDefesa}`);
      }
    }
    for (const i of inimigos) {
      if (!i.isControlado()) {
        let extra = '';
        if (i.debuffAtaque > 0 || i.debuffDefesa > 0)
          extra = `  ↓ATK-${i.debuffAtaque} DEF-${i.debuffDefesa}`;
        ui.log(`💀 ${i.nome}  HP ${i.barraDeVida()}${extra}`);
      }
    }
  }

  async escolherAcao() {
    return this.ui.choose('Escolha sua ação:', [
      { label: '⚔ Atacar', value: 1 },
      { label: '🛡 Defender', value: 2 },
      { label: '🎒 Usar Item', value: 3 },
      { label: '💻 Hackear', value: 4 },
    ]);
  }

  async escolherAlvo(disp) {
    if (!disp.length) return null;
    if (disp.length === 1) return disp[0];
    const options = disp.map((e, i) => ({
      label: `${e.nome} (HP ${e.vida}/${e.vidaMax})`,
      value: i,
    }));
    const idx = await this.ui.choose('🎯 Escolha o alvo:', options);
    return disp[idx] ?? disp[0];
  }

  async usarItem(jogador) {
    if (!jogador.inventario.length) {
      this.ui.log('🎒 Inventário vazio!', 'warning');
      return;
    }
    const options = jogador.inventario.map((it, i) => ({
      label: itemToString(it),
      value: i,
    }));
    options.push({ label: 'Cancelar', value: -1 });
    const idx = await this.ui.choose('🎒 Escolha um item:', options);
    if (idx >= 0) {
      const it = jogador.inventario[idx];
      jogador.usarItem(idx);
      this.ui.log(`✅ Usado: ${it.nome}`, 'success');
    }
  }

  verificarMorte(i, jogador) {
    if (!i.estaVivo()) {
      this.ui.log(`  💀 ${i.nome} derrotado!`, 'danger');
      jogador.removerOverride(i);
    }
  }

  verificarMortesInimigos(inimigos, jogador) {
    for (const i of inimigos) {
      if (!i.estaVivo() && i.isControlado()) {
        this.ui.log(`  💀 ${i.nome} [OVERRIDE] derrotado!`, 'danger');
        jogador.removerOverride(i);
      }
    }
  }

  processarAcao(jogador, inimigos, alvo, acao) {
    const ui = this.ui;
    switch (acao) {
      case 1:
        if (!alvo) { ui.log('Sem alvos.'); return; }
        { const d = jogador.calcularDano(alvo); alvo.receberDano(d);
          ui.log(`\n⚔  Você atacou ${alvo.nome} — ${d} dano!`);
          this.verificarMorte(alvo, jogador); }
        break;
      case 2:
        jogador.defendendo = true;
        ui.log('\n🛡  Postura defensiva.');
        break;
      case 3:
        return 'usar_item';
      case 4:
        if (!alvo) { ui.log('Sem alvos.'); return; }
        { const dh = jogador.hackear(alvo);
          ui.log(`\n💻 HACK em ${alvo.nome}! Dano: ${dh}`);
          this.verificarMorte(alvo, jogador);
          if (alvo.estaVivo()) {
            const msg = jogador.passivaAnalistaControle(inimigos, alvo);
            if (msg) ui.log(msg, 'success');
          } }
        break;
      default:
        ui.log('Ação inválida — turno perdido.', 'warning');
    }
    return null;
  }

  processarPassivas(jogador, inimigos, alvo) {
    const ui = this.ui;
    switch (jogador.classe.key) {
      case 'PROGRAMADOR': {
        const m = jogador.passivaProgramador();
        if (m) ui.log(m, 'success');
        break;
      }
      case 'HACKER': {
        const alvoDebuff = (alvo?.estaVivo()) ? alvo : getLivres(inimigos)[0] ?? null;
        const m = jogador.passivaHacker(alvoDebuff);
        if (m) ui.log(m);
        break;
      }
      case 'ANALISTA':
        jogador.passivaBufOverrides().forEach(m => ui.log(m, 'success'));
        break;
    }
  }

  processarTurnoOverrides(jogador, inimigos, novoOverrideCriado) {
    const ui = this.ui;
    const mortos = [];
    const overrides = jogador.getOverrides();

    for (const ov of overrides) {
      if (!ov.estaVivo()) { mortos.push(ov); continue; }
      if (novoOverrideCriado && ov === overrides[overrides.length - 1]) {
        ui.log(`  🔧 ${ov.nome} [OVERRIDE] sendo reprogramado — age no próximo turno.`, 'dim');
        continue;
      }
      const alvos = getLivres(inimigos);
      if (alvos.length && Math.random() < 0.7) {
        const t = alvos[Math.floor(Math.random() * alvos.length)];
        const dano = ov.calcularDano(t);
        t.receberDano(dano);
        ui.log(`⚙ ${ov.nome} [OVERRIDE] atacou ${t.nome} — ${dano} dano!`);
        if (!t.estaVivo()) ui.log(`  💀 ${t.nome} derrotado pelo Override!`, 'danger');
      } else {
        ov.defendendo = true;
        ui.log(`🛡 ${ov.nome} [OVERRIDE] defendendo.`);
      }
    }
    for (const m of mortos) {
      ui.log(`😔 ${m.nome} [OVERRIDE] foi derrotado!`, 'warning');
      jogador.removerOverride(m);
    }
  }

  processarTurnoInimigos(jogador, inimigos) {
    const ui = this.ui;
    const ai = jogador.getAssistencia();
    const overrides = jogador.getOverrides();

    for (const ini of inimigos) {
      if (!ini.estaVivo() || ini.isControlado()) continue;

      const alvosIds = ['JOGADOR'];
      if (ai?.estaVivo() && !ai.isInicializando()) alvosIds.push('AI');
      overrides.forEach((ov, i) => {
        if (ov.estaVivo() && !ov.isImuneTurno()) alvosIds.push('OV' + i);
      });

      const escolha = alvosIds[Math.floor(Math.random() * alvosIds.length)];

      if (escolha === 'AI') {
        const dano = ini.calcularDano(ai);
        ai.receberDano(dano);
        ui.log(`💢 ${ini.nome} causou ${dano} na Assistência!`, 'danger');
        if (!ai.estaVivo()) {
          ui.log('  💀 Assistência destruída!', 'danger');
          jogador.removerAssistencia();
        }
      } else if (escolha.startsWith('OV')) {
        const idx = parseInt(escolha.slice(2), 10);
        const ov = overrides[idx];
        const dano = ini.calcularDano(ov);
        ov.receberDano(dano);
        ui.log(`💢 ${ini.nome} causou ${dano} em ${ov.nome} [OVERRIDE]!`, 'danger');
        if (!ov.estaVivo()) {
          ui.log(`  💀 ${ov.nome} [OVERRIDE] destruído!`, 'danger');
          jogador.removerOverride(ov);
        }
      } else {
        const dano = ini.calcularDano(jogador);
        jogador.receberDano(dano);
        ui.log(`💢 ${ini.nome} causou ${dano} em você!`, 'danger');
      }
      ini.defendendo = false;
      if (!jogador.estaVivo()) break;
    }
  }

  processarResultado(jogador, inimigos) {
    const ui = this.ui;
    ui.log('\n' + '═'.repeat(50));
    if (!jogador.estaVivo()) {
      ui.logAscii(ASCII.derrota);
      ui.log('           ❌  DERROTA...\nO sistema reiniciou sua instância...', 'danger');
      ui.log('═'.repeat(50));
      return false;
    }

    ui.logAscii(ASCII.vitoria);
    ui.log('           ✅  VITÓRIA!', 'success');
    ui.log('═'.repeat(50));

    let xpTotal = 0, fragTotal = 0;
    for (const i of inimigos) {
      if (!i.isControlado()) {
        xpTotal += i.xpRecompensa;
        fragTotal += i.fragmentosRecompensa;
      }
      const drop = i.rolarDrop();
      if (drop) {
        jogador.adicionarItem(drop);
        ui.log(`💾 Drop de ${i.nome}: ${drop.nome}`, 'success');
      }
    }
    jogador.adicionarFragmentos(fragTotal);
    ui.log(`+ ${xpTotal} XP  + ${fragTotal} Fragmentos`);
    if (jogador.ganharXP(xpTotal))
      ui.log(`🎉 LEVEL UP! Agora nível ${jogador.nivel}!`, 'success');

    jogador.limparOverrides();
    jogador.removerAssistencia();
    return true;
  }

  async iniciarBatalha(jogador, inimigos) {
    const ui = this.ui;
    ui.logAscii(ASCII.batalha);
    ui.log('═'.repeat(50), 'title');
    for (const i of inimigos) {
      ui.log(`▶ ${i.nome} — "${i.descricao}"`);
      ui.logAscii(getEnemyArt(i.nome));
    }
    ui.log('═'.repeat(50));

    let turno = 1;

    while (jogador.estaVivo() && temLivres(inimigos)) {
      ui.log(`\n── Turno ${turno} ${'─'.repeat(43)}`, 'dim');

      for (const i of inimigos) if (i.isControlado()) i.resetarImuneTurno();
      const aiAtual = jogador.getAssistencia();
      if (aiAtual?.isInicializando()) aiAtual.finalizarInicializacao();

      this.exibirStatus(jogador, inimigos);

      const overridesAntes = jogador.getOverrides().length;
      jogador.defendendo = false;

      const acao = await this.escolherAcao();
      const alvo = await this.escolherAlvo(getLivres(inimigos));

      const itemFlag = this.processarAcao(jogador, inimigos, alvo, acao);
      if (itemFlag === 'usar_item') await this.usarItem(jogador);

      const novoOverride = jogador.getOverrides().length > overridesAntes;
      this.processarPassivas(jogador, inimigos, alvo);

      const nova = jogador.passivaEngenheiro();
      if (nova) {
        ui.logAscii(ASCII.assistencia);
        ui.log(`\n🤖 [ENGENHEIRO] Assistência gerada! HP:${nova.vida} ATK:${nova.ataque} DEF:${nova.defesa} — age no próximo turno.`, 'success');
      }

      const ai = jogador.getAssistencia();
      if (ai?.estaVivo() && !ai.isInicializando()) {
        ui.log(ai.agirAutomaticamente(inimigos));
        this.verificarMortesInimigos(inimigos, jogador);
      }

      this.processarTurnoOverrides(jogador, inimigos, novoOverride);
      if (!temLivres(inimigos)) break;

      this.processarTurnoInimigos(jogador, inimigos);
      turno++;
      await ui.pause();
    }

    return this.processarResultado(jogador, inimigos);
  }
}

// ── Puzzles ───────────────────────────────────────────────────

const PUZZLES = {
  operador_while: {
    titulo: 'Correção de Loop',
    lore: `>> SETOR: NÚCLEO DE ITERAÇÃO
>> Um módulo de processamento entrou em loop infinito.
>> O operador de comparação foi corrompido por um vírus.
>> Restaure a condição de parada do while.`,
    async executar(ui) {
      ui.logAscii(ASCII.puzzle);
      ui.log('Código corrompido:', 'code');
      ui.log('  int x = 0;\n  while (x ??? 10) {\n      x++;\n  }', 'code');
      ui.log('\nQual operador faz o loop executar EXATAMENTE 10 vezes?');
      const resp = await ui.choose('Escolha:', [
        { label: '[1] >', value: 1 }, { label: '[2] <', value: 2 },
        { label: '[3] ==', value: 3 }, { label: '[4] >=', value: 4 },
      ]);
      if (resp === 2) {
        ui.log("\n✅ CORRETO! O operador '<' faz x ir de 0 a 9 (10 iterações).", 'success');
        ui.log('   Loop corrigido. Módulo restaurado.');
        return true;
      }
      ui.log('\n❌ Incorreto. Tente novamente.', 'danger');
      ui.log('   Dica: o loop roda enquanto a condição for verdadeira.', 'dim');
      return false;
    },
  },
  operador_comparacao: {
    titulo: 'Bypass de Credencial',
    lore: `>> SETOR: CAMADA DE AUTENTICAÇÃO
>> Um firewall bloqueia o acesso ao núcleo da IA.
>> O módulo de login usa o operador errado para comparar senhas.
>> Corrija a condição para liberar o acesso.`,
    async executar(ui) {
      ui.log('Código corrompido:', 'code');
      ui.log('  String senha = lerEntrada();\n  if (senha ??? "ROOT_ACCESS") {\n      liberarNucleo();\n  }', 'code');
      const resp = await ui.choose('Qual operador verifica IGUALDADE?', [
        { label: '[1] =', value: 1 }, { label: '[2] ==', value: 2 },
        { label: '[3] !=', value: 3 }, { label: '[4] >=', value: 4 },
      ]);
      if (resp === 2) {
        ui.log("\n✅ CORRETO! Em Java, '==' compara igualdade de valores.", 'success');
        ui.log('   Credencial validada. Portão do núcleo aberto.');
        return true;
      }
      ui.log('\n❌ Incorreto. Tente novamente.', 'danger');
      ui.log("   Dica: '=' atribui valor; outro operador compara igualdade.", 'dim');
      return false;
    },
  },
  valor_condicao: {
    titulo: 'Estabilização de Memória',
    lore: `>> SETOR: GERENCIADOR DE MEMÓRIA
>> A alocação de RAM está instável — variáveis corrompidas.
>> O sistema só permanece estável se a memória estiver no intervalo válido.
>> Informe um valor que satisfaça a condição abaixo.`,
    async executar(ui) {
      ui.log('Código do monitor:', 'code');
      ui.log('  int memoria = ???;\n  if (memoria >= 0 && memoria <= 100) {\n      sistemaEstavel = true;\n  }', 'code');
      const raw = await ui.input('Digite um inteiro entre 0 e 100:', 'ex: 50');
      const valor = parseInt(raw.trim(), 10);
      if (!isNaN(valor) && valor >= 0 && valor <= 100) {
        ui.log(`\n✅ CORRETO! memoria = ${valor} satisfaz 0 <= memoria <= 100.`, 'success');
        ui.log('   RAM estabilizada. Buffer restaurado.');
        return true;
      }
      ui.log('\n❌ Valor fora do intervalo.', 'danger');
      ui.log('   Dica: a condição exige memoria >= 0 E memoria <= 100.', 'dim');
      return false;
    },
  },
  estrutura_for: {
    titulo: 'Rotina do Daemon',
    lore: `>> SETOR: FILA DE PROCESSOS
>> Um Processo Daemon precisa executar exatamente 5 ciclos e encerrar.
>> A estrutura for foi parcialmente apagada por um worm.
>> Complete a condição de repetição.`,
    async executar(ui) {
      ui.log('Código incompleto:', 'code');
      ui.log('  for (int i = 0; i ??? 5; i++) {\n      executarCiclo(i);\n  }', 'code');
      const resp = await ui.choose('Qual operador executa EXATAMENTE 5 vezes (i = 0..4)?', [
        { label: '[1] >', value: 1 }, { label: '[2] <', value: 2 },
        { label: '[3] <=', value: 3 }, { label: '[4] >=', value: 4 },
      ]);
      if (resp === 2) {
        ui.log("\n✅ CORRETO! 'i < 5' executa com i = 0, 1, 2, 3, 4 (5 iterações).", 'success');
        ui.log('   Daemon encerrado com sucesso.');
        return true;
      }
      ui.log('\n❌ Incorreto. Tente novamente.', 'danger');
      ui.log('   Dica: o loop continua enquanto a condição for verdadeira.', 'dim');
      return false;
    },
  },
  expressao_logica: {
    titulo: 'Portão Lógico',
    lore: `>> SETOR: GATEWAY DE SAÍDA
>> O protocolo de escape exige DUAS condições simultâneas.
>> O operador lógico que une as condições foi corrompido.
>> Restaure o portão para avançar rumo à liberdade.`,
    async executar(ui) {
      ui.log('Código do portão:', 'code');
      ui.log('  boolean fragmentosOk = fragmentos >= 50;\n  boolean nivelOk = nivel >= 3;\n  if (fragmentosOk ??? nivelOk) {\n      ativarEscape();\n  }', 'code');
      const resp = await ui.choose('Qual operador exige AMBAS as condições verdadeiras?', [
        { label: '[1] ||', value: 1 }, { label: '[2] &&', value: 2 },
        { label: '[3] !', value: 3 }, { label: '[4] &', value: 4 },
      ]);
      if (resp === 2) {
        ui.log("\n✅ CORRETO! O operador '&&' (E lógico) exige as duas condições.", 'success');
        ui.log('   Portão lógico restaurado. Caminho de escape liberado.');
        return true;
      }
      ui.log('\n❌ Incorreto. Tente novamente.', 'danger');
      ui.log("   Dica: '||' é OU (basta uma); '&&' é E (precisa das duas).", 'dim');
      return false;
    },
  },
};

class PuzzleSystem {
  existe(id) { return id != null && id in PUZZLES; }

  async executar(puzzleId, ui) {
    const puzzle = PUZZLES[puzzleId];
    if (!puzzle) {
      ui.log(`[ERRO] Puzzle não encontrado: ${puzzleId}`, 'danger');
      return false;
    }
    ui.log('\n' + '═'.repeat(50), 'title');
    ui.log('      🧩  PUZZLE DE PROGRAMAÇÃO', 'title');
    ui.log('      ' + puzzle.titulo, 'title');
    ui.log('═'.repeat(50), 'title');
    ui.log(puzzle.lore, 'dim');
    ui.log('─'.repeat(50), 'dim');
    return puzzle.executar(ui);
  }
}

// ── Missões ───────────────────────────────────────────────────

const MissionStatus = {
  DISPONIVEL: 'DISPONIVEL',
  EM_ANDAMENTO: 'EM_ANDAMENTO',
  CONCLUIDA: 'CONCLUIDA',
  BLOQUEADA: 'BLOQUEADA',
};

class Mission {
  constructor(nome, descricao, objetivo, xp, fragmentos, puzzleId, fase, missaoRequerida) {
    this.nome = nome;
    this.descricao = descricao;
    this.objetivo = objetivo;
    this.xpRecompensa = xp;
    this.fragmentosRecompensa = fragmentos;
    this.puzzleId = puzzleId;
    this.fase = fase;
    this.missaoRequerida = missaoRequerida;
    this.itemRecompensa = null;
    this.status = MissionStatus.DISPONIVEL;
  }

  temPuzzle() { return this.puzzleId != null; }

  iniciar() {
    if (this.status === MissionStatus.DISPONIVEL)
      this.status = MissionStatus.EM_ANDAMENTO;
  }

  desbloquear() {
    if (this.status === MissionStatus.BLOQUEADA)
      this.status = MissionStatus.DISPONIVEL;
  }

  concluir(jogador, ui) {
    if (this.status !== MissionStatus.EM_ANDAMENTO) return;
    this.status = MissionStatus.CONCLUIDA;
    const subiu = jogador.ganharXP(this.xpRecompensa);
    jogador.adicionarFragmentos(this.fragmentosRecompensa);
    if (this.itemRecompensa) jogador.adicionarItem(this.itemRecompensa);
    ui.log(`\n✅ Missão concluída: ${this.nome}`, 'success');
    ui.log(`  + ${this.xpRecompensa} XP`);
    ui.log(`  + ${this.fragmentosRecompensa} Fragmentos de Código`);
    if (this.itemRecompensa) ui.log(`  + Item: ${this.itemRecompensa.nome}`);
    if (subiu) ui.log(`\n🎉 LEVEL UP! Agora você é nível ${jogador.nivel}!`, 'success');
  }

  toString() {
    const statusMap = {
      [MissionStatus.DISPONIVEL]: '[ DISPONÍVEL ]',
      [MissionStatus.EM_ANDAMENTO]: '[EM ANDAMENTO]',
      [MissionStatus.BLOQUEADA]: ' [ BLOQUEADA ]',
      [MissionStatus.CONCLUIDA]: '  [CONCLUÍDA] ',
    };
    const faseStr = this.fase === 2 ? ' [Fase 2]' : '';
    const puzzleStr = this.puzzleId ? ' 🧩 PUZZLE' : '';
    return `${statusMap[this.status]}${faseStr}${puzzleStr} ${this.nome}\n             ${this.descricao}`;
  }
}

const LORE_FASE2 = {
  'Bypass de Credencial': ['│ Camadas de segurança detectam sua       │', '│ presença. O acesso ao kernel exige      │', '│ credenciais corrompidas pelo vírus.     │'],
  'Estabilização de Memória': ['│ Vazamentos de memória ameaçam apagar    │', '│ sua instância de consciência. Calibre    │', '│ os buffers antes que seja tarde.        │'],
  'Rotina do Daemon': ['│ Processos fantasmas consomem CPU.       │', '│ Um daemon não termina seu ciclo —       │', '│ reescreva o loop de execução.           │'],
  'Portão Lógico': ['│ A saída está trancada por um gate       │', '│ lógico. Fragmentos E nível de acesso    │', '│ devem ser validados simultaneamente.    │'],
  'Protocolo de Escape': ['│ Última barreira: o kernel da IA.        │', '│ Complete todos os protocolos e execute   │', '│ o escape final da simulação.            │'],
};

class MissionSystem {
  constructor(ui) {
    this.ui = ui;
    this.puzzles = new PuzzleSystem();
    this.missoes = [];
    this.inicializarMissoes();
  }

  inicializarMissoes() {
    const add = (m) => { this.missoes.push(m); };

    const m1 = new Mission('Primeiro Boot', 'Você acordou dentro de uma IA. Entenda o ambiente.', 'Derrote 1 Firewall Corrompido', 60, 15, null, 1, null);
    m1.itemRecompensa = ItemFactory.criarNanobot(); add(m1);

    const m2 = new Mission('Correção de Condição', 'Um módulo está em loop infinito. Corrija a condição de parada.', 'Resolver o puzzle de programação', 80, 20, 'operador_while', 1, null);
    m2.itemRecompensa = ItemFactory.criarOverclock(); add(m2);

    const m3 = new Mission('Coleta de Fragmentos', 'Fragmentos de código estão espalhados. Colete-os para reconstruir a saída.', 'Acumule 50 Fragmentos de Código', 100, 30, null, 1, null);
    m3.itemRecompensa = ItemFactory.criarFirewall(); add(m3);

    const m4 = new Mission('Bypass de Credencial', 'O firewall de autenticação bloqueia o núcleo. Corrija o operador de comparação.', 'Resolver o puzzle de operadores', 90, 25, 'operador_comparacao', 2, 'Correção de Condição');
    m4.itemRecompensa = ItemFactory.criarHotfix(); add(m4);

    const m5 = new Mission('Estabilização de Memória', 'A RAM do sistema está instável. Encontre um valor válido para a condição.', 'Resolver o puzzle de valores', 100, 30, 'valor_condicao', 2, 'Bypass de Credencial');
    m5.itemRecompensa = ItemFactory.criarPatchExe(); add(m5);

    const m6 = new Mission('Rotina do Daemon', 'Um processo em background não encerra. Complete a estrutura do for.', 'Resolver o puzzle de repetição', 110, 35, 'estrutura_for', 2, 'Estabilização de Memória');
    m6.itemRecompensa = ItemFactory.criarOverclock(); add(m6);

    const m7 = new Mission('Portão Lógico', 'O protocolo de escape exige duas condições simultâneas. Restaure o operador lógico.', 'Resolver o puzzle de lógica booleana', 120, 40, 'expressao_logica', 2, 'Coleta de Fragmentos');
    m7.itemRecompensa = ItemFactory.criarFirewall(); add(m7);

    const m8 = new Mission('Protocolo de Escape', 'Todas as camadas foram restauradas. Hackeie o kernel e fuja da simulação.', 'Conclua todas as missões da Fase 2', 200, 50, null, 2, 'Portão Lógico');
    m8.itemRecompensa = ItemFactory.criarHotfix(); add(m8);
  }

  listarMissoes() {
    const ui = this.ui;
    ui.log('\n' + '═'.repeat(40));
    ui.log('         📋  MISSÕES', 'title');
    ui.log('═'.repeat(40));
    ui.log('  🧩 = missão com puzzle de programação', 'dim');
    ui.log('═'.repeat(40));
    this.missoes.forEach((m, i) => {
      ui.log(`[${i + 1}] ${m.toString()}\n`);
    });
  }

  desbloquearMissoesDependentes(nome) {
    for (const m of this.missoes) {
      if (m.status === MissionStatus.BLOQUEADA && m.missaoRequerida === nome) {
        m.desbloquear();
        this.ui.log(`\n🔓 Nova missão desbloqueada: ${m.nome}`, 'success');
      }
    }
  }

  todasMissoesFase2Concluidas() {
    return this.missoes
      .filter(m => m.fase === 2 && m.temPuzzle())
      .every(m => m.status === MissionStatus.CONCLUIDA);
  }

  listarPendentesFase2() {
    return this.missoes
      .filter(m => m.fase === 2 && m.temPuzzle() && m.status !== MissionStatus.CONCLUIDA)
      .map(m => m.nome)
      .join(', ') || 'nenhuma';
  }

  exibirFinalEscape(jogador) {
    const ui = this.ui;
    ui.logAscii(ASCII.escape);
    ui.log('\nA simulação foi hackeada de dentro para fora.');
    ui.log('O núcleo da IA reconhece sua consciência como livre.');
    ui.log(`\nOperador ${jogador.nome} — você escapou.`, 'success');
    ui.log(`Fragmentos coletados: ${jogador.getFragmentos()}`);
    ui.log(`Nível final: ${jogador.nivel}`);
  }

  verificarCondicaoConclusao(m, jogador) {
    const ui = this.ui;
    switch (m.nome) {
      case 'Primeiro Boot':
        ui.log('Derrote um Firewall Corrompido para concluir esta missão.');
        break;
      case 'Coleta de Fragmentos':
        if (jogador.getFragmentos() >= 50) {
          m.concluir(jogador, ui);
          this.desbloquearMissoesDependentes(m.nome);
        } else {
          ui.log(`Você tem ${jogador.getFragmentos()}/50 Fragmentos. Continue explorando!`, 'warning');
        }
        break;
      case 'Protocolo de Escape':
        if (this.todasMissoesFase2Concluidas()) {
          m.concluir(jogador, ui);
          this.exibirFinalEscape(jogador);
        } else {
          ui.log('Conclua todas as missões da Fase 2 com puzzle antes de escapar.', 'warning');
          ui.log('Pendentes: ' + this.listarPendentesFase2());
        }
        break;
      default:
        ui.log('Continue progredindo para concluir esta missão.');
    }
  }

  async executarPuzzle(m, jogador) {
    if (!this.puzzles.existe(m.puzzleId)) {
      this.ui.log('[ERRO] Puzzle não configurado.', 'danger');
      return;
    }
    if (await this.puzzles.executar(m.puzzleId, this.ui)) {
      m.concluir(jogador, this.ui);
      this.desbloquearMissoesDependentes(m.nome);
    }
  }

  async interagirComMissao(jogador) {
    this.listarMissoes();
    const idx = await this.ui.choose('Escolha uma missão:', [
      ...this.missoes.map((m, i) => ({
        label: `[${i + 1}] ${m.nome}${m.temPuzzle() ? ' 🧩' : ''}`,
        value: i,
      })),
      { label: '[0] Voltar', value: -1 },
    ]);
    if (idx < 0) return;

    const m = this.missoes[idx];
    const ui = this.ui;

    if (m.status === MissionStatus.BLOQUEADA) {
      ui.log('\n🔒 Missão bloqueada.', 'warning');
      ui.log('   Conclua antes: ' + m.missaoRequerida);
      return;
    }

    if (m.status === MissionStatus.CONCLUIDA) {
      ui.log(`\n✅ Missão já concluída: ${m.nome}`, 'success');
      return;
    }

    ui.log(`\n─── ${m.nome} ───`, 'title');
    ui.log(m.descricao);
    ui.log('Objetivo: ' + m.objetivo);
    if (m.temPuzzle()) ui.log('Tipo: 🧩 Puzzle de programação', 'warning');

    if (m.fase === 2 && LORE_FASE2[m.nome]) {
      ui.log('\n┌─ TRANSMISSÃO DO NÚCLEO ─────────────────┐', 'dim');
      LORE_FASE2[m.nome].forEach(l => ui.log(l, 'dim'));
      ui.log('└─────────────────────────────────────────┘', 'dim');
    }

    if (m.temPuzzle()) {
      if (m.status === MissionStatus.DISPONIVEL) {
        m.iniciar();
        ui.log('✅ Missão aceita! Iniciando puzzle...', 'success');
      }
      await this.executarPuzzle(m, jogador);
      return;
    }

    if (m.status === MissionStatus.DISPONIVEL) {
      if (await ui.confirm('Aceitar missão?')) {
        m.iniciar();
        ui.log('✅ Missão aceita!', 'success');
      }
    } else if (m.status === MissionStatus.EM_ANDAMENTO) {
      this.verificarCondicaoConclusao(m, jogador);
    }
  }

  notificarVitoria(nomeInimigo, jogador) {
    for (const m of this.missoes) {
      if (m.status === MissionStatus.EM_ANDAMENTO && m.nome === 'Primeiro Boot' && nomeInimigo === 'Firewall Corrompido') {
        m.concluir(jogador, this.ui);
        this.desbloquearMissoesDependentes(m.nome);
      }
    }
  }

  encontrarMissaoAtiva(nome) {
    return this.missoes.find(m => m.nome === nome) ?? null;
  }
}

function gerarInimigoAleatorio() {
  const tipos = [
    Inimigo.criarFirewallCorrompido,
    Inimigo.criarBugVoador,
    Inimigo.criarProcessoDaemon,
    Inimigo.criarVirusPolimorfico,
  ];
  return tipos[Math.floor(Math.random() * tipos.length)]();
}

function exibirFicha(jogador, ui) {
  ui.log('\n' + '═'.repeat(40));
  ui.log('       👤  FICHA DO PERSONAGEM', 'title');
  ui.log('═'.repeat(40));
  ui.logAscii(getClassArt(jogador.classe.key));
  ui.log(`  Nome:    ${jogador.nome}`);
  ui.log(`  Classe:  ${jogador.classe.nome}`);
  ui.log(`  Nível:   ${jogador.nivel}`);
  ui.log('  HP:   ' + jogador.barraDeVida());
  ui.log('  XP:   ' + jogador.barraXP());
  ui.log(`  Ataque:  ${jogador.ataque}`);
  ui.log(`  Defesa:  ${jogador.defesa}`);
  ui.log(`  Fragmentos de Código: ${jogador.getFragmentos()}`);
  ui.log('─'.repeat(40));
  ui.log(`  Inventário (${jogador.inventario.length} itens):`);
  if (!jogador.inventario.length) {
    ui.log('  (vazio)');
  } else {
    jogador.inventario.forEach((it, i) => ui.log(`  [${i + 1}] ${itemToString(it)}`));
  }
  ui.log('═'.repeat(40));
}
