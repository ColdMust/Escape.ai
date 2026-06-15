package battle;

import characters.AssistenciaInteligente;
import characters.Inimigo;
import characters.Jogador;
import items.Item;

import java.util.*;
import java.util.stream.Collectors;

public class BattleSystem {

    private final Scanner scanner;
    private static final Random RNG = new Random();

    public BattleSystem(Scanner scanner) { this.scanner = scanner; }

    // ════════════════════════════════════════════════════════════
    //  Sorteio de quantidade de inimigos
    // ════════════════════════════════════════════════════════════

    public static int sortearQuantidadeInimigos(Jogador jogador) {
        int nv = jogador.getNivel();
        switch (jogador.getClasse()) {
            case PROGRAMADOR: case HACKER: {
                double chance2 = Math.min(1.0, (nv - 1) * 0.05);
                return RNG.nextDouble() < chance2 ? 2 : 1;
            }
            case ANALISTA: case ENGENHEIRO: {
                double chance3 = Math.min(1.0, 0.02 + (nv - 1) * 0.05);
                double chance2 = Math.min(1.0, 0.05 + (nv - 1) * 0.05);
                double roll = RNG.nextDouble();
                if (roll < chance3)           return 3;
                if (roll < chance3 + chance2) return 2;
                return 1;
            }
            default: return 1;
        }
    }

    // ════════════════════════════════════════════════════════════
    //  Entrada
    // ════════════════════════════════════════════════════════════

    public boolean iniciarBatalha(Jogador jogador, Inimigo inimigo) {
        return iniciarBatalha(jogador, new ArrayList<>(Collections.singletonList(inimigo)));
    }

    public boolean iniciarBatalha(Jogador jogador, List<Inimigo> inimigos) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("             ⚡  BATALHA INICIADA  ⚡");
        System.out.println("═".repeat(50));
        for (Inimigo i : inimigos)
            System.out.println("▶ " + i.getNome() + " — \"" + i.getDescricao() + "\"");
        System.out.println("═".repeat(50));

        int turno = 1;

        while (jogador.estaVivo() && temLivres(inimigos)) {

            System.out.println("\n── Turno " + turno + " " + "─".repeat(43));

            // Reset imunidades no início do turno
            for (Inimigo i : inimigos) if (i.isControlado()) i.resetarImuneTurno();
            AssistenciaInteligente aiAtual = jogador.getAssistencia();
            if (aiAtual != null && aiAtual.isInicializando()) aiAtual.finalizarInicializacao();

            exibirStatus(jogador, inimigos);

            int overridesAntes = jogador.getOverrides().size();

            jogador.setDefendendo(false);
            int acao = escolherAcao();
            Inimigo alvo = escolherAlvo(getLivres(inimigos));

            processarAcao(jogador, inimigos, alvo, acao);

            boolean novoOverride = jogador.getOverrides().size() > overridesAntes;

            processarPassivas(jogador, inimigos, alvo, novoOverride);

            // Passiva Engenheiro
            AssistenciaInteligente nova = jogador.passivaEngenheiro();
            if (nova != null) {
                System.out.println("\n🤖 [ENGENHEIRO] Assistência gerada! HP:" + nova.getVida()
                        + " ATK:" + nova.getAtaque() + " DEF:" + nova.getDefesa()
                        + " — age no próximo turno.");
            }

            // Turno da Assistência (não age se inicializando)
            AssistenciaInteligente ai = jogador.getAssistencia();
            if (ai != null && ai.estaVivo() && !ai.isInicializando()) {
                System.out.println(ai.agirAutomaticamente(inimigos));
                verificarMortesInimigos(inimigos, jogador);
            }

            // Turno dos Overrides
            processarTurnoOverrides(jogador, inimigos, novoOverride);

            if (!temLivres(inimigos)) break;

            processarTurnoInimigos(jogador, inimigos);

            turno++;
            pausar();
        }

