package no.kristiania.pgr200.database;

import org.flywaydb.core.Flyway;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;


import static org.assertj.core.api.Java6Assertions.assertThat;

public class HttpTest {
    private static HttpEchoServer server;
    private String query;


    @Before
    public void startServer() throws IOException {
        ConferenceDatabaseProgram.useH2 = true;
        Flyway flyway = new Flyway();
        flyway.setDataSource(ConferenceDatabaseProgram.createH2DataSource());
        flyway.migrate();
        server = new HttpEchoServer(0);
        populateDatabase();
    }

    public static void populateDatabase() throws IOException {
        new HttpRequest("localhost", server.getPort(), "/insert/talk", "POST",
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
        assertThat(response.getHeader()).isEqualTo("45");
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