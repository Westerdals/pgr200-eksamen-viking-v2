package no.kristiania.prg200.commandline;

import java.io.IOException;
import java.sql.SQLException;

public class ConferenceCommandLineClient {

    private UriBuilder argumentReader;

    public ConferenceClientCommand decodeCommand(String[] args) {
        ConferenceClientCommand command;
        if (args[0].equals("list")) {
            command = new ListTalksCommand();
        } else {
            command = new AddTalkCommand();
        }


        command.readArguments(args);
        return command;
    }
    public static void main(String[] args) throws SQLException, IOException {
        new ConferenceCommandLineClient().run(args);
    }

    private void run(String[] args) {
        if(args.length == 0) {
            System.out.println("Welcome to the shit show");
        }

        argumentReader = new UriBuilder(args);
    }


}
