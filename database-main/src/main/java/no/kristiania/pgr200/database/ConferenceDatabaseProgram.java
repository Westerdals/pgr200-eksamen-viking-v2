package no.kristiania.pgr200.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import javax.sql.DataSource;

import com.sun.org.apache.xpath.internal.Arg;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGPoolingDataSource;

public class ConferenceDatabaseProgram {

    private DataSource dataSource;
    private ConferenceTalkDao talkDao;
    private ConferenceTopicDao topicDao;
    private UriBuilder builder;
    private ArgumentReader reader;
    public static boolean useH2 = false;

    public ConferenceDatabaseProgram() throws IOException {
        this.dataSource = createDataSource();
        this.talkDao = new ConferenceTalkDao(dataSource);
        this.topicDao = new ConferenceTopicDao(dataSource);

    }

    public static DataSource createDataSource() throws IOException {
        if(useH2){
            return createH2DataSource();
        }
        Properties props = new Properties();

        try( FileInputStream in = new FileInputStream("db.properties")) {
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

    public static DataSource createH2DataSource(){
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
        return dataSource;
    }


    //TODO: Tell user to start server + Handle crash if server is not started
    public static void main(String[] args) throws SQLException, IOException {
        new ConferenceDatabaseProgram().run(args);
    }

    public void run(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Run the class with one of these arguments:\n" +
                    "For inserting a talk/topic type insert [talk/topic] [title] [description] <- Only for Talk \n" +
                    "To Retrieve a single object type Retrieve [talk/topic] and ID for talk \n" +
                    "To List either every talk or topic type list [talk/topic]");
            System.exit(1);
        }

        if(args[0].equals("reset")) {
            resetDatabase();
            return;
        }
        try {
            builder = new UriBuilder(args);
        } catch (ConnectException e) {
            System.out.println("Start the server before running the clinet");
        }


    }

    public void resetDatabase() throws IOException {
            ArgumentReader reader = new ArgumentReader();
            reader.reset();
            System.out.println("Database has been reset");
    }
}
