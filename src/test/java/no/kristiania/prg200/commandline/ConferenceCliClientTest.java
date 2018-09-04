package no.kristiania.prg200.commandline;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class ConferenceCliClientTest {

    private ConferenceCommandLineClient client = new ConferenceCommandLineClient();

    @Test
    public void shouldDecodeAddCommand() {
        String title = SampleData.sampleText(5);
        String description = SampleData.sampleText(10);
        ConferenceClientCommand command = client.decodeCommand(new String[] {
                "add",
                "-title", title,
                "-description", description
        });
        AddTalkCommand expectedCommand = new AddTalkCommand();
        expectedCommand.setTitle(title);
        expectedCommand.setDescription(description);
        assertThat(command).isInstanceOf(AddTalkCommand.class)
            .isEqualToComparingFieldByField(expectedCommand);
    }

    @Test
    public void shouldDecodeAddCommandWithTopic() {
        String title = SampleData.sampleText(5);
        String description = SampleData.sampleText(10);
        String topic = SampleData.sampleTopic();
        ConferenceClientCommand command = client.decodeCommand(new String[] {
                "add",
                "-title", title,
                "-topic", topic,
                "-description", description
        });
        AddTalkCommand expectedCommand = new AddTalkCommand();
        expectedCommand.setTitle(title);
        expectedCommand.setTopic(topic);
        expectedCommand.setDescription(description);
        assertThat(command).isInstanceOf(AddTalkCommand.class)
            .isEqualToComparingFieldByField(expectedCommand);
    }

    @Test
    public void shouldDecodeListCommandWithTopic() {
        String title = SampleData.sampleText(5);
        String description = SampleData.sampleText(10);
        String topic = SampleData.sampleTopic();
        ConferenceClientCommand command = client.decodeCommand(new String[] {
                "list",
                "-topic", topic,
        });
        ListTalksCommand expectedCommand = new ListTalksCommand();
        expectedCommand.setTopic(topic);
        assertThat(command).isInstanceOf(ListTalksCommand.class)
            .isEqualToComparingFieldByField(expectedCommand);
    }
}
