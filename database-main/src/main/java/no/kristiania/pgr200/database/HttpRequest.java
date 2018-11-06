package no.kristiania.pgr200.database;

import java.io.IOException;
import java.net.Socket;

public class HttpRequest {

    private String hostname;
    private String uri;
    String requestMethod;
    private int port;

    public HttpRequest(String hostname, int port, String uri, String requestMethod) throws IOException {
        this.hostname = hostname;
        this.uri = uri;
        this.port = port;
        this.requestMethod = requestMethod;
        execute();
    }

    public HttpResponse execute() throws IOException {
        try(Socket socket = new Socket(hostname, port)) {
            socket.getOutputStream()
                    .write((requestMethod + uri + " HTTP/1.1\r\n").getBytes());
            socket.getOutputStream()
                    .write(("Host: " + hostname + "\r\n").getBytes());
            socket.getOutputStream()
                    .write("Connection: close\r\n".getBytes());
            socket.getOutputStream().write("\r\n".getBytes());
            return new HttpResponse(socket);
        }
    }

}
