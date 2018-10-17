package no.kristiania.pgr200.database;

import java.sql.*;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.postgresql.core.SqlCommand;
import org.postgresql.ds.PGPoolingDataSource;

public class ConferenceDatabaseProgram {

    private DataSource dataSource;
    private ConferenceTalkDao talkDao;
    private ConferenceTopicDao topicDao;

    public ConferenceDatabaseProgram() throws SQLException {
        this.dataSource = createDataSource();
        this.talkDao = new ConferenceTalkDao(dataSource);
        this.topicDao = new ConferenceTopicDao(dataSource);

    }

    public static DataSource createDataSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost/postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("root");

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

        return dataSource;
    }

    public static void main(String[] args) throws SQLException {
        new ConferenceDatabaseProgram().run(args);
        ConferenceTalkDao talkDao = new ConferenceTalkDao(createDataSource());
        ConferenceTopicDao topicDao = new ConferenceTopicDao(createDataSource());
        talkDao.listTalk();
        topicDao.listTopics();


    }

    private void run(String[] args) throws SQLException {
        if (args.length == 0) {
            System.out.println("Run the class with an argument, on of `insert`, or ...");
            System.exit(1);
        }

        ConferenceTalk testTalk = new ConferenceTalk("My Talk Title", "A description of my Talk");
        ConferenceTopic testTopic = new ConferenceTopic("Science");

        if(args[0].equals("insert")) {
            if (args[1].equals("talk")) {
                insertTalk(testTalk);
            } else if (args[1].equals("topic")) {
                insertTopic(testTopic);
            } else {
                System.err.println("Unknown command!");
            }
        } else if (args[0].equals("view")) {
            if (args[1].equals("single")){
                if (args[2].equals("talk")){
                    retrieveTalk();
                }
            } else if (args[1].equals("talks")) {
                    listTalks();

            }
        }

    }

    private void insertTalk(ConferenceTalk talk) throws SQLException {
        talkDao.insertTalk(talk);
    }

    private void retrieveTalk() throws SQLException {
        talkDao.retrieve(1);
    }

    private void insertTopic(ConferenceTopic topic) throws SQLException {
        topicDao.insertTopic(topic);
    }

    private void listTalks() throws SQLException {
        talkDao.listTalk();
    }
}
