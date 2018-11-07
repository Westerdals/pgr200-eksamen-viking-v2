package no.kristiania.pgr200.database;

//import no.kristiania.prg200.http.HttpRequest;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UriBuilder {
    private String[] arguments;
    private String hostname;
    private String methodArgument;
    private String objectArgument;
    private String titleArgument;
    private String descriptionArgument;
    private String topicArgument;
    Scanner input = new Scanner(System.in);


    public UriBuilder(String[] arguments) throws IOException {
        this.arguments = Arrays.stream(arguments).map(String::toLowerCase).toArray(String[]::new);
        this.hostname = "localhost";

        for(int i = 0; i < arguments.length; i++) {
            if(i == 0) { methodArgument = arguments[i]; }
            else if(i == 1){ objectArgument = arguments[i]; }
            else if(i == 2) { titleArgument = arguments[i]; }
            else if(i == 3) { descriptionArgument = arguments[i]; }
            else if(i == 4) { topicArgument = arguments[i]; }
        }

        switch (methodArgument) {
            case "retrieve":
                retrieve();
                break;
            case "delete":
                delete();
                break;
            case "insert":
                insert();
                break;
            case "list":
                list();
                break;
        }
    }

    // TODO: There has to be a better way to do this
    private HttpRequest retrieve() throws IOException {
        if(arguments.length == 3 && objectArgument.equals("talk") && titleArgument != null) {
            return new HttpRequest(hostname, 8080, "/retrieve/talk/" + titleArgument, "GET");
        } else if(arguments.length == 3 && objectArgument.equals("topic") && titleArgument != null) {
            return new HttpRequest(hostname, 8080, "/retrieve/topic/" + titleArgument, "GET");
        } System.out.println("Invalid input");
        return null;
    }

    private HttpRequest delete() throws IOException {
        if(arguments.length == 3 && objectArgument.equals("talk") && titleArgument != null) {
            return new HttpRequest(hostname, 8080, "talks/" + titleArgument, "DELETE");
        } else if(arguments.length == 3 && objectArgument.equals("topic") && titleArgument != null) {
            return new HttpRequest(hostname, 8080, "topics/" + titleArgument, "DELETE");
        } System.out.println("Invalid input");
        return null;
    }


    //TODO: Make spaces to + signs
    //TODO: proper body format
    private HttpRequest insert() throws IOException {
        if(objectArgument.equals("talk") && titleArgument != null && descriptionArgument != null) {
            return new HttpRequest(hostname, 8080, "/insert/talk", "POST",
                    titleArgument + " " + descriptionArgument);
        } else if (objectArgument.equals("talk") && titleArgument != null && descriptionArgument != null && topicArgument != null) {
            return new HttpRequest(hostname, 8080, "/insert/talk", "POST",
                     titleArgument + " " + descriptionArgument + " " + topicArgument);
        } System.out.println("Invalid input");
        return null;
    }

    // TODO: List talks with topic
    private HttpRequest list() throws IOException {
        if(arguments.length == 2 && objectArgument.equals("talks")) {
            return new HttpRequest(hostname, 8080, "/list/talks", "GET");
        } else if(arguments.length == 2 && objectArgument.equals("topics")) {
            return new HttpRequest(hostname, 8080, "/list/topics", "GET");
        } System.out.println("Invalid input");
        return null;
    }
}