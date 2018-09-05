package no.kristiania.prg200.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HttpResponse {

    private int statusCode;

    public HttpResponse(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();

        String statusLine = readNextLine(input);
        String[] parts = statusLine.split(" ");

        statusCode = Integer.parseInt(parts[1]);
    }

    private String readNextLine(InputStream input) throws IOException {
        StringBuilder currentLine = new StringBuilder();
        int c;
        while ((c = input.read()) != -1) {
            if (c == '\r') {
                break;
            }
            currentLine.append((char)c);
        }
        return currentLine.toString();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getHeader(String headerName) {
        // TODO Auto-generated method stub
        return null;
    }

}
