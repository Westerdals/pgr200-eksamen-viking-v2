package no.kristiania.pgr200.database;

import java.sql.*;
import java.util.UUID;

import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;

public class ConferenceDatabaseProgram {

    private DataSource dataSource;
    private ConferenceTalkDao dao;

    public ConferenceDatabaseProgram() throws SQLException {
        this.dataSource = createDataSource();
        this.dao = new ConferenceTalkDao(dataSource);
        this.dao.createTableIfNotExists();
    }

    public static DataSource createDataSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost/postgres");
        dataSource.setUser("demouser");
        dataSource.setPassword("root");
        return dataSource;
    }



    public static void main(String[] args) throws SQLException {
        new ConferenceDatabaseProgram().run(args);
        ConferenceTalkDao test = new ConferenceTalkDao(createDataSource());
        test.listTalks();

    }

    private void run(String[] args) throws SQLException {
        if (args.length == 0) {
            System.out.println("Run the class with an argument, on of `insert`, or ...");
            System.exit(1);
        }


        String command = args[0];

        if (command.equals("insert")) {
            insertTalk();
        } else {
            System.err.println("Unknown command!");
        }
    }

    private void insertTalk() throws SQLException {
        dao.insertTalk("A new talk called " + UUID.randomUUID(), "This is a nice description");
    }

}
