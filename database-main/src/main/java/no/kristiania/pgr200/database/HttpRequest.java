package no.kristiania.pgr200.database;

import java.io.IOException;
import java.net.Socket;

public class HttpRequest {

    private String hostname;
    private String uri;
    String requestMethod;
    private int port;
    private String body;

    public HttpRequest(String hostname, int port, String uri, String requestMethod) throws IOException {
        this(hostname, port, uri, requestMethod, null);
    }

    public HttpRequest(String hostname, int port, String uri, String requestMethod, String body) throws IOException {
        this.hostname = hostname;
        this.uri = uri;
        this.port = port;
        this.requestMethod = requestMethod;
        this.body = body;
        execute();
    }

    public HttpResponse execute() throws IOException {
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
                        .write(("Content-Length: " + body.trim().getBytes().length  +"\r\n").getBytes());
            }
            socket.getOutputStream().write("\r\n".getBytes());
            if(body != null && !body.trim().isEmpty()) {
                socket.getOutputStream().write(body.trim().getBytes());
            }

            return new HttpResponse(socket);
        }
    }

}