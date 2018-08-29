package no.kristiania.prg200.commandline;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SampleData {

    private static Random random = new Random();

    public static String sampleText(int wordCount) {
        return IntStream.range(0, wordCount).mapToObj(n -> randomWord()).collect(Collectors.joining(" "));
    }

    private static String randomWord() {
        String[] alternatives = { "hello", "world", "it's", "a", "beautiful", "morning", "and", "I'd", "like", "some", "coffee" };
        return pickOne(alternatives);
    }

    public static String pickOne(String[] alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }

    public static String sampleTopic() {
        return pickOne(new String[] { "testing", "java", "sockets", "jdbc", "database" });
    }

}
