package puzzles;

import java.util.Scanner;

public interface Puzzle {

    String getId();

    String getTitulo();

    /** Texto de lore cyberpunk exibido antes do desafio. */
    String getLore();

    /**
     * Executa o puzzle interativamente.
     *
     * @return {@code true} se o jogador resolveu corretamente.
     */
    boolean executar(Scanner scanner);
}
