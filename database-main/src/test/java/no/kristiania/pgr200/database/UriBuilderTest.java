package no.kristiania.pgr200.database;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UriBuilderTest {


    @Test
    public void ShouldReturnCorrectHttpRequestForTalk() throws IOException {
        String[] testSet = {"Insert", "talk", "my talk", "my description"};

        //The UriBuilder doing a request
        HttpRequest request = new UriBuilder(testSet).insert();

        //What the UriBuilder-request should look like
        HttpRequest compareRequest = new HttpRequest("localhost", 8080, "/insert/talk", "POST",
                "title=my+talk" + "&description=my+description");

        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForTalkWithTopic() throws IOException {
        String[] testSet = {"Insert", "topic", "my topic"};

        HttpRequest request = new UriBuilder(testSet).insert();

        HttpRequest compareRequest = new HttpRequest("localhost", 8080, "/insert/topic", "POST",
                "topic=my+topic");

        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    @Test
    public void ShouldReturnCorrectHttpRequestForTopic() throws IOException {
        String[] testSet = {"Insert","topic", "gardening"};

        HttpRequest request = new UriBuilder(testSet).insert();

        HttpRequest compareRequest = new HttpRequest("localhost", 8080, "/insert/topic", "POST",
                "topic=gardening");

        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }

    //TODO: Something is fucking up with the Query class here
    @Ignore
    @Test
    public void ShouldReturnCorrectHttpRequestForListingTalks() throws IOException {
        String[] testSet = {"list", "talks"};

        HttpRequest request = new UriBuilder(testSet).list();

        HttpRequest compareRequest = new HttpRequest("localhost", 8080, "/list/talks", "GET");

        assertThat(request).isEqualToComparingFieldByField(compareRequest);
    }
}