package no.kristiania.pgr200.database;

//import no.kristiania.prg200.http.HttpRequest;

import java.io.IOException;
import java.sql.SQLOutput;
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

        list();
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

    private void retrieve() {
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