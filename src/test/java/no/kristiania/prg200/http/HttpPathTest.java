package no.kristiania.prg200.http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class HttpPathTest {

    @Test
    public void shouldFindParameters() {
        HttpPath path = new HttpPath("/echo?status=200");
        HttpQuery query = path.getQuery();
        assertThat(query.toString()).isEqualTo("status=200");
        assertThat(query.getParameter("status"))
            .isEqualTo("200");
    }

    @Test
    public void shouldFindMoreParameters() {
        HttpPath path = new HttpPath("/echo?status=404&body=Hello");
        HttpQuery query = path.getQuery();
        assertThat(query.toString()).isEqualTo("status=404&body=Hello");
        assertThat(query.getParameter("status"))
            .isEqualTo("404");
        assertThat(query.getParameter("body"))
            .isEqualTo("Hello");
    }

    @Test
    public void shouldUrlDecodeParameters() {
        String query = "status=307&Location=http%3A%2F%2Fwww.kristiania.no";
        HttpPath path = new HttpPath("/echo?" + query);
        assertThat(path.getQuery().getParameter("Location"))
            .isEqualTo("http://www.kristiania.no");
        assertThat(path.getQuery().toString())
            .isEqualTo(query);
    }

}
