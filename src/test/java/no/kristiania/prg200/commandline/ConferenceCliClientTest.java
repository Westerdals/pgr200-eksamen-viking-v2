package no.kristiania.prg200.commandline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;


public class ConferenceCliClientTest {

    private ConferenceCliClient client = new ConferenceCliClient();

    @Test
    public void shouldDecodeAddCommand() {
        String title = SampleData.sampleText(5);
        String description = SampleData.sampleText(10);
        ConferenceClientCommand command = client.decodeCommand(new String[] { "add", "-title", title, "-description", description });
        assertThat(command).isInstanceOf(AddTalkCommand.class)
            .isEqualToComparingFieldByField(new AddTalkCommand().withTitle(title).withDescription(description));
    }

}
