---
marp: true
theme: default
paginate: true
style: |
  section {
    background: #0a0a0a;
    color: #e0e0e0;
    font-family: 'Courier New', monospace;
  }
  h1 { color: #00ff88; border-left: 8px solid #00ff88; padding-left: 20px; }
  h2 { color: #ffffff; border-bottom: 2px solid #2ecc71; }
  code { background: #1c2a1c; color: #9eff9e; }
---

<!-- SLIDE 1 -->

# escape.ai

```
╔══════════════════════════════════════════════════════════════╗
║                                                              ║
║   ███████╗███████╗ ██████╗ █████╗ ██████╗ ███████╗           ║
║   ██╔════╝██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝           ║
║   █████╗  ███████╗██║     ███████║██████╔╝█████╗             ║
║   ██╔══╝  ╚════██║██║     ██╔══██║██╔═══╝ ██╔══╝             ║
║   ███████╗███████║╚██████╗██║  ██║██║     ███████╗           ║
║   ╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝     ╚══════╝           ║
║                        escape.ai                             ║
║                                                              ║
║             "Você acorda preso dentro de uma IA."            ║
║        Escapar exige código, batalhas e astúcia.             ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
```

**Fase 1 + 2 · RPG Cyberpunk Educacional**

Batalhas · Inventário · Missões · Puzzles · Interface Web

*Java (lógica) + HTML/CSS/JS (visual) · orientação a objetos*

---

<!-- SLIDE 2 -->

## 📟 O CONCEITO

```
╔══════════════════════════════════════════════════════════════╗
║   [ SISTEMA : CORE_AI ]       [ STATUS : CORROMPIDO ]        ║
║   ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ║
║                                                              ║
║   ► Você desperta dentro de uma simulação digital hostil.    ║
║   ► A única saída: vencer batalhas, resolver puzzles         ║
║     de programação e completar missões.                      ║
║   ► Inimigos corrompem variáveis. Missões exigem corrigir    ║
║     condições, operadores e estruturas de código.            ║
║   ► Itens digitais (Patch.exe, Hotfix…) alteram o combate.   ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝
```

| Mecânica | Descrição |
|----------|-----------|
| ⚔️ **Batalhas táticas** | Ataque, defesa, hackear, item — cada escolha importa |
| 🧩 **Puzzles de código** | if, while, for, operadores — integrados à lore |
| 💫 **Fragmentos digitais** | Recompensa de missões; fortalecem o operador |
| 🌐 **Terminal visual** | Interface cyberpunk no navegador, lógica 100% Java |

*A programação É a narrativa — aprenda jogando*

---

<!-- SLIDE 3 -->

## 🎯 O QUE FOI ENTREGUE

```
┌────────────────────────────────────────────────────────────────┐
│  ✔  Criação de personagem (nome + 4 classes com passivas)       │
│  ✔  Sistema de batalha por turnos (1 a 3 inimigos)              │
│  ✔  Inventário funcional + drops aleatórios                     │
│  ✔  XP, níveis e fragmentos de código                           │
│  ✔  Missões lineares com puzzles de programação                 │
│  ✔  Contra-hack em tempo real (30s, puzzle sob pressão)         │
│  ✔  Interface web conectada ao Java via API REST                  │
│  ✔  Modo console puro (run.bat) ainda disponível                │
└────────────────────────────────────────────────────────────────┘
```

> ✨ **Diferencial:** arquitetura separada — **Java decide tudo**, o navegador só renderiza. Mesma lógica roda no terminal ou na web.

*GameIO · RemoteGameIO · GameServer · UiState*

---

<!-- SLIDE 4 -->

## 🏗️ ARQUITETURA · LÓGICA × VISUAL

```
┌──────────────┐     HTTP /api/session      ┌──────────────────┐
│   Navegador  │ ◄────────────────────────► │   GameServer     │
│  index.html  │   JSON (mensagens, UI)     │   (porta 9090)   │
│  main.js     │                            │                  │
│  ui.js       │                            │   GameSession    │
│  api.js      │                            │   RemoteGameIO   │
└──────────────┘                            │   Game.java      │
                                            └──────────────────┘
```

**Camada Java (`src/`)**
- Combate, missões, puzzles, inventário, progressão

**Camada Web (`web/`)**
- Terminal ASCII, painel de vida, overlay de contra-hack
- Poll a cada 200ms · shake na tela ao tomar dano

**Abstração `GameIO`**
- `ConsoleIO` → terminal puro
- `RemoteGameIO` → sincroniza estado com o frontend

*Um código, duas interfaces*

---

<!-- SLIDE 5 -->

## 🛠️ TECNOLOGIAS & CONCEITOS

| ☕ Backend | 🌐 Frontend | ⚙️ Ferramentas |
|-----------|-------------|----------------|
| Java 21 (JDK) | HTML5 / CSS3 | run-server.bat |
| POO completa | JavaScript ES6+ | Git |
| HttpServer (JDK) | Fetch API | IntelliJ / VS Code |
| ArrayList, Streams | Animações CSS | setup-java.bat |

```java
// Pilar da arquitetura — I/O desacoplado
public interface GameIO {
    void println(String msg);
    String readLine();
    void syncBattleUi(...);
    void startHackUi(...);
}
// ConsoleIO e RemoteGameIO implementam a mesma interface
```

*Código modular, extensível, sem frameworks externos*

---

<!-- SLIDE 6 -->

## 📁 ORGANIZAÇÃO DO CÓDIGO

```
src/
├── Main.java / server/ServerMain.java
├── io/              GameIO, ConsoleIO, RemoteGameIO, UiState
├── server/          GameServer, GameSession
├── game/            Game.java (loop principal)
├── characters/      Personagem, Jogador, Inimigo, AssistenciaInteligente
├── battle/          BattleSystem, HackEscapeSystem
├── puzzles/         Puzzle + 5 tipos (if, while, for, operadores…)
├── items/           Item, DropTable
├── missions/        Mission, MissionSystem
└── levels/          LevelSystem

web/
├── index.html       Layout terminal + painel HP + hack overlay
├── css/style.css    Tema cyberpunk, animações, battle-mode
└── js/              api.js, main.js, ui.js, ascii.js
```

✔ encapsulamento · herança · polimorfismo · coleções · separação de camadas

---

<!-- SLIDE 7 -->

## ⚔️ BATALHA · TURNO, HACK & CONTRA-HACK

```
╔══════════════════════════════════════════════════════════════╗
║  PAINEL ESQUERDO          │  TERMINAL                      ║
║  ─────────────            │  ─────────                     ║
║  VIDA  ████████░░ 72/100  │  ⚡ BATALHA INICIADA            ║
║                           │  ▶ Firewall Corrompido          ║
║  INIMIGOS                 │                                ║
║  Firewall  ████░░ 28/40   │  [1] Atacar  [2] Defender       ║
║                           │  [3] Item    [4] Hackear        ║
╚══════════════════════════════════════════════════════════════╝
```

| Ação | Efeito |
|------|--------|
| **Atacar** | Dano = ATK efetivo − DEF inimigo |
| **Hackear** | Puzzle de código → bônus tático + passiva de classe |
| **Contra-hack** | Vírus Polimórfico invade: **30s** para resolver ou −15 HP/erro |
| **Overrides** | Analista controla inimigos hackeados (até 2) |

*switch-case · while(combate) · UiState sincronizado em tempo real*

---

<!-- SLIDE 8 -->

## 🧬 CLASSES DO OPERADOR

| Classe | Bônus base | Passiva |
|--------|------------|---------|
| **Programador** | ATK/DEF neutros | 80% buff ATK ou DEF (+3) por ação |
| **Hacker** | +5 ATK, −2 DEF | 60% debuff ATK ou DEF no inimigo |
| **Analista** | −2 ATK, +5 DEF | 20% controlar inimigo hackeado (Override) |
| **Engenheiro de IA** | +2 ATK/DEF, +10 HP | 30% spawn de Assistência Inteligente |

```
Inimigos disponíveis:
  Firewall Corrompido · Bug Voador · Processo Daemon
  Vírus Polimórfico (contra-hack) · Kernel Fantasma · …
```

*Cada classe muda a estratégia de combate e a curva de dificuldade*

---

<!-- SLIDE 9 -->

## 📈 PROGRESSÃO · XP, NÍVEL & FRAGMENTOS

```
┌────────────────────────────────────────────────────────────────┐
│   XP_necessário = Nível_atual × 100                            │
│                                                                │
│   Nível 1 → 100 XP │ Nível 2 → 200 XP │ …                     │
│                                                                │
│   Ao subir de nível:                                           │
│      Vida +12 · Ataque +6 · Defesa +4 · cura total             │
│                                                                │
│   Fragmentos de código → recompensa de missões e inimigos      │
└────────────────────────────────────────────────────────────────┘
```

- Batalhas e missões concedem **XP** e **fragmentos**
- Quantidade de inimigos por batalha escala com **nível** e **classe**
- Programador/Hacker: até 2 inimigos · Analista/Engenheiro: até 3

*LevelSystem.java · regras simples, progressão tangível*

---

<!-- SLIDE 10 -->

## 📜 MISSÕES · CÓDIGO NA LORE

```
┌────────────────────────────────────────────────────────────────┐
│  [ MISSÃO: CORREÇÃO DE CONDIÇÃO ]                              │
│  "Um módulo está em loop infinito. Corrija a condição."        │
│                                                                 │
│   while ( tentativas ___ 3 ) {                                 │
│       executarLogin();                                         │
│       tentativas++;                                            │
│   }                                                            │
│                                                                 │
│   ► Resposta correta:  ' < '                                   │
│   ✅ Recompensa: +80 XP · +20 fragmentos · item especial        │
└────────────────────────────────────────────────────────────────┘
```

**Tipos de puzzle (Fase 2):**
- Operador de comparação (`<`, `>`, `==`…)
- Valor de condição (if verdadeiro/falso)
- Operador do while
- Estrutura for (início, fim, passo)
- Expressão lógica (AND / OR)

*PuzzleSystem.java · interface Puzzle com polimorfismo*

---

<!-- SLIDE 11 -->

## 🎒 INVENTÁRIO · ITENS DIGITAIS

| Item | Efeito |
|------|--------|
| 💾 **Patch.exe** | Cura 30 HP |
| 🔥 **Hotfix v1.2** | Cura 50 HP (emergência) |
| ⚡ **Overclock.dll** | +5 ATK permanente |
| 🛡️ **Firewall.cfg** | +3 DEF permanente |
| 🤖 **Nanobot** | Cura 15 HP (drop comum) |

```
> Seu inventário:
  1. Patch.exe (cura 30)
  2. Overclock.dll (+5 ATK)
> Usar item durante batalha: opção [3]
```

*ArrayList<Item> · DropTable com sorteio aleatório pós-vitória*

---

<!-- SLIDE 12 -->

## 🖥️ INTERFACE WEB · EXPERIÊNCIA IMERSIVA

```
┌─ escape.ai — terminal://setor-7 ──────────────── [ONLINE] ─┐
│ ┌─ STATUS ─┐  ┌─ TERMINAL ─────────────────────────────────┐ │
│ │ VIDA      │  │ >> BATALHA INICIADA                         │ │
│ │ ████░ 72  │  │ >> Firewall ataca! −8 HP                    │ │
│ │           │  │                                               │ │
│ │ INIMIGOS  │  │ [Atacar] [Defender] [Item] [Hackear]          │ │
│ │ Firewall  │  └───────────────────────────────────────────────┘ │
│ │ ███░ 28   │                                                   │
│ └───────────┘                                                   │
└─────────────────────────────────────────────────────────────────┘
```

**Recursos visuais:**
- Painel HP desliza da esquerda na batalha
- Overlay vermelho fullscreen no contra-hack + timer regressivo
- Screen shake ao receber ou causar dano
- Arte ASCII para logo, boot, batalha e classes

**Como rodar:** `run-server.bat` → http://localhost:9090

---

<!-- SLIDE 13 -->

## 🎮 FLUXO DE JOGO · DEMONSTRAÇÃO

```
  INÍCIO → Criar Operador (nome + classe)
           ↓
  Menu: Explorar · Missões · Inventário · Status · Batalha
           ↓
  Missão 1: "Primeiro Boot" → Derrotar Firewall Corrompido
           ↓
  Combate: Atacar / Defender / Hackear / Item / Fugir
           ↓
  (Contra-hack?) → Puzzle 30s → Escapar ou perder HP
           ↓
  VITÓRIA → XP + fragmentos + drop de item
           ↓
  Missão 2: Puzzle de programação → Recompensa especial
           ↓
  Loop até completar arco inicial da simulação
```

> Comando secreto `vipoli` no menu → batalha imediata vs Vírus Polimórfico com contra-hack

*Game.java gerencia estados, transições e modos console/web*

---

<!-- SLIDE 14 -->

## 🧠 POO & ESTRUTURAS ESSENCIAIS

**Fundamentos POO**
- Classes e objetos · construtores · encapsulamento
- Herança: `Jogador` e `Inimigo` estendem `Personagem`
- Polimorfismo: `Puzzle` (5 implementações), `GameIO` (2 implementações)
- Composição: `BattleSystem` + `HackEscapeSystem` + `PuzzleSystem`

**Estruturas de controle**
- `if-else` / `switch-case` (menu de batalha)
- `while` (combate ativo, loop principal, contra-hack)
- `for` / streams (inventário, inimigos, overrides)

**Coleções**
- `ArrayList<Item>`, `ArrayList<Mission>`, `List<Inimigo> overrides`

*Preparado para save/load JSON e expansão de conteúdo*

---

<!-- SLIDE 15 -->

## 🚀 PRÓXIMOS PASSOS · FASE 3

```
╔══════════════════════════════════════════════════════════════╗
║   ► Sprites pixel art 32×32 (mapa top-down, personagens)      ║
║   ► Salvamento e carregamento de progresso (JSON)             ║
║   ► Puzzles avançados: arrays, métodos, recursão              ║
║   ► Sistema narrativo com escolhas e múltiplos finais         ║
║   ► Chefes temáticos com fases e mecânicas únicas             ║
║   ► Efeitos de status: corrupção, sobrecarga, regeneração     ║
║   ► Multiplayer / ranking (opcional)                          ║
╚══════════════════════════════════════════════════════════════╝
```

> A base atual entrega MVP jogável **no terminal e no navegador**, com arquitetura pronta para escalar sem reescrever a lógica.

*narrativa cyberpunk · desafios que ensinam programação*

---

<!-- SLIDE 16 -->

## 🏁 CONCLUSÃO

```
╔══════════════════════════════════════════════════════════════╗
║   ✅  Jogável no console E no navegador                         ║
║   ✅  Lógica 100% Java — frontend só renderiza                  ║
║   ✅  4 classes · batalhas táticas · contra-hack                ║
║   ✅  5 tipos de puzzle integrados à lore                       ║
║   ✅  POO, coleções e arquitetura em camadas                    ║
║   ✅  O código é parte essencial da narrativa                   ║
╚══════════════════════════════════════════════════════════════╝
```

**escape.ai** — um RPG cyberpunk que une diversão, desafios de lógica e os pilares da programação orientada a objetos.

```
  ┌─┐       ┌──────────────────┐
  │█│       │  ESCAPE REALITY   │
  └──┘       │  FRAGMENTOS: ?/?  │
 PROTAGONISTA└──────────────────┘
 [DIGITAL GHOST]
```

*escape.ai · onde cada linha de código importa*
