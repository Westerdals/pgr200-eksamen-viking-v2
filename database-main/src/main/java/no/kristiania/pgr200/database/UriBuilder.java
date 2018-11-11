package no.kristiania.pgr200.database;
import java.io.IOException;
import java.util.Arrays;

class UriBuilder {
    private int port;
    private String[] arguments;
    private String hostname;
    private String methodArgument;
    private String objectArgument;
    private String titleArgument;
    private String descriptionArgument;
    private String topicArgument;


    UriBuilder(int port, String[] arguments) throws IOException {
        this.arguments = Arrays.stream(arguments).map(s -> s.replace(" ", "+").toLowerCase()).toArray(String[]::new);
        this.hostname = "localhost";
        this.port = port;

        commandRedirect();
    }

    UriBuilder(String[] arguments) throws IOException {
        this(8080, arguments);
    }

    private void commandRedirect() throws IOException {
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
            case "update":
                update();
                break;
            default:
                System.out.println("Invalid input");
        }
    }


    HttpRequest retrieve() throws IOException {
        if(arguments.length == 3 && objectArgument.equals("talk") && titleArgument != null) {
            return new HttpRequest(hostname, port, "/retrieve/talk/" + titleArgument, "GET");
        } else if(arguments.length == 3 && objectArgument.equals("topic") && titleArgument != null) {
            return new HttpRequest(hostname, port, "/retrieve/topic/" + titleArgument, "GET");
        } System.out.println("Not valid input");
        return null;
    }

    HttpRequest update() throws IOException {
        if(arguments.length == 5 && objectArgument.equals("talk") && titleArgument != null && descriptionArgument != null && topicArgument != null) {
            return new HttpRequest(hostname, port, "/update/talk", "PUT",
                     "id=" + titleArgument + "&column=" + descriptionArgument + "&value=" + topicArgument);
        }
        System.out.println("Not valid input");
        return null;
    }

    HttpRequest delete() throws IOException {
        if(arguments.length == 3 && objectArgument.equals("talk") && titleArgument != null) {
            return new HttpRequest(hostname, port, "/delete/talk/" + titleArgument, "DELETE");
        } else if(arguments.length == 3 && objectArgument.equals("topic") && titleArgument != null) {
            return new HttpRequest(hostname, port, "/delete/topic/" + titleArgument, "DELETE");
        } System.out.println("Not valid input");
        return null;
    }

    HttpRequest insert() throws IOException {
        if(objectArgument.equals("talk") && titleArgument != null && descriptionArgument != null && topicArgument == null) {
            return new HttpRequest(hostname, port, "/insert/talk", "POST",
                    "title=" + titleArgument + "&description=" + descriptionArgument);
        } else if (objectArgument.equals("talk") && titleArgument != null && descriptionArgument != null && topicArgument != null) {
            return new HttpRequest(hostname, port, "/insert/talk", "POST",
                    "title=" + titleArgument + "&description=" + descriptionArgument + "&topic=" + topicArgument);
        } else if (arguments.length == 3 && objectArgument.equals("topic") && titleArgument != null) {
            return new HttpRequest(hostname, port, "/insert/topic", "POST",
                    "topic=" + titleArgument);
        } System.out.println("Not valid input");
        return null;
    }

    HttpRequest list() throws IOException {
        if(arguments.length == 2 && objectArgument.equals("talks")) {
            return new HttpRequest(hostname, port, "/list/talks", "GET");
        } else if(arguments.length == 2 && objectArgument.equals("topics")) {
            return new HttpRequest(hostname, port, "/list/topics", "GET");
        } else if(arguments.length == 4 && objectArgument.equals("talks") && titleArgument.equals("with") && descriptionArgument != null) {
            return new HttpRequest(hostname, port, "/list/talks/with/" + descriptionArgument, "GET");
        } System.out.println("Invalid input");
        return null;
    }
}