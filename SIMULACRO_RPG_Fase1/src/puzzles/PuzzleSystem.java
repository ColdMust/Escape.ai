package puzzles;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PuzzleSystem {

    private final Map<String, Puzzle> puzzles = new HashMap<>();

    public PuzzleSystem() {
        registrar(new OperadorWhilePuzzle());
        registrar(new OperadorComparacaoPuzzle());
        registrar(new ValorCondicaoPuzzle());
        registrar(new EstruturaForPuzzle());
        registrar(new ExpressaoLogicaPuzzle());
    }

    private void registrar(Puzzle puzzle) {
        puzzles.put(puzzle.getId(), puzzle);
    }

    public boolean existe(String puzzleId) {
        return puzzleId != null && puzzles.containsKey(puzzleId);
    }

    /**
     * Executa o puzzle associado ao ID.
     *
     * @return {@code true} se resolvido com sucesso.
     */
    public boolean executar(String puzzleId, Scanner scanner) {
        Puzzle puzzle = puzzles.get(puzzleId);
        if (puzzle == null) {
            System.out.println("[ERRO] Puzzle não encontrado: " + puzzleId);
            return false;
        }

        System.out.println("\n" + "═".repeat(50));
        System.out.println("      🧩  PUZZLE DE PROGRAMAÇÃO");
        System.out.println("      " + puzzle.getTitulo());
        System.out.println("═".repeat(50));
        System.out.println(puzzle.getLore());
        System.out.println("─".repeat(50));

        return puzzle.executar(scanner);
    }
}
