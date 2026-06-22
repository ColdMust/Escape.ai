package battle;

import characters.Inimigo;
import characters.Jogador;
import io.GameIO;
import puzzles.Puzzle;
import puzzles.PuzzleSystem;

public class HackEscapeSystem {

    public static final int TEMPO_SEGUNDOS = 30;
    public static final int DANO_ERRO = 15;

    private final PuzzleSystem puzzles = new PuzzleSystem();

    /**
     * Inimigo hackeia o jogador — 30s para resolver puzzle ou perder HP.
     * @return true se o jogador escapou do hack.
     */
    public boolean executar(Jogador jogador, Inimigo hacker, GameIO io) {
        Puzzle puzzle = puzzles.sortearAleatorio();
        long deadline = System.currentTimeMillis() + TEMPO_SEGUNDOS * 1000L;

        io.println();
        io.println("╔══════════════════════════════════════════╗");
        io.println("║   ⚠  CONTRA-HACK DETECTADO  ⚠            ║");
        io.println("╚══════════════════════════════════════════╝");
        io.println();
        io.println("💀 " + hacker.getNome() + " invadiu sua consciência!");
        io.println(">> Você tem " + TEMPO_SEGUNDOS + " segundos para corrigir o código e fugir!");
        io.println(">> Cada erro drenará " + DANO_ERRO + " HP da sua instância.");
        io.println();

        io.startHackUi(
            hacker.getNome(),
            deadline,
            puzzle.getTitulo(),
            puzzle.getLore(),
            puzzle.getCorpo(),
            puzzle.getPergunta(),
            puzzle.getOpcoesTexto()
        );

        while (jogador.estaVivo()) {
            int restante = segundosRestantes(deadline);
            io.updateHackTimer(restante);

            if (restante <= 0) {
                io.println("\n⏱  TEMPO ESGOTADO! O vírus corrompeu sua instância.");
                jogador.receberDano(DANO_ERRO * 2);
                io.endHackUi();
                return false;
            }

            io.println("⏱  Tempo restante: " + restante + "s");
            io.println();
            io.println("Código corrompido:");
            io.println(puzzle.getCorpo());
            io.println();
            io.println(puzzle.getPergunta());
            io.println("  " + puzzle.getOpcoesTexto());
            io.print("\nSua resposta: ");

            String linha = io.readLineUntil(deadline);
            if (linha == null) {
                io.println("\n⏱  TEMPO ESGOTADO!");
                jogador.receberDano(DANO_ERRO * 2);
                io.endHackUi();
                return false;
            }

            int resp;
            try {
                resp = Integer.parseInt(linha.trim());
            } catch (NumberFormatException e) {
                io.println("❌ Entrada inválida! -" + DANO_ERRO + " HP");
                jogador.receberDano(DANO_ERRO);
                io.notifyPlayerDamaged(jogador.getVida(), jogador.getVidaMax());
                io.println("   HP: " + jogador.getVida() + "/" + jogador.getVidaMax());
                continue;
            }

            if (puzzles.validarResposta(puzzle, resp)) {
                io.println("\n✅ FIREWALL QUEBRADO! Você escapou do contra-hack!");
                io.println("   " + hacker.getNome() + " perdeu o controle da sua instância.");
                io.endHackUi();
                return true;
            }

            io.println("\n❌ RESPOSTA ERRADA! -" + DANO_ERRO + " HP");
            jogador.receberDano(DANO_ERRO);
            io.notifyPlayerDamaged(jogador.getVida(), jogador.getVidaMax());
            io.println("   HP: " + jogador.getVida() + "/" + jogador.getVidaMax());
        }

        io.endHackUi();
        return false;
    }

    private int segundosRestantes(long deadline) {
        return Math.max(0, (int) ((deadline - System.currentTimeMillis() + 999) / 1000));
    }
}
