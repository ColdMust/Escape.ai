package puzzles;

import io.GameIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PuzzleSystem {

    private static final Random RNG = new Random();
    private final Map<String, Puzzle> puzzles = new HashMap<>();
    private final List<Puzzle> lista = new ArrayList<>();

    public PuzzleSystem() {
        registrar(new OperadorWhilePuzzle());
        registrar(new OperadorComparacaoPuzzle());
        registrar(new ValorCondicaoPuzzle());
        registrar(new EstruturaForPuzzle());
        registrar(new ExpressaoLogicaPuzzle());
    }

    private void registrar(Puzzle puzzle) {
        puzzles.put(puzzle.getId(), puzzle);
        lista.add(puzzle);
    }

    public boolean existe(String puzzleId) {
        return puzzleId != null && puzzles.containsKey(puzzleId);
    }

    public Puzzle sortearAleatorio() {
        return lista.get(RNG.nextInt(lista.size()));
    }

    public Puzzle get(String puzzleId) {
        return puzzles.get(puzzleId);
    }

    public boolean validarResposta(Puzzle puzzle, int resposta) {
        return puzzle != null && puzzle.validarResposta(resposta);
    }

    public boolean executar(String puzzleId, GameIO io) {
        Puzzle puzzle = puzzles.get(puzzleId);
        if (puzzle == null) {
            io.println("[ERRO] Puzzle não encontrado: " + puzzleId);
            return false;
        }

        io.println("\n" + "═".repeat(50));
        io.println("      🧩  PUZZLE DE PROGRAMAÇÃO");
        io.println("      " + puzzle.getTitulo());
        io.println("═".repeat(50));
        io.println(puzzle.getLore());
        io.println("─".repeat(50));

        return puzzle.executar(io);
    }
}
