package no.kristiania.pgr200.database;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpEchoServer {

    private ServerSocket serverSocket;
    private ArgumentReader argumentReader;
    private String requestMethod;

    public static void main(String[] args) throws IOException {
        HttpEchoServer server = new HttpEchoServer(8080);
    }

    public HttpEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        Thread thread = new Thread(() -> runServer(serverSocket));
        thread.start();
    }

    private void runServer(ServerSocket serverSocket) {
        System.out.println("Server now running");
        while (true) {

            try {
                Socket socket = serverSocket.accept();
                String[] requestLine = readLine(socket).split(" ");
                this.requestMethod = requestLine[0];
                String uri = requestLine[1];

                Map<String, String> headers = new HashMap<>();
                String line;
                while (!(line = this.readLine(socket)).trim().isEmpty()) {
                    String[] parts = line.split(":", 2);
                    headers.put(parts[0].trim().toLowerCase(), parts[1].trim().toLowerCase());
                }

                String body = readBody(headers, socket);

                if(requestMethod.equals("GET") || requestMethod.equals("DELETE")) {
                    argumentReader = new ArgumentReader(getUriArguments(uri));
                } else if(requestMethod.equals("POST") || requestMethod.equals("PUT")) {
                    argumentReader = new ArgumentReader(getArguments(uri, body));
                } else {
                    System.out.println("Unsupported request method: " + requestMethod);
                }

                // Writes the response
                socket.getOutputStream().write(("HTTP/1.1 " + setStatusCode() + " OK\r\n").getBytes());
                socket.getOutputStream().write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
                socket.getOutputStream().write(("Content-Length: " + setBody().getBytes(UTF_8).length + "\r\n").getBytes());
                socket.getOutputStream().write("\r\n".getBytes());
                socket.getOutputStream().write(setBody().getBytes(UTF_8));
                socket.getOutputStream().flush();
            } catch (IOException | SQLException e) {
                //TODO: Do something here?
                e.printStackTrace();
                System.out.println("Something went wrong with the server");
            }
        }
    }

    private String setBody() {
        return argumentReader.getBody();
    }

    private int setStatusCode() {
        return argumentReader.getStatusCode();
    }


    private String readBody(Map<String, String> headers, Socket socket) throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();
        int contentLength = Integer.parseInt(headers.getOrDefault("content-length", "0"));
        String body = null;
        if (contentLength != 0) {
            int c;
            int counter = 0;
            while ((c = socket.getInputStream().read()) != -1) {
                bodyBuilder.append((char) c);
                if (++counter == contentLength) {
                    break;
                }
            }
            body = bodyBuilder.toString();
            HttpQuery query = new HttpQuery(body);
            switch (this.requestMethod) {
                case "POST":
                    body = query.getParameter("title") + " " + query.getParameter("description") + " " + query.getParameter("topic");
                    break;
                case "PUT":
                    body = query.getParameter("id") + " " + query.getParameter("column") + " " + query.getParameter("value");
                    break;
                default:
                    System.out.println("Invalid body arguments");
                    break;
            }
        }
        return body;
    }


    private String[] getArguments(String uri, String body) {
        return Stream.concat(Arrays.stream(getUriArguments(uri)), Arrays.stream(getBodyArguments(body)))
                .filter(Arrays -> !"null".equals(Arrays))
                .toArray(String[]::new);
    }

    private String[] getBodyArguments(String body) {
        return body.split(" ");
    }

    private String[] getUriArguments(String uri) {
        String[] argumentsUri = uri.split("/");
        String[] uriArguments = new String[argumentsUri.length - 1];
        if (argumentsUri.length - 1 >= 0) System.arraycopy(argumentsUri, 1, uriArguments, 0, argumentsUri.length - 1);
        return uriArguments;
    }


    private String readLine(Socket socket) throws IOException {
        StringBuilder requestLine = new StringBuilder();
        // Reads the first line
        int c;
        while ((c = socket.getInputStream().read()) != -1) {
            if (c == '\r') {
                socket.getInputStream().read();
                break;
            }
            requestLine.append((char)c);
        }
        return requestLine.toString();
    }

    int getPort() {
        return serverSocket.getLocalPort();

    }

}