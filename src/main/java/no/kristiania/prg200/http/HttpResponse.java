package no.kristiania.prg200.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int statusCode;

    private Map<String, String> headers = new HashMap<>();

    private InputStream input;

    private String body;

    public String getBody() {
        return body;
    }

    public HttpResponse(Socket socket) throws IOException {
        input = socket.getInputStream();

        readStatusLine();
        readHeaderLines();
        readBody();
    }

    private void readBody() throws IOException {
        int contentLength = getContentLength();
        StringBuilder body = new StringBuilder();
        for (int i=0; i < contentLength; i++) {
            int c = input.read();
            body.append((char)c);
        }
        this.body = body.toString();
    }

    private int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    private void readHeaderLines() throws IOException {
        String headerLine;
        while ((headerLine = readNextLine()) != null) {
            if (headerLine.isEmpty()) break;

            int colonPos = headerLine.indexOf(':');
            String headerName = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos+1).trim();

            headers.put(headerName, headerValue);
        }
    }

    private void readStatusLine() throws IOException {
        String statusLine = readNextLine();
        String[] parts = statusLine.split(" ");
        statusCode = Integer.parseInt(parts[1]);
    }

    private String readNextLine() throws IOException {
        StringBuilder currentLine = new StringBuilder();
        int c;
        while ((c = input.read()) != -1) {
            if (c == '\r') {
                input.mark(1);
                c = input.read();
                if (c != '\n') {
                    input.reset();
                }
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
        return headers.get(headerName);
    }

}