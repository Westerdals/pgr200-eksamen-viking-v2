package no.kristiania.prg200.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.junit.Test;

public class HttpClientTest {

    @Test
    public void shouldReadStatusCode() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", "/echo?status=200");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void shouldReadOtherStatusCodes() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", "/echo?status=404");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void shouldReadResponseHeaders() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com",
                "/echo?status=307&Location=http%3A%2F%2Fwww.google.com");
        HttpResponse response = request.execute();

        assertThat(response.getStatusCode()).isEqualTo(307);
        assertThat(response.getHeader("Location")).isEqualTo("http://www.google.com");
    }


    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket("172.217.19.212", 80)) {
            socket.getOutputStream()
                .write("GET /echo?status=307&Location=http%3A%2F%2Fwww.google.com HTTP/1.1\r\n".getBytes());
            socket.getOutputStream()
                .write("Host: urlecho.appspot.com\r\n".getBytes());
            socket.getOutputStream()
                .write("Connection: close\r\n".getBytes());
            socket.getOutputStream().write("\r\n".getBytes());

            InputStream input = socket.getInputStream();

            int c;
            while ((c = input.read()) != -1) {
                System.out.print((char)c);
            }

        }
    }


}


