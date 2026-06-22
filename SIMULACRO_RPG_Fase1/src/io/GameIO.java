package io;

/**
 * Abstração de entrada/saída — permite que a lógica do jogo rode
 * no terminal (ConsoleIO) ou via API web (RemoteGameIO).
 */
public interface GameIO {

    void print(String text);

    void println(String text);

    default void println() {
        println("");
    }

    /** Bloqueia até o jogador enviar uma linha de texto. */
    String readLine();

    default void pause(String message) {
        print(message);
        readLine();
    }

    /** Lê entrada com prazo (ms). Retorna null se expirou. */
    default String readLineUntil(long deadlineMs) {
        return readLine();
    }

    default void syncBattleUi(String playerName, int hp, int maxHp,
                              java.util.List<UiState.EnemyBar> enemies) {}

    default void startBattleUi(String playerName, int hp, int maxHp) {}

    default void endBattleUi() {}

    default void startHackUi(String hacker, long deadlineMs, String title, String lore,
                             String body, String pergunta, String opcoes) {}

    default void updateHackTimer(int secondsLeft) {}

    default void endHackUi() {}

    default void notifyPlayerDamaged(int hp, int maxHp) {}
}
