package levels;

import characters.Jogador;
import io.GameIO;

public class LevelSystem {

    public static int xpNecessario(int nivel) {
        return nivel * 100;
    }

    public static void exibirFicha(Jogador j, GameIO io) {
        io.println("\n" + "═".repeat(40));
        io.println("       👤  FICHA DO PERSONAGEM");
        io.println("═".repeat(40));
        io.println("  Nome:    " + j.getNome());
        io.println("  Classe:  " + j.getClasse().nome);
        io.println("  Nível:   " + j.getNivel());
        io.println("  HP:   " + j.barraDeVida());
        io.println("  XP:   " + j.barraXP());
        io.println("  Ataque:  " + j.getAtaque());
        io.println("  Defesa:  " + j.getDefesa());
        io.println("  Fragmentos de Código: " + j.getFragmentos());
        io.println("─".repeat(40));
        io.println("  Inventário (" + j.getInventario().size() + " itens):");
        if (j.getInventario().isEmpty()) {
            io.println("  (vazio)");
        } else {
            for (int i = 0; i < j.getInventario().size(); i++) {
                io.println("  [" + (i + 1) + "] " + j.getInventario().get(i));
            }
        }
        io.println("═".repeat(40));
    }
}