        return processarResultado(jogador, inimigos);
    }

    // ════════════════════════════════════════════════════════════
    //  Ação do jogador
    // ════════════════════════════════════════════════════════════

    private void processarAcao(Jogador jogador, List<Inimigo> inimigos, Inimigo alvo, int acao) {
        switch (acao) {
            case 1:
                if (alvo == null) { System.out.println("Sem alvos."); break; }
                int d = jogador.calcularDano(alvo); alvo.receberDano(d);
                System.out.println("\n⚔  Você atacou " + alvo.getNome() + " — " + d + " dano!");
                verificarMorte(alvo, jogador);
                break;
            case 2:
                jogador.setDefendendo(true);
                System.out.println("\n🛡  Postura defensiva.");
                break;
            case 3:
                usarItem(jogador);
                break;
            case 4:
                if (alvo == null) { System.out.println("Sem alvos."); break; }
                int dh = jogador.hackear(alvo);
                System.out.println("\n💻 HACK em " + alvo.getNome() + "! Dano: " + dh);
                verificarMorte(alvo, jogador);
                if (alvo.estaVivo()) {
                    String msg = jogador.passivaAnalistaControle(inimigos, alvo);
                    if (msg != null) System.out.println(msg);
                }
                break;
            default:
                System.out.println("Ação inválida — turno perdido.");
        }
    }

    // ════════════════════════════════════════════════════════════
    //  Passivas pós-ação
    // ════════════════════════════════════════════════════════════

    private void processarPassivas(Jogador jogador, List<Inimigo> inimigos,
                                    Inimigo alvo, boolean novoOverride) {
        switch (jogador.getClasse()) {
            case PROGRAMADOR: {
                String m = jogador.passivaProgramador();
                if (m != null) System.out.println(m);
                break;
            }
            case HACKER: {
                Inimigo alvoDebuff = (alvo != null && alvo.estaVivo()) ? alvo
                        : getLivres(inimigos).stream().findFirst().orElse(null);
                String m = jogador.passivaHacker(alvoDebuff);
                if (m != null) System.out.println(m);
                break;
            }
            case ANALISTA: {
                jogador.passivaBufOverrides().forEach(System.out::println);
                break;
            }
            default: break; // ENGENHEIRO não tem passiva pós-ação neste bloco
        }
    }

    // ════════════════════════════════════════════════════════════
    //  Turno dos Overrides
    // ════════════════════════════════════════════════════════════

    private void processarTurnoOverrides(Jogador jogador, List<Inimigo> inimigos,
                                          boolean novoOverrideCriado) {
        List<Inimigo> mortos = new ArrayList<>();
        List<Inimigo> overrides = jogador.getOverrides();

        for (Inimigo ov : overrides) {
            if (!ov.estaVivo()) { mortos.add(ov); continue; }

            // O Override mais recente não age no turno em que foi criado
            if (novoOverrideCriado && ov == overrides.get(overrides.size() - 1)) {
                System.out.println("  🔧 " + ov.getNome()
                        + " [OVERRIDE] sendo reprogramado — age no próximo turno.");
                continue;
            }

            List<Inimigo> alvos = getLivres(inimigos);
            if (!alvos.isEmpty() && RNG.nextDouble() < 0.70) {
                Inimigo t = alvos.get(RNG.nextInt(alvos.size()));
                int dano = ov.calcularDano(t); t.receberDano(dano);
                System.out.println("⚙ " + ov.getNome() + " [OVERRIDE] atacou "
                        + t.getNome() + " — " + dano + " dano!");
                if (!t.estaVivo())
                    System.out.println("  💀 " + t.getNome() + " derrotado pelo Override!");
            } else {
                ov.setDefendendo(true);
                System.out.println("🛡 " + ov.getNome() + " [OVERRIDE] defendendo.");
            }
        }

        for (Inimigo m : mortos) {
            System.out.println("😔 " + m.getNome() + " [OVERRIDE] foi derrotado!");
            jogador.removerOverride(m);
        }
    }

    // ════════════════════════════════════════════════════════════
    //  Turno dos inimigos
    // ════════════════════════════════════════════════════════════

    private void processarTurnoInimigos(Jogador jogador, List<Inimigo> inimigos) {
        AssistenciaInteligente ai = jogador.getAssistencia();
        List<Inimigo> overrides = jogador.getOverrides();

        for (Inimigo ini : inimigos) {
            if (!ini.estaVivo() || ini.isControlado()) continue;

            List<String> alvosIds = new ArrayList<>(Collections.singletonList("JOGADOR"));
            if (ai != null && ai.estaVivo() && !ai.isInicializando()) alvosIds.add("AI");
            for (int i = 0; i < overrides.size(); i++) {
                Inimigo ov = overrides.get(i);
                if (ov.estaVivo() && !ov.isImuneTurno()) alvosIds.add("OV" + i);
            }

            String escolha = alvosIds.get(RNG.nextInt(alvosIds.size()));

            if (escolha.equals("AI")) {
                int dano = ini.calcularDano(ai); ai.receberDano(dano);
                System.out.println("💢 " + ini.getNome() + " causou " + dano + " na Assistência!");
                if (!ai.estaVivo()) {
                    System.out.println("  💀 Assistência destruída!");
                    jogador.removerAssistencia();
                }
            } else if (escolha.startsWith("OV")) {
                int idx = Integer.parseInt(escolha.substring(2));
                Inimigo ov = overrides.get(idx);
                int dano = ini.calcularDano(ov); ov.receberDano(dano);
                System.out.println("💢 " + ini.getNome() + " causou " + dano
                        + " em " + ov.getNome() + " [OVERRIDE]!");
                if (!ov.estaVivo()) {
                    System.out.println("  💀 " + ov.getNome() + " [OVERRIDE] destruído!");
                    jogador.removerOverride(ov);
                }
            } else {
                int dano = ini.calcularDano(jogador); jogador.receberDano(dano);
                System.out.println("💢 " + ini.getNome() + " causou " + dano + " em você!");
            }

            ini.setDefendendo(false);
            if (!jogador.estaVivo()) break;
        }
    }

    // ════════════════════════════════════════════════════════════
    //  Status
    // ════════════════════════════════════════════════════════════

    private void exibirStatus(Jogador j, List<Inimigo> inimigos) {
        System.out.println("👤 " + j.getNome() + " Nv." + j.getNivel()
                + " [" + j.getClasse().nome + "]  HP " + j.barraDeVida());
        if (j.getBuffAtaque() > 0 || j.getBuffDefesa() > 0)
            System.out.println("   ↑ ATK+" + j.getBuffAtaque() + " DEF+" + j.getBuffDefesa());

        AssistenciaInteligente ai = j.getAssistencia();
        if (ai != null && ai.estaVivo())
            System.out.println("🤖 Assistência  HP " + ai.barraDeVida()
                    + (ai.isInicializando() ? " [inicializando]" : ""));

        for (Inimigo ov : j.getOverrides()) {
            if (ov.estaVivo()) {
                System.out.println("⚙  " + ov.getNome() + " [OVERRIDE]  HP " + ov.barraDeVida()
                        + (ov.isImuneTurno() ? " [imune]" : ""));
                if (ov.getBuffAtaque() > 0 || ov.getBuffDefesa() > 0)
                    System.out.println("   ↑ ATK+" + ov.getBuffAtaque() + " DEF+" + ov.getBuffDefesa());
            }
        }

        for (Inimigo i : inimigos) {
            if (!i.isControlado())
                System.out.println("💀 " + i.getNome() + "  HP " + i.barraDeVida()
                        + (i.getDebuffAtaque() > 0 || i.getDebuffDefesa() > 0
                        ? "  ↓ATK-" + i.getDebuffAtaque() + " DEF-" + i.getDebuffDefesa() : ""));
        }
    }

    // ════════════════════════════════════════════════════════════
    //  Resultado
    // ════════════════════════════════════════════════════════════

    private boolean processarResultado(Jogador jogador, List<Inimigo> inimigos) {
        System.out.println("\n" + "═".repeat(50));
        if (!jogador.estaVivo()) {
            System.out.println("           ❌  DERROTA...\nO sistema reiniciou sua instância...");
            System.out.println("═".repeat(50));
            return false;
        }

        System.out.println("           ✅  VITÓRIA!");
        System.out.println("═".repeat(50));

        int xpTotal = 0, fragTotal = 0;
        for (Inimigo i : inimigos) {
            if (!i.isControlado()) {
                xpTotal   += i.getXpRecompensa();
                fragTotal += i.getFragmentosRecompensa();
            }
            Item drop = i.rolarDrop();
            if (drop != null) {
                jogador.adicionarItem(drop);
                System.out.println("💾 Drop de " + i.getNome() + ": " + drop.getNome());
            }
        }

        jogador.adicionarFragmentos(fragTotal);
        System.out.println("+ " + xpTotal + " XP  + " + fragTotal + " Fragmentos");

        if (jogador.ganharXP(xpTotal))
            System.out.println("🎉 LEVEL UP! Agora nível " + jogador.getNivel() + "!");

        jogador.limparOverrides();
        jogador.removerAssistencia();
        return true;
    }

    // ════════════════════════════════════════════════════════════
    //  Helpers
    // ════════════════════════════════════════════════════════════

    private List<Inimigo> getLivres(List<Inimigo> inimigos) {
        return inimigos.stream().filter(i -> i.estaVivo() && !i.isControlado())
                .collect(Collectors.toList());
    }

    private boolean temLivres(List<Inimigo> inimigos) {
        return inimigos.stream().anyMatch(i -> i.estaVivo() && !i.isControlado());
    }

    private Inimigo escolherAlvo(List<Inimigo> disp) {
        if (disp.isEmpty()) return null;
        if (disp.size() == 1) return disp.get(0);
        System.out.println("\n🎯 Escolha o alvo:");
        for (int i = 0; i < disp.size(); i++)
            System.out.println("  [" + (i+1) + "] " + disp.get(i).getNome()
                    + " HP:" + disp.get(i).getVida() + "/" + disp.get(i).getVidaMax());
        System.out.print("  Alvo: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < disp.size()) return disp.get(idx);
        } catch (NumberFormatException ignored) {}
        return disp.get(0);
    }

    private int escolherAcao() {
        System.out.println("\n┌─ Ação ──────────────────┐");
        System.out.println("│ [1]⚔ Atacar  [2]🛡 Def  │");
        System.out.println("│ [3]🎒 Item   [4]💻 Hack  │");
        System.out.println("└─────────────────────────┘");
        System.out.print("  Escolha: ");
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private void usarItem(Jogador jogador) {
        if (jogador.getInventario().isEmpty()) { System.out.println("🎒 Inventário vazio!"); return; }
        System.out.println("🎒 INVENTÁRIO:");
        for (int i = 0; i < jogador.getInventario().size(); i++)
            System.out.println("  [" + (i+1) + "] " + jogador.getInventario().get(i));
        System.out.print("  Item (0=cancelar): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx == -1) return;
            Item it = jogador.getInventario().get(idx);
            jogador.usarItem(idx);
            System.out.println("✅ Usado: " + it.getNome());
        } catch (Exception e) { System.out.println("Inválido."); }
    }

    private void verificarMorte(Inimigo i, Jogador jogador) {
        if (!i.estaVivo()) {
            System.out.println("  💀 " + i.getNome() + " derrotado!");
            jogador.removerOverride(i);
        }
    }

    private void verificarMortesInimigos(List<Inimigo> inimigos, Jogador jogador) {
        inimigos.stream()
                .filter(i -> !i.estaVivo() && i.isControlado())
                .forEach(i -> {
                    System.out.println("  💀 " + i.getNome() + " [OVERRIDE] derrotado!");
                    jogador.removerOverride(i);
                });
    }

    private void pausar() {
        System.out.print("\n[Enter para continuar...]");
        scanner.nextLine();
    }
}


