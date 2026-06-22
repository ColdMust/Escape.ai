package io;

import java.util.ArrayList;
import java.util.List;

/** Estado visual sincronizado com o cliente web. */
public class UiState {

    public String mode = "normal";

    public String playerName = "";
    public int playerHp;
    public int playerMax;
    public boolean battleActive;

    public final List<EnemyBar> enemies = new ArrayList<>();

    public boolean hackActive;
    public int hackTimeLeft;
    public long hackDeadlineMs;
    public String hackHackerName = "";
    public String hackTitle = "";
    public String hackLore = "";
    public String hackBody = "";
    public String hackPergunta = "";
    public String hackOpcoes = "";

    public static class EnemyBar {
        public String name;
        public int hp;
        public int maxHp;

        public EnemyBar(String name, int hp, int maxHp) {
            this.name = name;
            this.hp = hp;
            this.maxHp = maxHp;
        }
    }

    public void resetBattle() {
        mode = "normal";
        battleActive = false;
        enemies.clear();
        hackActive = false;
    }

    public void startBattle(String name, int hp, int max) {
        mode = "battle";
        battleActive = true;
        playerName = name;
        playerHp = hp;
        playerMax = max;
        hackActive = false;
    }

    public void updateBattle(String name, int hp, int max, List<EnemyBar> enemyBars) {
        playerName = name;
        playerHp = hp;
        playerMax = max;
        enemies.clear();
        enemies.addAll(enemyBars);
    }

    public void startHack(String hacker, long deadlineMs, String title, String lore,
                          String body, String pergunta, String opcoes) {
        mode = "hack";
        hackActive = true;
        hackHackerName = hacker;
        hackDeadlineMs = deadlineMs;
        hackTimeLeft = segundosRestantes(deadlineMs);
        hackTitle = title;
        hackLore = lore;
        hackBody = body;
        hackPergunta = pergunta;
        hackOpcoes = opcoes;
    }

    public void refreshHackTimer() {
        if (hackActive) {
            hackTimeLeft = segundosRestantes(hackDeadlineMs);
        }
    }

    private static int segundosRestantes(long deadlineMs) {
        return Math.max(0, (int) ((deadlineMs - System.currentTimeMillis() + 999) / 1000));
    }

    public void endHack() {
        hackActive = false;
        mode = battleActive ? "battle" : "normal";
    }

    public String toJson() {
        StringBuilder j = new StringBuilder("{");
        j.append("\"mode\":\"").append(esc(mode)).append('"');
        j.append(",\"battleActive\":").append(battleActive);
        j.append(",\"playerName\":\"").append(esc(playerName)).append('"');
        j.append(",\"playerHp\":").append(playerHp);
        j.append(",\"playerMax\":").append(playerMax);

        j.append(",\"enemies\":[");
        for (int i = 0; i < enemies.size(); i++) {
            if (i > 0) j.append(',');
            EnemyBar e = enemies.get(i);
            j.append("{\"name\":\"").append(esc(e.name)).append('"');
            j.append(",\"hp\":").append(e.hp);
            j.append(",\"maxHp\":").append(e.maxHp).append('}');
        }
        j.append(']');

        j.append(",\"hackActive\":").append(hackActive);
        refreshHackTimer();
        j.append(",\"hackTimeLeft\":").append(hackTimeLeft);
        j.append(",\"hackDeadlineMs\":").append(hackDeadlineMs);
        j.append(",\"hackHackerName\":\"").append(esc(hackHackerName)).append('"');
        j.append(",\"hackTitle\":\"").append(esc(hackTitle)).append('"');
        j.append(",\"hackLore\":\"").append(esc(hackLore)).append('"');
        j.append(",\"hackBody\":\"").append(esc(hackBody)).append('"');
        j.append(",\"hackPergunta\":\"").append(esc(hackPergunta)).append('"');
        j.append(",\"hackOpcoes\":\"").append(esc(hackOpcoes)).append('"');
        j.append('}');
        return j.toString();
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "");
    }
}
