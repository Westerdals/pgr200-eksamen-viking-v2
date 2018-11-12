package no.kristiania.pgr200.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Properties;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGPoolingDataSource;

public class ConferenceDatabaseProgram {

    static boolean useH2 = false;

    ConferenceDatabaseProgram() throws IOException {
        DataSource dataSource = createDataSource();
        ConferenceTalkDao talkDao = new ConferenceTalkDao(dataSource);
        ConferenceTopicDao topicDao = new ConferenceTopicDao(dataSource);

    }

    static DataSource createDataSource() throws IOException {
        if(useH2){
            return createH2DataSource();
        }
        Properties props = new Properties();

        try( FileInputStream in = new FileInputStream("innlevering.properties")) {
            props.load(in);
        }

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    static DataSource createH2DataSource(){
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
        return dataSource;
    }


    //TODO: Tell user to start server + Handle crash if server is not started
    public static void main(String[] args) throws IOException {
        new ConferenceDatabaseProgram().run(args);
    }

    private void run(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Application needs commands to run \n" +
                    "See README.md for full list of commands");
            System.exit(1);
        }

        if(args[0].equals("reset")) {
            resetDatabase();
            return;
        }
        try {
            new UriBuilder(args);
        } catch (ConnectException e) {
            System.out.println("Start the server before running the client");
        }


    }

    void resetDatabase() throws IOException {
            ArgumentReader reader = new ArgumentReader();
            reader.reset();
            System.out.println("Database has been reset");
    }
}
