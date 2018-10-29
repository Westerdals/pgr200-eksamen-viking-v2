package no.kristiania.pgr200.database;

import java.sql.*;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGPoolingDataSource;

public class ConferenceDatabaseProgram {

    private DataSource dataSource;
    private ConferenceTalkDao talkDao;
    private ConferenceTopicDao topicDao;
    private ArgumentReader argumentReader;

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
        //flyway.clean();
        flyway.migrate();

        return dataSource;
    }

    public static void main(String[] args) throws SQLException {
        new ConferenceDatabaseProgram().run(args);
    }

    public void run(String[] args) throws SQLException {
        if (args.length == 0) {
            System.out.println("Run the class with one of these arguments:\n" +
                    "For inserting a talk/topic type Insert [Talk/Topic] [Title] [Description] <- Only for Talk \n" +
                    "To Retrieve a single object type Retrieve [Talk/Topic] and ID for talk \n" +
                    "To List either every talk or topic type List [Talk/Topic]");
            System.exit(1);
        }

        argumentReader = new ArgumentReader(args);
    }
}
