package no.kristiania.pgr200.database;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import no.kristiania.pgr200.database.ConferenceTalkDao;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class ConferenceTalksDaoTest {

    @Test
    public void shouldInsertConferenceTalks() throws SQLException {
        ConferenceTalkDao dao = new ConferenceTalkDao(createDataSource());
        dao.createTableIfNotExists();
        dao.insertTalk("My Talk Title", "A longer description of the talk");
        assertThat(dao.listTalks()).extracting(t -> t.getTitle()).contains("My Talk Title");
    }

    @Test
    public void confirmCorrectId() throws SQLException {
        ConferenceTalkDao dao = new ConferenceTalkDao(createDataSource());
        dao.createTableIfNotExists();
        dao.insertTalk("My Talk Title", "A longer description of the talk");
        assertThat(dao.listTalks()).extracting(t -> t.getId()).contains(dao.listTalks().size());
    }

    private DataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        return dataSource;
    }


}
