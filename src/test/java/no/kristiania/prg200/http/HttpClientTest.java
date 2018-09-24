package no.kristiania.prg200.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.junit.Test;

public class HttpClientTest {

    @Test
    public void shouldReadStatusCode() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80, "/echo?status=200");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void shouldReadOtherStatusCodes() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80, "/echo?status=404");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void shouldReadResponseHeaders() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com",
                80, "/echo?status=307&Location=http%3A%2F%2Fwww.google.com");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(307);
        assertThat(response.getHeader("Location")).isEqualTo("http://www.google.com");
    }

    @Test
    public void shouldReadResponseBody() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com",
                80, "/echo?body=Hello+world!");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Hello world!");
    }


    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket("localhost", 10080)) {
            socket.getOutputStream()
                .write("GET /echo?status=307&Location=http%3A%2F%2Fwww.google.com HTTP/1.1\r\n".getBytes());
            socket.getOutputStream()
                .write("Host: urlecho.appspot.com\r\n".getBytes());
            socket.getOutputStream()
                .write("Connection: close\r\n".getBytes());
            socket.getOutputStream().write("\r\n".getBytes());



        }
    }


}


