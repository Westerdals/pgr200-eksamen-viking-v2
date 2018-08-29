package no.kristiania.prg200.commandline;

public class ListTalksCommand implements ConferenceClientCommand {

    private String topic;

    public ListTalksCommand withTopic(String topic) {
        this.topic = topic;
        return this;
    }

}
