package io;

import java.util.ArrayList;
import java.util.List;

public class RemoteGameIO implements GameIO {

    private final List<String> pendingMessages = new ArrayList<>();
    private final UiState uiState = new UiState();
    private volatile boolean waitingForInput;
    private volatile String pendingInput;
    private volatile boolean closed;
    private final Object lock = new Object();

    @Override
    public void print(String text) {
        synchronized (lock) {
            if (closed) return;
            if (!pendingMessages.isEmpty()) {
                int last = pendingMessages.size() - 1;
                pendingMessages.set(last, pendingMessages.get(last) + text);
            } else {
                pendingMessages.add(text);
            }
            lock.notifyAll();
        }
    }

    @Override
    public void println(String text) {
        synchronized (lock) {
            if (closed) return;
            pendingMessages.add(text);
            lock.notifyAll();
        }
    }

    @Override
    public String readLine() {
        return readLineUntil(Long.MAX_VALUE);
    }

    @Override
    public String readLineUntil(long deadlineMs) {
        synchronized (lock) {
            if (closed) return "";
            waitingForInput = true;
            lock.notifyAll();
            while (pendingInput == null && !closed) {
                if (System.currentTimeMillis() >= deadlineMs) {
                    waitingForInput = false;
                    return null;
                }
                try {
                    long waitMs = Math.min(250, deadlineMs - System.currentTimeMillis());
                    if (waitMs <= 0) {
                        waitingForInput = false;
                        return null;
                    }
                    lock.wait(waitMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    waitingForInput = false;
                    return "";
                }
            }
            waitingForInput = false;
            if (closed) return "";
            String result = pendingInput;
            pendingInput = null;
            return result != null ? result : "";
        }
    }

    public List<String> drainMessages() {
        synchronized (lock) {
            List<String> copy = new ArrayList<>(pendingMessages);
            pendingMessages.clear();
            return copy;
        }
    }

    public UiState getUiState() { return uiState; }

    @Override
    public void startBattleUi(String playerName, int hp, int maxHp) {
        synchronized (lock) {
            uiState.startBattle(playerName, hp, maxHp);
            lock.notifyAll();
        }
    }

    @Override
    public void syncBattleUi(String playerName, int hp, int maxHp,
                             List<UiState.EnemyBar> enemies) {
        synchronized (lock) {
            uiState.updateBattle(playerName, hp, maxHp, enemies);
            lock.notifyAll();
        }
    }

    @Override
    public void endBattleUi() {
        synchronized (lock) {
            uiState.resetBattle();
            lock.notifyAll();
        }
    }

    @Override
    public void startHackUi(String hacker, long deadlineMs, String title, String lore,
                            String body, String pergunta, String opcoes) {
        synchronized (lock) {
            uiState.startHack(hacker, deadlineMs, title, lore, body, pergunta, opcoes);
            lock.notifyAll();
        }
    }

    @Override
    public void updateHackTimer(int secondsLeft) {
        synchronized (lock) {
            uiState.refreshHackTimer();
            lock.notifyAll();
        }
    }

    @Override
    public void endHackUi() {
        synchronized (lock) {
            uiState.endHack();
            lock.notifyAll();
        }
    }

    @Override
    public void notifyPlayerDamaged(int hp, int maxHp) {
        synchronized (lock) {
            uiState.playerHp = hp;
            uiState.playerMax = maxHp;
            lock.notifyAll();
        }
    }

    public boolean isWaitingForInput() {
        return waitingForInput;
    }

    public void submitInput(String input) {
        synchronized (lock) {
            pendingInput = input != null ? input : "";
            waitingForInput = false;
            lock.notifyAll();
        }
    }

    public void close() {
        synchronized (lock) {
            closed = true;
            pendingInput = "";
            lock.notifyAll();
        }
    }

    public boolean isClosed() {
        return closed;
    }
}
