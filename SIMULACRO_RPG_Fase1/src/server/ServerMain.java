package server;

import java.nio.file.Path;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        Path webRoot = Path.of("web");
        if (!webRoot.toFile().exists()) {
            webRoot = Path.of("..", "web");
        }
        new GameServer(webRoot.toAbsolutePath().normalize()).start();
    }
}
