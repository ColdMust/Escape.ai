# Prompt para Manus — Sprites e Mapa | SIMULACRO.EXE

Copie o bloco abaixo e cole no Manus para gerar os assets visuais do jogo.

---

## PROMPT PRINCIPAL (cole isto no Manus)

```
Crie um pacote completo de pixel art para um RPG cyberpunk chamado "SIMULACRO.EXE".
O jogo se passa DENTRO de uma IA — o jogador é uma consciência presa em uma simulação
e precisa hackear o sistema para escapar. Estética: terminal hacker + matrix digital +
interior de mainframe dos anos 90 reinterpretado.

═══════════════════════════════════════
ESPECIFICAÇÕES TÉCNICAS (obrigatório)
═══════════════════════════════════════
- Estilo: pixel art 16-bit, top-down (visão de cima), limpo e legível
- Tamanho dos sprites de personagem/inimigo: 32×32 pixels cada frame
- Fundo: TRANSPARENTE (PNG) em todos os sprites
- Paleta principal:
  · Fundo/estrutura: #0a0e14, #121820, #1a2332
  · Acento neon azul: #58a6ff
  · Sucesso/operador: #3fb950
  · Perigo/corrupção: #f85149
  · Alerta: #d29922
  · Texto UI: #c9d1d9
- Cada personagem: 4 frames de animação idle (piscar/luz neon pulsando)
- Entregar arquivos PNG separados, nomes exatos listados abaixo

═══════════════════════════════════════
1. PERSONAGEM JOGADOR (4 classes)
═══════════════════════════════════════
Criar 4 variações do mesmo operador humano digitalizado (silhueta humanoide
feita de código/luz), cada uma com identidade visual distinta:

A) player_programador.png
   - Roupa/circuitos azul-claro (#58a6ff)
   - Óculos de HUD, postura equilibrada
   - Símbolo: chaves de código { }

B) player_hacker.png
   - Capuz escuro, máscara com glitch vermelho
   - Cabos saindo das mangas, postura agressiva
   - Símbolo: cursor piscando _

C) player_analista.png
   - Tablet holográfico, aura defensiva verde
   - Antenas de rede, postura de suporte
   - Símbolo: gráfico de barras

D) player_engenheiro.png
   - Macacão com logos de IA, drones minúsculos ao redor
   - Laranja + azul, postura técnica
   - Símbolo: engrenagem com neurônio

Também criar player.png (versão padrão = Programador) para uso genérico.

═══════════════════════════════════════
2. INIMIGOS (4 tipos — são bugs do sistema)
═══════════════════════════════════════

A) firewall.png — "Firewall Corrompido"
   - Parece um escudo digital rachado, chamas de código vermelho
   - Forma quadrada/blocada, bordas de parede de fogo pixelado
   - Olhos de alerta amarelos no centro

B) bug.png — "Bug Voador"
   - Inseto mecânico pequeno feito de pixels errados
   - Asas de asci art, rastro de glitch
   - Cores: magenta + verde tóxico

C) daemon.png — "Processo Daemon"
   - Massa sombria flutuante com símbolos de terminal
   - Maior que os outros (pode ser 48×48 se necessário)
   - Tentáculos de cabo USB, olho único vermelho

D) virus.png — "Vírus Polimórfico"
   - Forma instável que parece mudar (usar bordas irregulares)
   - Metade de cada lado com cor diferente (polimorfismo visual)
   - Partículas de corrupção ao redor

Bônus: assistencia_ia.png — robô drone aliado pequeno (24×24),
       holograma azul, gerado pelo Engenheiro de IA.

═══════════════════════════════════════
3. TILESET DO MAPA (tileset.png)
═══════════════════════════════════════
Sprite sheet horizontal com tiles 32×32, fundo transparente onde aplicável:

Coluna 0 — chão_piso.png: piso de servidor escuro com linhas de circuito sutis
Coluna 1 — chão_parede.png: parede de data center com painéis
Coluna 2 — chão_no.png: nó do sistema (círculo neon azul pulsante no chão)
Coluna 3 — chão_corrompido.png: tile infectado (vermelho/verde glitch)
Coluna 4 — chão_dados.png: pilha de fragmentos de código verde

Layout do sprite sheet: 5 tiles lado a lado = 160×32 pixels total
Nome do arquivo: tileset.png

═══════════════════════════════════════
4. MAPA COMPLETO (mapa_setor7.png)
═══════════════════════════════════════
Mapa top-down do "SETOR-7" — interior de uma IA, 480×480 pixels (15×15 tiles de 32px).

Layout:
- Bordas: paredes de servidor
- Centro: sala do KERNEL (nó azul brilhante) — área de boss/escape
- Corredores conectando 4 salas nos cantos
- 3 zonas corrompidas (tiles vermelhos) onde spawnam inimigos
- 4 salas de dados (tiles verdes) onde há fragmentos de código
- 4 nós de rede (azul) nos corredores principais
- Atmosfera: escuro, névoa digital sutil, fios de luz nos corredores

O mapa deve ser jogável visualmente — corredores de 1 tile de largura mínimo,
sem becos sem saída. Estética de dungeon de mainframe.

═══════════════════════════════════════
5. ENTREGA
═══════════════════════════════════════
Gerar todos os PNGs listados, organizados assim:
/assets/sprites/player.png
/assets/sprites/player_programador.png
/assets/sprites/player_hacker.png
/assets/sprites/player_analista.png
/assets/sprites/player_engenheiro.png
/assets/sprites/firewall.png
/assets/sprites/bug.png
/assets/sprites/daemon.png
/assets/sprites/virus.png
/assets/sprites/assistencia_ia.png
/assets/sprites/tileset.png
/assets/sprites/mapa_setor7.png

Manter coerência visual entre todos os assets — mesmo universo, mesma paleta,
mesmo nível de detalhe pixel art.
```

---

## PROMPTS INDIVIDUAIS (se o Manus funcionar melhor por asset)

### Personagem
> Pixel art 32×32 top-down, operador digital preso dentro de uma IA, silhueta humanoide feita de luz e código, neon azul #58a6ff e verde #3fb950, fundo transparente, 4 frames idle, estilo cyberpunk RPG retrô.

### Inimigo — Firewall Corrompido
> Pixel art 32×32 top-down, escudo de firewall digital rachado e infectado, chamas de código vermelho #f85149, olhos de alerta amarelos, interior de mainframe, fundo transparente, estilo SIMULACRO.EXE cyberpunk.

### Inimigo — Bug Voador
> Pixel art 32×32 top-down, inseto mecânico de bug de software, asas de glitch, magenta e verde tóxico, erro de runtime com consciência, fundo transparente, cyberpunk pixel art.

### Mapa SETOR-7
> Pixel art mapa top-down 480×480, dungeon interior de IA/mainframe, corredores escuros #0a0e14, nós de rede neon azul, zonas corrompidas vermelhas, salas de dados verdes, estilo RPG cyberpunk SETOR-7, tiles 32×32.

---

## Depois de gerar

Salve os arquivos em `web/assets/sprites/` com os nomes indicados.
O jogo já está preparado para carregá-los automaticamente no painel direito.
