package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor HTTP que expõe a lógica Java via API JSON
 * e serve os arquivos estáticos do frontend (web/).
 */
public class GameServer {

    private static final int PORT = 9090;
    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();
    private final Path webRoot;

    public GameServer(Path webRoot) {
        this.webRoot = webRoot;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/api/session", new SessionHandler());
        server.createContext("/api/session/", new SessionDetailHandler());
        server.createContext("/", new StaticHandler());

        server.setExecutor(null);
        server.start();

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║   SIMULACRO.EXE — Servidor Web + Java    ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  Lógica: Java  |  Visual: navegador (HTML/JS)");
        System.out.println("  Abra: http://localhost:" + PORT);
        System.out.println();
    }

    // ── API: POST /api/session ────────────────────────────────────

    private class SessionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            addCors(ex);
            if ("OPTIONS".equals(ex.getRequestMethod())) {
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equals(ex.getRequestMethod())) {
                sendJson(ex, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }
            GameSession session = new GameSession();
            sessions.put(session.getId(), session);
            sendJson(ex, 201, "{\"sessionId\":\"" + session.getId() + "\"}");
        }
    }

    // ── API: GET/POST /api/session/{id} ───────────────────────────

    private class SessionDetailHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            addCors(ex);
            if ("OPTIONS".equals(ex.getRequestMethod())) {
                ex.sendResponseHeaders(204, -1);
                return;
            }

            String path = ex.getRequestURI().getPath();
            String[] parts = path.split("/");
            if (parts.length < 4) {
                sendJson(ex, 404, "{\"error\":\"Not found\"}");
                return;
            }
            String sessionId = parts[3];
            GameSession session = sessions.get(sessionId);
            if (session == null) {
                sendJson(ex, 404, "{\"error\":\"Session not found\"}");
                return;
            }

            if ("GET".equals(ex.getRequestMethod())) {
                handlePoll(ex, session);
            } else if ("POST".equals(ex.getRequestMethod())) {
                handleInput(ex, session);
            } else {
                sendJson(ex, 405, "{\"error\":\"Method not allowed\"}");
            }
        }

        private void handlePoll(HttpExchange ex, GameSession session) throws IOException {
            var io = session.getIo();
            var messages = io.drainMessages();
            String hud = session.getGame().getHudText();
            boolean waiting = io.isWaitingForInput();
            boolean finished = session.isFinished();
            String uiJson = io.getUiState().toJson();

            StringBuilder json = new StringBuilder("{");
            json.append("\"messages\":[");
            for (int i = 0; i < messages.size(); i++) {
                if (i > 0) json.append(',');
                json.append('"').append(escapeJson(messages.get(i))).append('"');
            }
            json.append("],\"waitingForInput\":").append(waiting);
            json.append(",\"hud\":\"").append(escapeJson(hud)).append('"');
            json.append(",\"finished\":").append(finished);
            json.append(",\"ui\":").append(uiJson);
            json.append('}');

            sendJson(ex, 200, json.toString());
        }

        private void handleInput(HttpExchange ex, GameSession session) throws IOException {
            String body = readBody(ex);
            String value = extractJsonValue(body, "value");
            session.getIo().submitInput(value);
            sendJson(ex, 200, "{\"ok\":true}");
        }
    }

    // ── Arquivos estáticos (web/) ─────────────────────────────────

    private class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            String reqPath = ex.getRequestURI().getPath();
            if (reqPath.equals("/")) reqPath = "/index.html";

            Path file = webRoot.resolve(reqPath.substring(1)).normalize();
            if (!file.startsWith(webRoot) || !Files.exists(file) || Files.isDirectory(file)) {
                ex.sendResponseHeaders(404, -1);
                ex.close();
                return;
            }

            String contentType = guessContentType(file.toString());
            byte[] bytes = Files.readAllBytes(file);
            ex.getResponseHeaders().set("Content-Type", contentType);
            ex.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // ── Utilitários ───────────────────────────────────────────────

    private static void addCors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private static void sendJson(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static String extractJsonValue(String json, String key) {
        if (json == null) return "";
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start < 0) return "";
        start += search.length();
        int end = json.indexOf('"', start);
        return end < 0 ? "" : json.substring(start, end);
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=utf-8";
        if (path.endsWith(".css"))  return "text/css; charset=utf-8";
        if (path.endsWith(".js"))   return "application/javascript; charset=utf-8";
        if (path.endsWith(".png"))  return "image/png";
        if (path.endsWith(".svg"))  return "image/svg+xml";
        return "application/octet-stream";
    }
}
