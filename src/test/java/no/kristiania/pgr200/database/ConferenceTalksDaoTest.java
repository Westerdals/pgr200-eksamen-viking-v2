package no.kristiania.pgr200.database;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import no.kristiania.pgr200.database.ConferenceTalkDao;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class ConferenceTalksDaoTest {

    @Test
    public void shouldInsertConferenceTalks() throws SQLException {
        ConferenceTalk talk = new ConferenceTalk("My Talk Title", "A description of my Talk");
        ConferenceTalkDao dao = new ConferenceTalkDao(createDataSource());
        dao.insertTalk(talk);
        assertThat(dao.listTalks()).extracting(t -> t.getTitle()).contains("My Talk Title");
    }

    @Test
    public void confirmCorrectId() throws SQLException {
        ConferenceTalk talk = new ConferenceTalk("My Talk Title", "A description of my Talk");
        ConferenceTalkDao dao = new ConferenceTalkDao(createDataSource());
        dao.insertTalk(talk);
        assertThat(dao.listTalks()).extracting(t -> t.getId()).contains(talk.getId());
    }

    private DataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.clean();
        flyway.migrate();
        return dataSource;
    }


}
