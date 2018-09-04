package no.kristiania.prg200.commandline;

public class ConferenceCommandLineClient {

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

}
