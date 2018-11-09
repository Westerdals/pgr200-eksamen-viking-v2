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
import java.util.stream.Stream;

public class HttpEchoServer {

    private ServerSocket serverSocket;
    private ArgumentReader argumentReader;
    private int statusCode;
    private HttpRequest request;

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
                System.out.println(uri);
                Map<String, String> headers = new HashMap<>();
                String line;
                while (!(line = this.readLine(socket)).trim().isEmpty()) {
                    String[] parts = line.split(":", 2);
                    headers.put(parts[0].trim().toLowerCase(), parts[1].trim().toLowerCase());
                }


                String body = readBody(headers, socket);


                argumentReader = new ArgumentReader(getArguments(uri, body));

                // Writes the response
                socket.getOutputStream().write(("HTTP/1.1 " + setStatusCode() + " OK\r\n").getBytes());
                socket.getOutputStream().write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
                socket.getOutputStream().write("Server: Kristiania Java Server!!\r\n".getBytes());
                socket.getOutputStream().write(("Content-Length: " + setBody().length() + "\r\n").getBytes());
                socket.getOutputStream().write("\r\n".getBytes());
                socket.getOutputStream().write((setBody() + "\r\n").getBytes());
                System.out.println(setBody());
                socket.getOutputStream().flush();

            } catch (IOException | SQLException e) {
                //TODO: Do something here?
                e.printStackTrace();
                System.out.println("Something went wrong with the server");
            }
        }
    }

    public String setBody() {
        String body = argumentReader.getBody();
        return body;
    }

    public int setStatusCode() {
        int statusCode = argumentReader.getStatusCode();
        return statusCode;
    }

    //TODO: LØRDAG: FINNE UT HVORFOR LIST TALKS SISTE TALK IKKE LISTES FULLSTENDIG

    /*
    public void setBodyAndStatusCode(String uri, String body) throws IOException, SQLException {
        //TODO: Needs to work with Topic aswell
        if (uri.contains("insert")) {
            HttpQuery query = new HttpQuery(body);
            body = query.getParameter("title") + " " + query.getParameter("description") + " " + query.getParameter("topic");
            ArgumentReader reader = new ArgumentReader(getArguments(uri, body));
            this.statusCode = reader.getStatusCode();
            body = reader.getBody();
        } else {
            ArgumentReader argumentReader = new ArgumentReader(getUriArguments(uri));
            this.statusCode = argumentReader.getStatusCode();
            body = argumentReader.getBody();
        }
    }
    */

    public int getStatusCode() {
        this.statusCode = 0;
        return statusCode;
    }

    //TODO: Refactor setting the body

    public String readBody(Map<String, String> headers, Socket socket) throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();
        int contentLength = Integer.parseInt(headers.getOrDefault("content-length", "0"));
        if (contentLength != 0) {
            int c;
            int counter = 0;
            while ((c = socket.getInputStream().read()) != -1) {
                bodyBuilder.append((char) c);
                if (++counter == contentLength) {
                    break;
                }
            }
        }
        String body = bodyBuilder.toString();
        HttpQuery query = new HttpQuery(body);
        body = query.getParameter("title") + " " + query.getParameter("description") + " " + query.getParameter("topic");
        System.out.println(body);
        return body;
    }


    public String[] getArguments(String uri, String body) {
        String[] arguments = Stream.concat(Arrays.stream(getUriArguments(uri)), Arrays.stream(getBodyArguments(body)))
                .filter(Arrays -> !"null".equals(Arrays))
                .toArray(String[]::new);
        Arrays.stream(arguments).forEach(System.out::println);
        return arguments;
    }

    public String[] getBodyArguments(String body) {
        String[] bodyArguments = body.split(" ");
        return bodyArguments;
    }

    // /echo?status=200&list=talks
    public String[] getUriArguments(String uri) {
        String[] argumentsUri = uri.split("/");
        String[] uriArguments = new String[argumentsUri.length - 1];
        for(int i = 1; i < argumentsUri.length; i++) {
            uriArguments[i - 1] = argumentsUri[i];
        }
        return uriArguments;
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


    public String readLine(Socket socket) throws IOException {
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

    public int getPort() {
        return serverSocket.getLocalPort();

    }

}