package no.kristiania.pgr200.database;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

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
        System.out.println(contentLength);
        StringBuilder body = new StringBuilder();
        int c;
        int read = 0;
        while ((c = input.read()) != -1) {
            body.append((char) c);
            read = read + String.valueOf((char) c).getBytes(UTF_8).length;
            if (read >= contentLength) break;
        }
        int length = body.toString().getBytes(UTF_8).length;
        this.body = body.toString();
        System.out.println(this.body);
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
                input.read();
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
