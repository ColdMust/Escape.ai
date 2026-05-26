package levels;

import characters.Jogador;

public class LevelSystem {

    /** XP necessário para chegar ao nível 'nivel' */
    public static int xpNecessario(int nivel) {
        return nivel * 100;
    }

    /** Exibe ficha completa do personagem */
    public static void exibirFicha(Jogador j) {
        System.out.println("\n" + "═".repeat(40));
        System.out.println("       👤  FICHA DO PERSONAGEM");
        System.out.println("═".repeat(40));
        System.out.printf("  Nome:    %s%n", j.getNome());
        System.out.printf("  Classe:  %s%n", j.getClasse().nome);
        System.out.printf("  Nível:   %d%n", j.getNivel());
        System.out.println("  HP:   " + j.barraDeVida());
        System.out.println("  XP:   " + j.barraXP());
        System.out.printf("  Ataque:  %d%n", j.getAtaque());
        System.out.printf("  Defesa:  %d%n", j.getDefesa());
        System.out.printf("  Fragmentos de Código: %d%n", j.getFragmentos());
        System.out.println("─".repeat(40));
        System.out.println("  Inventário (" + j.getInventario().size() + " itens):");
        if (j.getInventario().isEmpty()) {
            System.out.println("  (vazio)");
        } else {
            for (int i = 0; i < j.getInventario().size(); i++) {
                System.out.println("  [" + (i + 1) + "] " + j.getInventario().get(i));
            }
        }
        System.out.println("═".repeat(40));
    }
}
