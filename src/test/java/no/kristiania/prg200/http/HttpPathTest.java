package no.kristiania.prg200.http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class HttpPathTest {

    @Test
    public void shouldSeparatePathAndQuery() {
        HttpPath path = new HttpPath("/urlecho?status=200");
        assertThat(path.getPath()).isEqualTo("/urlecho");
        assertThat(path.getQuery().toString()).isEqualTo("status=200");
    }

    @Test
    public void shouldReturnQueryNullWhenNoQuery() {
        HttpPath path = new HttpPath("/myfile");
        assertThat(path.getPath()).isEqualTo("/myfile");
        assertThat(path.getQuery()).isNull();
    }
}