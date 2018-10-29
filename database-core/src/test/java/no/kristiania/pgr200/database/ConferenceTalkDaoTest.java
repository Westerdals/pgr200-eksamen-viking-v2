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


public class ConferenceTalkDaoTest {
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

    @Test
    public void shouldInsertConferenceTalks() throws SQLException {
        ConferenceTalk talk = new ConferenceTalk("My Talk Title", "A description of my Talk", null);
        ConferenceTalkDao talkDao = new ConferenceTalkDao(createDataSource());
        talkDao.insert(talk);
        assertThat(talkDao.list()).extracting(t -> t.getTitle()).contains("My Talk Title");
    }

    @Test
    public void confirmCorrectTalkId() throws SQLException {
        ConferenceTalk talk = new ConferenceTalk("My Talk Title", "A description of my Talk", null);
        ConferenceTalkDao dao = new ConferenceTalkDao(createDataSource());
        dao.insert(talk);
        assertThat(dao.list()).extracting(t -> t.getId()).contains(talk.getId());
    }
}