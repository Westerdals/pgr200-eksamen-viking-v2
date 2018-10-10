package no.kristiania.pgr200.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class TestDataSource {

    public static DataSource createTestDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost/postgres");
        dataSource.setUser("demouser");
        dataSource.setPassword("root");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        return dataSource;

    }
}
