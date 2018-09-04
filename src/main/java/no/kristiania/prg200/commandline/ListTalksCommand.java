package no.kristiania.prg200.commandline;

public class ListTalksCommand implements ConferenceClientCommand {

    private String topic;

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void readArguments(String[] args) {
        setTopic(getArgument(args, "-topic"));
    }

}
