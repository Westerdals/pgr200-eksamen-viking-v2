package no.kristiania.prg200.commandline;

public class AddTalkCommand implements ConferenceClientCommand {

    private String title;
    private String description;

    public AddTalkCommand withTitle(String title) {
        this.title = title;
        return this;
    }

    public AddTalkCommand withDescription(String description) {
        this.description = description;
        return this;
    }

}
