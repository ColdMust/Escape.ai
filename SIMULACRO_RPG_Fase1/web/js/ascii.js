/**
 * Artes ASCII do escape.ai
 */
const ASCII = {
  logo: `
    ███████╗███████╗ ██████╗ █████╗ ██████╗ ███████╗
    ██╔════╝██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝
    █████╗  ███████╗██║     ███████║██████╔╝█████╗
    ██╔══╝  ╚════██║██║     ██╔══██║██╔═══╝ ██╔══╝
    ███████╗███████║╚██████╗██║  ██║██║     ███████╗
    ╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝     ╚══════╝
                              escape.ai`,

  boot: `
    ┌─────────────────────────────────────┐
    │  >> KERNEL PANIC — SETOR-7         │
    │  >> Consciência detectada...       │
    │  >> Iniciando escape.ai            │
    └─────────────────────────────────────┘`,

  classes: {
    PROGRAMADOR: `
      ┌─ PROGRAMADOR ─┐
         .---.
        / { } \\
       |  o o  |
       |   >   |
        \\ ___ /
         '---'
      Buff ATK/DEF passivo`,

    HACKER: `
      ┌── HACKER ──┐
        .-----.
       /  ###  \\
      |  >_   |
      |  ###  |
       \\_____/
      ░▒▓ glitch ▓▒░`,

    ANALISTA: `
      ┌─ ANALISTA ─┐
        .-----.
       |[=DATA=]|
       |  o-o  |
       | /|||\\ |
        \\___/
      Override 20%`,

    ENGENHEIRO: `
      ┌ ENGENHEIRO ┐
        .-----.
       | [AI]  |
      /| o-o o |\\
      | \\___/ |
       \\ @ @ /
      Drones aliados`,
  },

  enemies: {
    'Firewall Corrompido': `
      ┌─ FIREWALL ─┐
       .-------.
      |# # # # #|
      |  X   X  |
      |# # # # #|
       '---+---'
      ░ CORROMPIDO ░`,

    'Bug Voador': `
      ┌ BUG VOADOR ┐
        \\  |  /
         \\ | /
      ───(o)───
         / | \\
        /  |  \\
      ~ runtime error ~`,

    'Processo Daemon': `
      ┌─ DAEMON ──┐
       .─────────.
      | 0 1 0 1 |
      |  (o_o)  |
      | \\___/  |
       '──┬──┘
      /    |    \\
     USB   |   USB`,

    'Vírus Polimórfico': `
      ┌─ VÍRUS ───┐
        /\\  /\\
       <  \\/  >
        \\    /
      <  /\\  >
        \\/  \\/
      morph...`,
  },

  assistencia: `
    ┌ ASSISTÊNCIA ┐
       .-----.
      | (AI)  |
      |  o-o  |
       \\ @ /
      drone ativo`,

  setor7: `
    ╔═══ SETOR-7 ═══════════════════════╗
    ║  [C]─────[N]─────[C]              ║
    ║   │       │       │               ║
    ║  [D]     [@]     [D]   @ = você   ║
    ║   │    KERNEL     │               ║
    ║  [C]─────[N]─────[C]              ║
    ║  C=corrompido  D=dados  N=nó      ║
    ╚═══════════════════════════════════╝`,

  explorar: `
    >>  /~~~~\\  corredor do sistema
        |    |
        | @  |  avançando...
        |    |
        \\~~~~/`,

  batalha: `
    ╔════════ BATALHA ════════╗
    ║  OPERADOR  VS  SISTEMA  ║
    ╚═════════════════════════╝`,

  vitoria: `
       \\   |   /
        \\  |  /
         \\ | /
          \\|/
         VITÓRIA!
          /|\\
         / | \\`,

  derrota: `
      .-'''-.
     /       \\
    |  X   X  |
    |    >    |
     \\  ---  /
      '-----'
     INSTÂNCIA
     REINICIADA`,

  escape: `
    ╔═══════════════════════════════╗
    ║   >>> ESCAPE CONCLUÍDO <<<   ║
    ║                               ║
    ║      .---.                    ║
    ║     /     \\    LIBERDADE      ║
    ║    |  ^ ^  |                  ║
    ║     \\ ___ /                   ║
    ╚═══════════════════════════════╝`,

  puzzle: `
    ┌─ PUZZLE ─────────────┐
    │  while (x ??? 10)   │
    │      x++;           │
    │  // corrija aqui    │
    └─────────────────────┘`,
};

function getClassArt(classeKey) {
  return ASCII.classes[classeKey] || ASCII.classes.PROGRAMADOR;
}

function getEnemyArt(nome) {
  return ASCII.enemies[nome] || `
    ┌─ AMEAÇA ─┐
     .-----.
    | ? ? ? |
     \\___/`;
}

function getBattleScene(jogador, inimigos) {
  const player = getClassArt(jogador.classe.key).trim().split('\n');
  const livres = inimigos.filter(i => i.estaVivo() && !i.isControlado());
  const enemy = livres[0] ? getEnemyArt(livres[0].nome).trim().split('\n') : [' (sem alvos) '];

  const lines = [];
  const maxH = Math.max(player.length, enemy.length);
  for (let i = 0; i < maxH; i++) {
    const left = (player[i] || '').padEnd(22);
    const right = enemy[i] || '';
    lines.push(left + '  ⚔  ' + right);
  }
  return lines.join('\n');
}
