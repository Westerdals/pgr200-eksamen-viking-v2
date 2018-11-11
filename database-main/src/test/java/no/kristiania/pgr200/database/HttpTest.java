package no.kristiania.pgr200.database;


import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class HttpTest {

    private static DataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.clean();
        flyway.migrate();
        return dataSource;
    }

    String query;
    private static HttpEchoServer server;

    @BeforeClass
    public static void createServer() throws IOException, SQLException {
        server = new HttpEchoServer(0);
        ConferenceTalkDao talkDao = new ConferenceTalkDao(createDataSource());
        ConferenceTalk talk = new ConferenceTalk("talk", "description", "hacking");
        talkDao.insert(talk);
    }

    @Test
    public void shouldExecuteHttpRequest() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/list/talks", "GET");
        HttpResponse response = request.execute();
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void shouldPostData() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/insert/talk", "POST",
                "title=my&description=description");
        HttpResponse response = request.execute();
        assertThat(response.getBody()).isEqualTo("Successfully inserted my into conference_talk");
    }

    @Test
    public void shouldPutData() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/update/talk", "PUT",
                "id=1&column=title&value=newtitle");
        HttpResponse response = request.execute();
        assertThat(response.getBody()).isEqualTo("The talk you tried to update does not exist");
    }

    @Test
    public void shouldDeleteData() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/delete/talk/1", "DELETE");
        HttpResponse response = request.execute();
        assertThat(response.getBody()).isEqualTo("The talk you tried to delete does not exist");
    }

    @Test
    public void shouldReturnIdenticalString () {
        query = "title=mytalk&description=mydescription&topic=xd";
        HttpQuery queryObject = new HttpQuery(query);

        assertThat(queryObject.toString()).isEqualTo(query);
    }

    @Test
    public void shouldReadBody() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/insert/talk", "POST",
                "title=my&description=description");
        HttpResponse response = request.execute();
        assertThat(response.getBody()).isEqualTo("Successfully inserted my into conference_talk");
    }

    @Test
    public void shouldReadHeaders() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/insert/talk", "POST",
                "title=my&description=description");
        HttpResponse response = request.execute();
        assertThat(response.getHeader("Content-Length")).isEqualTo("45");
    }

    @Test
    public void shouldGetCorrectParameter() {
        query = "title=mytalk&description=mydescription&topic=xd";
        HttpQuery queryObject = new HttpQuery(query);

        String titleValue = queryObject.getParameter("title");
        String descriptionValue = queryObject.getParameter("description");

        assertThat(titleValue).isEqualTo("mytalk");
        assertThat(descriptionValue).isEqualTo("mydescription");
    }
}