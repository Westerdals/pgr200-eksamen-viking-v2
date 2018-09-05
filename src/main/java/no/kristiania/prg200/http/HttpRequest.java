package no.kristiania.prg200.http;

import java.io.IOException;
import java.net.Socket;

public class HttpRequest {

    private String hostname;
    private String uri;

    public HttpRequest(String hostname, String uri) {
        this.hostname = hostname;
        this.uri = uri;
    }

    public HttpResponse execute() throws IOException {
        try(Socket socket = new Socket(hostname, 80)) {
            socket.getOutputStream()
                .write(("GET " + uri + " HTTP/1.1\r\n").getBytes());
            socket.getOutputStream()
                .write(("Host: " + hostname + "\r\n").getBytes());
            socket.getOutputStream()
                .write("Connection: close\r\n".getBytes());
            socket.getOutputStream().write("\r\n".getBytes());


            return new HttpResponse(socket);
        }
    }

}
