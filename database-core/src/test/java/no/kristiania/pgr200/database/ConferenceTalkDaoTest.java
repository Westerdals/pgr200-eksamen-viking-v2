package no.kristiania.pgr200.database;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
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
        assertThat(talkDao.list()).extracting(ConferenceTalk::getTitle).contains("My Talk Title");
    }

    @Test
    public void shouldInsertConferenceTalkWithTopic() {

    }

    @Test
    public void shouldRetrieveOneTalk() throws SQLException {
        ConferenceTalk talk = new ConferenceTalk("My Talk Title", "A description of my Talk", null);
        ConferenceTalkDao talkDao = new ConferenceTalkDao(createDataSource());
        talkDao.insert(talk);

        assertThat(talkDao.retrieve(1).getId()).isEqualTo(1);
    }

    @Test
    public void shouldReturnConferenceTalkWithGivenTopic() throws SQLException {
        String topic = "Science";
        ConferenceTalkDao talkDao = new ConferenceTalkDao(createDataSource());
        ConferenceTalk talk2 = new ConferenceTalk("My Talk Title", "A description of my Talk", "Hacking");
        ConferenceTalk talk1 = new ConferenceTalk("My Talk Title", "A description of my Talk", topic);


        talkDao.insert(talk1);
        talkDao.insert(talk2);
        List<ConferenceTalk> list = talkDao.listConferenceTalkWithTopic(topic);

        System.out.println(list.get(0));

        assertThat(list.get(0).getTopic()).isEqualTo(topic);
    }

    @Test
    public void confirmCorrectTalkId() throws SQLException {
        ConferenceTalk talk = new ConferenceTalk("My Talk Title", "A description of my Talk", null);
        ConferenceTalkDao dao = new ConferenceTalkDao(createDataSource());
        dao.insert(talk);
        assertThat(dao.list()).extracting(ConferenceTalk::getId).contains(talk.getId());
    }
}