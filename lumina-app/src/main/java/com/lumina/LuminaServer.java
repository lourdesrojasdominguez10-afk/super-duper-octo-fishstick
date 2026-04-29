package com.lumina;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;

/**
 * LUMINA Studio Creativo — Servidor Web Local
 *
 * Sirve el archivo HTML en http://localhost:8080
 * No requiere dependencias externas (usa el servidor HTTP incluido en el JDK).
 *
 * ▶  Para ejecutar en IntelliJ IDEA:
 *    1. Haz clic derecho sobre este archivo → Run 'LuminaServer.main()'
 *    2. Abre tu navegador en:  http://localhost:8080
 *    3. Para detener: haz clic en el botón Stop (■) en IntelliJ
 */
public class LuminaServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Sirve todos los archivos estáticos desde /resources/static/
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null); // usa el executor por defecto
        server.start();

        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║   ✦  LUMINA Studio — Servidor iniciado    ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.printf( "║   URL:  http://localhost:%d               ║%n", PORT);
        System.out.println("║   Presiona Ctrl+C para detener            ║");
        System.out.println("╚═══════════════════════════════════════════╝");

        // Abrir el navegador automáticamente si el SO lo permite
        try {
            String url = "http://localhost:" + PORT;
            String os = System.getProperty("os.name").toLowerCase();
            Runtime rt = Runtime.getRuntime();
            if (os.contains("win")) {
                rt.exec(new String[]{"cmd", "/c", "start", url});
            } else if (os.contains("mac")) {
                rt.exec(new String[]{"open", url});
            } else if (os.contains("nix") || os.contains("nux")) {
                rt.exec(new String[]{"xdg-open", url});
            }
        } catch (Exception ignored) {
            System.out.println("   Abre manualmente: http://localhost:" + PORT);
        }
    }

    static class StaticFileHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();

            // Raíz → index.html
            if (requestPath.equals("/") || requestPath.isEmpty()) {
                requestPath = "/index.html";
            }

            // Buscar el archivo dentro del JAR/classpath (resources/static)
            String resourcePath = "/static" + requestPath;
            InputStream is = LuminaServer.class.getResourceAsStream(resourcePath);

            if (is == null) {
                // 404
                byte[] body = ("404 - No encontrado: " + requestPath).getBytes();
                exchange.sendResponseHeaders(404, body.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body);
                }
                return;
            }

            // Determinar Content-Type
            String contentType = getContentType(requestPath);
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Cache-Control", "no-cache");

            byte[] content = is.readAllBytes();
            exchange.sendResponseHeaders(200, content.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(content);
            }
        }

        private String getContentType(String path) {
            if (path.endsWith(".html"))  return "text/html; charset=UTF-8";
            if (path.endsWith(".css"))   return "text/css; charset=UTF-8";
            if (path.endsWith(".js"))    return "application/javascript; charset=UTF-8";
            if (path.endsWith(".json"))  return "application/json";
            if (path.endsWith(".png"))   return "image/png";
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
            if (path.endsWith(".svg"))   return "image/svg+xml";
            if (path.endsWith(".ico"))   return "image/x-icon";
            if (path.endsWith(".woff2")) return "font/woff2";
            if (path.endsWith(".woff"))  return "font/woff";
            return "application/octet-stream";
        }
    }
}
