package no.kristiania.pgr200.database;


import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.*;

import javax.sql.DataSource;
import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UriBuilderTest {

    private static HttpEchoServer server;


    @Before
    public void startServer() throws IOException {
        ConferenceDatabaseProgram.useH2 = true;
        HttpEchoServer.TEST_ENVIRONMENT = true;
        Flyway flyway = new Flyway();
        flyway.setDataSource(ConferenceDatabaseProgram.createH2DataSource());
        flyway.migrate();
        server = new HttpEchoServer(0);
        populateDatabase();
    }

    public static void populateDatabase() throws IOException {
        HttpRequest insert = new HttpRequest("localhost", server.getPort(), "/insert/talk", "POST",
                "title=hacks" + "&description=hacking" + "&topic=hacking");
    }

    @AfterClass public static void logout() throws IOException {
       ConferenceDatabaseProgram client = new ConferenceDatabaseProgram();
        client.resetDatabase();
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForInsertingTalk() throws IOException {
        String[] testSet = {"Insert", "talk", "hacks", "hacking"};
        //The UriBuilder doing a request
        HttpRequest request = new UriBuilder(server.getPort(), testSet).insert();

        //What the UriBuilder-request should look like
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/insert/talk", "POST",
                "title=hacks" + "&description=hacking");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForInsertingTalkWithTopic() throws IOException {
        String[] testSet = {"insert", "topic", "hacking"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).insert();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/insert/topic", "POST",
                "topic=hacking");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestInsertingForTopic() throws IOException {
        String[] testSet = {"Insert","topic", "hacking"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).insert();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/insert/topic", "POST",
                "topic=hacking");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForListingTalks() throws IOException {
        String[] testSet = {"list", "talks"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).list();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/list/talks", "GET");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForListingTopics() throws IOException {
        String[] testSet = {"list", "topics"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).list();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/list/topics", "GET");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForListingTalksWithGivenTopic() throws IOException {
        String[] testSet = {"list", "talks", "with", "hacking"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).list();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/list/talks/with/hacking", "GET");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForRetrievingTalk() throws IOException {
        String[] testSet = {"retrieve", "talk", "1"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).retrieve();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/retrieve/talk/1", "GET");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForRetrievingTopic() throws IOException {
        String[] testSet = {"retrieve", "topic", "1"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).retrieve();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/retrieve/topic/1", "GET");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForDeletingTalk() throws IOException {
        String[] testSet = {"delete", "talk", "1"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).delete();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/delete/talk/1", "DELETE");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForDeletingTopic() throws IOException {
        String[] testSet = {"delete", "topic", "1"};
        HttpRequest request = new UriBuilder(server.getPort(), testSet).delete();
        HttpRequest compareRequest = new HttpRequest("localhost", server.getPort(), "/delete/topic/1", "DELETE");
        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }
}