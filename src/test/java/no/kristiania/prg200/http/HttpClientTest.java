package no.kristiania.prg200.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.Socket;

import org.jsonbuddy.JsonObject;
import org.jsonbuddy.parse.JsonParser;
import org.junit.Test;

public class HttpClientTest {

    @Test
    public void shouldExecuteRequest() throws Exception {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80, "/echo");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void shouldReadResponseCode() throws Exception {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80,
                "/echo?" + new HttpQuery().add("status", "404"));
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void shouldReadBody() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80,
                "/echo?" + new HttpQuery().add("body", "Hello World"));
        HttpResponse response = request.execute();

        assertThat(response.getBody())
            .isEqualTo("Hello World");
    }

    @Test
    public void shouldReadResponseHeaders() throws IOException {
        HttpQuery query = new HttpQuery()
                .add("status", "307")
                .add("Location", "http://www.google.com");
        // Tips: Her trenger du java.net.URLEncoder
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80,
                "/echo?" + query);

        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(307);
        assertThat(response.getHeader("Location")).isEqualTo("http://www.google.com");
    }

    @Test
    public void shoudPostRequest() throws IOException {
        HttpRequest request = new HttpRequest("httpbin.org", 80,
                "/post", "POST");
        request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

        HttpQuery query = new HttpQuery()
            .add("custname", "Joe Random Hacker")
            .add("custel", "222-555-1245")
            .add("topping", "cheese");
        request.setBody(query.toString());

        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(200);

        JsonObject json = JsonParser.parseToObject(response.getBody());
        assertThat(json.requiredObject("headers").requiredString("Content-Type"))
            .isEqualTo("application/x-www-form-urlencoded");
        assertThat(json.requiredObject("form").requiredString("custname"))
            .isEqualTo("Joe Random Hacker");
    }


    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("urlecho.appspot.com", 80)) {
            socket.getOutputStream().write("GET /echo?status=404 HTTP/1.1\r\n".getBytes());
            socket.getOutputStream().write("Host: urlecho.appspot.com\r\n".getBytes());
            socket.getOutputStream().write("Connection: close\r\n".getBytes());
            socket.getOutputStream().write("\r\n".getBytes());
            socket.getOutputStream().flush();

            int c;
            while ((c = socket.getInputStream().read()) != -1) {
                System.out.print((char)c);
            }
        }
    }

}


