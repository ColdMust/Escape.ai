package battle;

import characters.Jogador;
import characters.Inimigo;
import items.Item;
import java.util.Scanner;

public class BattleSystem {

    private Scanner scanner;

    public BattleSystem(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Executa uma batalha completa entre o jogador e um inimigo.
     * Retorna true se o jogador venceu, false se foi derrotado.
     */
    public boolean iniciarBatalha(Jogador jogador, Inimigo inimigo) {
        System.out.println("\n" + "═".repeat(40));
        System.out.println("         ⚡  BATALHA INICIADA  ⚡");
        System.out.println("═".repeat(40));
        System.out.println("▶ " + inimigo.getNome() + " apareceu!");
        System.out.println("  \"" + inimigo.getDescricao() + "\"");
        System.out.println("═".repeat(40));

        int turno = 1;

        while (jogador.estaVivo() && inimigo.estaVivo()) {
            System.out.println("\n── Turno " + turno + " ──────────────────────────");
            exibirStatus(jogador, inimigo);

            // ── Turno do jogador ──────────────────────────────
            int acao = escolherAcao(jogador);
            jogador.setDefendendo(false);

            switch (acao) {
                case 1: // Atacar
                    int dano = jogador.calcularDano(inimigo);
                    inimigo.receberDano(dano);
                    System.out.println("\n⚔  Você atacou " + inimigo.getNome()
                            + " causando " + dano + " de dano!");
                    break;

                case 2: // Defender
                    jogador.setDefendendo(true);
                    System.out.println("\n🛡  Você assumiu postura defensiva.");
                    break;

                case 3: // Usar Item
                    usarItem(jogador);
                    break;

                case 4: // Hackear
                    int danoHack = jogador.hackear(inimigo);
                    System.out.println("\n💻 HACK executado em " + inimigo.getNome()
                            + "! Dano: " + danoHack + " (bônus de classe aplicado)");
                    break;

                default:
                    System.out.println("Ação inválida. Você perdeu o turno.");
            }

            if (!inimigo.estaVivo()) break;

            // ── Turno do inimigo ──────────────────────────────
            int danoInimigo = inimigo.calcularDano(jogador);
            jogador.receberDano(danoInimigo);
            System.out.println("💢 " + inimigo.getNome() + " causou "
                    + danoInimigo + " de dano em você!");

            jogador.setDefendendo(false);
            turno++;

            pausar();
        }

        return processarResultado(jogador, inimigo);
    }

    // ── Exibição de status ────────────────────────────────────────
    private void exibirStatus(Jogador j, Inimigo i) {
        System.out.println("👤 " + j.getNome() + " (Nv." + j.getNivel() + ")");
        System.out.println("   HP " + j.barraDeVida());
        System.out.println("💀 " + i.getNome());
        System.out.println("   HP " + i.barraDeVida());
    }

    // ── Menu de ação ─────────────────────────────────────────────
    private int escolherAcao(Jogador jogador) {
        System.out.println("\n┌─ O que você fará? ─────────────────┐");
        System.out.println("│  [1] ⚔  Atacar                     │");
        System.out.println("│  [2] 🛡  Defender                   │");
        System.out.println("│  [3] 🎒  Usar Item                  │");
        System.out.println("│  [4] 💻  Hackear                    │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("   Escolha: ");

        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ── Uso de item ───────────────────────────────────────────────
    private void usarItem(Jogador jogador) {
        if (jogador.getInventario().isEmpty()) {
            System.out.println("\n🎒 Inventário vazio!");
            return;
        }
        System.out.println("\n🎒 INVENTÁRIO:");
        for (int i = 0; i < jogador.getInventario().size(); i++) {
            System.out.println("   [" + (i + 1) + "] " + jogador.getInventario().get(i));
        }
        System.out.print("   Qual item usar? (0 = cancelar): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx == -1) {
                System.out.println("Cancelado.");
                return;
            }
            Item item = jogador.getInventario().get(idx);
            jogador.usarItem(idx);
            System.out.println("✅ Você usou " + item.getNome() + ".");
        } catch (Exception e) {
            System.out.println("Índice inválido.");
        }
    }

    // ── Resultado ─────────────────────────────────────────────────
    private boolean processarResultado(Jogador jogador, Inimigo inimigo) {
        System.out.println("\n" + "═".repeat(40));
        if (jogador.estaVivo()) {
            System.out.println("     ✅  VITÓRIA!");
            System.out.println("═".repeat(40));

            boolean subiu = jogador.ganharXP(inimigo.getXpRecompensa());
            jogador.adicionarFragmentos(inimigo.getFragmentosRecompensa());

            System.out.println("+ " + inimigo.getXpRecompensa() + " XP");
            System.out.println("+ " + inimigo.getFragmentosRecompensa() + " Fragmentos de Código");

            if (inimigo.getItemDrop() != null) {
                jogador.adicionarItem(inimigo.getItemDrop());
                System.out.println("+ Item obtido: " + inimigo.getItemDrop().getNome());
            }

            if (subiu) {
                System.out.println("\n🎉 LEVEL UP! Agora você é nível " + jogador.getNivel() + "!");
                System.out.println("   HP restaurado completamente.");
                System.out.println("   Atributos aumentados.");
            }
            return true;
        } else {
            System.out.println("     ❌  DERROTA...");
            System.out.println("═".repeat(40));
            System.out.println("O sistema reiniciou sua instância...");
            return false;
        }
    }

    private void pausar() {
        System.out.print("\n[Enter para continuar...]");
        scanner.nextLine();
    }
}
