package no.kristiania.prg200.commandline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
        AddTalkCommand expectedCommand = new AddTalkCommand().withTitle(title).withDescription(description);
        assertThat(command).isInstanceOf(AddTalkCommand.class)
            .isEqualToComparingFieldByField(expectedCommand);
    }

    @Test
    public void shouldDecodeAddCommandInAnyOrder() {
        String topic = SampleData.sampleTopic();
        String title = SampleData.sampleText(5);
        String description = SampleData.sampleText(10);
        ConferenceClientCommand command = client.decodeCommand(new String[] {
                "add",
                "-topic", topic,
                "-title", title,
                "-description", description
        });
        AddTalkCommand expectedCommand = new AddTalkCommand().withTitle(title).withDescription(description);
        assertThat(command)
            .isInstanceOf(AddTalkCommand.class)
            .isEqualToComparingFieldByField(expectedCommand);
    }

    @Test
    public void shouldDecodeListCommand() {
        String topic = SampleData.sampleTopic();
        ConferenceClientCommand command = client.decodeCommand(new String[] {
                "list", "-topic", topic
        });
        assertThat(command)
            .isInstanceOf(ListTalksCommand.class)
            .isEqualToComparingFieldByField(new ListTalksCommand().withTopic(topic));
    }

}
