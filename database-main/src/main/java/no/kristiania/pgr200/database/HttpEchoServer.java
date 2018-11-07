package no.kristiania.pgr200.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpEchoServer {

    private ServerSocket serverSocket;
    private ArgumentReader argumentReader;
    private int statusCode;
    private HttpRequest request;
    private String body;

    public static void main(String[] args) throws IOException {
        HttpEchoServer server = new HttpEchoServer(8080);
    }

    public HttpEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        Thread thread = new Thread(() -> runServer(serverSocket));
        thread.start();
    }

    public void runServer(ServerSocket serverSocket) {

        while (true) {

            try {
                Socket socket = serverSocket.accept();

                String uri = readLine(socket).split(" ")[1];
                Map<String, String> parameters = readParameters(uri);
                String[] arguments = readArguments(uri);
                System.out.println(uri);

                /* TODO: Better handling for favicon
                if(!uri.equals("/favicon.ico")) {
                    ArgumentReader argumentReader = new ArgumentReader(arguments);
                    this.statusCode = argumentReader.getStatusCode();
                    body = argumentReader.getBody();
                }
                */

                ArgumentReader argumentReader = new ArgumentReader(arguments);
                this.statusCode = argumentReader.getStatusCode();

                body = argumentReader.getBody();
                if(body == null) {
                    body = "Body is null";
                }

                // Writes the response
                socket.getOutputStream().write(("HTTP/1.1 " + this.statusCode + " OK\r\n").getBytes());
                socket.getOutputStream().write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
                socket.getOutputStream().write("Server: Kristiania Java Server!!\r\n".getBytes());
                socket.getOutputStream().write(("Content-Length: " + body.length() + "\r\n").getBytes());
                socket.getOutputStream().write("\r\n".getBytes());
                socket.getOutputStream().write((body + "\r\n").getBytes());
                System.out.println(body);
                socket.getOutputStream().flush();

            } catch (IOException | SQLException e) {
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
            if(equalsPos < 0) {
                break;
            }
            String paramName = URLDecoder.decode(parameter.substring(0, equalsPos), "UTF-8");
            String paramValue = URLDecoder.decode(parameter.substring(equalsPos+1), "UTF-8");
            parameters.put(paramName, paramValue);
        }
        return parameters;
    }

    // /echo?status=200&list=talks
    public String[] readArguments(String uri) {
        String[] argumentsUri = uri.split("/");
        System.out.println(argumentsUri[0]);
        String[] arguments = new String[argumentsUri.length - 1];
        for(int i = 1; i < argumentsUri.length; i++) {
            arguments[i - 1] = argumentsUri[i];
        }
        return arguments;
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