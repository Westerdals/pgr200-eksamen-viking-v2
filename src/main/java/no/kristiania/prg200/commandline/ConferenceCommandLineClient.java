package no.kristiania.prg200.commandline;

public class ConferenceCommandLineClient {

    public ConferenceClientCommand decodeCommand(String[] strings) {
        AddTalkCommand command = new AddTalkCommand();

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals("-title")) {
                command.withTitle(strings[i+1]);
            } else if (strings[i].equals("-description")) {
                command.withDescription(strings[i+1]);
            }
        }

        return command;
    }

}
