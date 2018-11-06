package no.kristiania.pgr200.database;

import java.io.IOException;
import java.util.Scanner;

public class UriBuilder {
    private String[] arguments;
    private String methodArgument;
    private String objectArgument;
    private String titleArgument;
    private String descriptionArgument;
    private String topicArgument;
    Scanner input = new Scanner(System.in);


    public UriBuilder(String[] arguments) throws IOException {
        this.arguments = arguments;

        for(int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].toLowerCase();
        }

        for(int i = 0; i < arguments.length; i++) {
            if(i == 0) { methodArgument = arguments[i]; }
            else if(i == 1){ objectArgument = arguments[i]; }
            else if(i == 2) { titleArgument = arguments[i]; }
            else if(i == 3) { descriptionArgument = arguments[i]; }
            else if(i == 4) { topicArgument = arguments[i]; }
        }

        //list();
        //retrieve(Integer.parseInt(titleArgument));
        reset();
    }


    private void run() {
        boolean run = true;
        while(run) {
            System.out.println(" -     Hello and welcome to to this program    -");
            System.out.println(" - Start by typing the name of your conference -");

            String conferenceName = input.nextLine();

            if(conferenceName != null) {
                createConference(conferenceName);
            } else {

            }
        }

    }

    private void createConference(String conferenceName) {
    }

    private HttpRequest retrieve(int id) throws IOException {
        if(arguments.length == 2 && objectArgument.equals("talks")) {
            return new HttpRequest("localhost", 8080, "/retrieve/talk/" + id, "GET");
        } else if(arguments.length == 3 && titleArgument != null) {
            return new HttpRequest("localhost", 8080, "/retrieve/talk/" + id, "GET");
        } else {
            System.out.println("you Broke the machine");
        }
        return null;
        //TODO: retrieve topic
    }

    private HttpRequest reset() throws IOException {
        return new HttpRequest("localhost", 8080, "/reset/", "PUT");
    }

    private void add() {

    }

    private HttpRequest list() throws IOException {
        if(arguments.length == 2 && objectArgument.equals("talks")) {
            return new HttpRequest("localhost", 8080, "/list/talks", "GET");
        } else if(arguments.length == 3 && titleArgument != null) {
            return new HttpRequest("localhost", 8080, "/list/talks/" + titleArgument, "GET");
        } else {
            System.out.println("you Broke the machine");
        }
        return null;
    }
}