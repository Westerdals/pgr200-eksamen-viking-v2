package no.kristiania.pgr200.database;

import java.io.IOException;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

class HttpRequest {

    private String requestMethod;
    private String hostname;
    private String uri;
    private int port;
    private String body;

    HttpRequest(String hostname, int port, String uri, String requestMethod) throws IOException {
        this(hostname, port, uri, requestMethod, "");
    }

    HttpRequest(String hostname, int port, String uri, String requestMethod, String body) throws IOException {
        this.hostname = hostname;
        this.uri = uri;
        this.port = port;
        this.requestMethod = requestMethod;
        this.body = body;
        execute();
    }

    HttpResponse execute() throws IOException {
        try(Socket socket = new Socket(hostname, port)) {
            socket.getOutputStream()
                    .write((requestMethod + " " + uri + " HTTP/1.1\r\n").getBytes());
            socket.getOutputStream()
                    .write(("Host: " + hostname + "\r\n").getBytes());
            socket.getOutputStream()
                    .write("Connection: close\r\n".getBytes());
            if(body != null && !body.trim().isEmpty()) {
                socket.getOutputStream()
                        .write("Content-type: application/x-www-form-urlencoded\r\n".getBytes());
                socket.getOutputStream()
                        .write(("Content-Length: " + body.trim().getBytes(UTF_8).length  +"\r\n").getBytes());
            }
            socket.getOutputStream().write("\r\n".getBytes());
            if(body != null && !body.trim().isEmpty()) {
                socket.getOutputStream().write(body.trim().getBytes(UTF_8));
            }
            socket.getOutputStream().flush();

            return new HttpResponse(socket);
        }

    }

}