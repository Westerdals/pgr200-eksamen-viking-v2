package no.kristiania.pgr200.database;


import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class HttpTest {
    private static HttpEchoServer server;
    private String query;


    @BeforeClass
    public static void startServer() throws IOException {
        server = new HttpEchoServer(0);
        populateDatabase();

    }

    public static void populateDatabase() throws IOException {
        HttpRequest insert = new HttpRequest("localhost", server.getPort(), "/insert/talk", "POST",
                "title=hacks" + "&description=hacking" + "&topic=hacking");
    }

    @AfterClass
    public static void logout() throws IOException {
        ConferenceDatabaseProgram client = new ConferenceDatabaseProgram();
        client.resetDatabase();
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

    @Ignore
    @Test
    public void shouldPutData() throws IOException {
        HttpRequest request = new HttpRequest("localhost", server.getPort(), "/update/talk", "PUT",
                "id=1&column=title&value=hacks");
        HttpResponse response = request.execute();
        assertThat(response.getBody()).isEqualTo("Successfully updated conference talk 1 with hacks in title");
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