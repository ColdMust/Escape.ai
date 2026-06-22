package server;

import game.Game;
import io.RemoteGameIO;

import java.util.UUID;

public class GameSession {

    private final String id;
    private final RemoteGameIO io;
    private final Game game;
    private final Thread thread;
    private volatile boolean finished;

    public GameSession() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.io = new RemoteGameIO();
        this.game = new Game(io);
        this.thread = new Thread(() -> {
            try {
                game.iniciar();
            } finally {
                finished = true;
                io.close();
            }
        }, "game-" + id);
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public String getId() { return id; }

    public RemoteGameIO getIo() { return io; }

    public Game getGame() { return game; }

    public boolean isFinished() {
        return finished || io.isClosed();
    }
}
