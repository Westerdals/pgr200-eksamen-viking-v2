package no.kristiania.prg200.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpEchoServer {

    private ServerSocket serverSocket;

    public HttpEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        Thread thread = new Thread(this::runServer);
        thread.start();
    }

    public void runServer() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                String uri = readLine(socket).split(" ")[1];

                Map<String, String> parameters = readParameters(uri);

                String statusCode = parameters.get("status");
                if (statusCode == null) {
                    statusCode = "200";
                }
                String body = parameters.get("body");
                if (body == null) {
                    body = "Hello world";
                }
                String location = parameters.get("Location");

                // Writes the response
                socket.getOutputStream().write(("HTTP/1.1 " + statusCode + " OK\r\n").getBytes());
                socket.getOutputStream().write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
                if (location != null) {
                    socket.getOutputStream().write(("Location: " + location + "\r\n").getBytes());
                }
                socket.getOutputStream().write("Server: Kristiania Java Server!!\r\n".getBytes());
                socket.getOutputStream().write(("Content-Length: " + body.length() + "\r\n").getBytes());
                socket.getOutputStream().write("\r\n".getBytes());
                socket.getOutputStream().write((body + "\r\n").getBytes());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> readParameters(String uri) throws UnsupportedEncodingException {
        int questionPos = uri.indexOf('?');
        String query = uri.substring(questionPos+1);

        Map<String, String> parameters = new HashMap<>();
        for (String parameter : query.split("&")) {
            int equalsPos = parameter.indexOf('=');
            String paramName = URLDecoder.decode(parameter.substring(0, equalsPos), "UTF-8");
            String paramValue = URLDecoder.decode(parameter.substring(equalsPos+1), "UTF-8");

            parameters.put(paramName, paramValue);

        }
        return parameters;
    }

    public String readLine(Socket socket) throws IOException {
        StringBuilder requestLine = new StringBuilder();

        // Reads the first line
        int c;
        while ((c = socket.getInputStream().read()) != -1) {
            if (c == '\r') {
                break;
            }
            requestLine.append((char)c);
        }

        return requestLine.toString();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

}
