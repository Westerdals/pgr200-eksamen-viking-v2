package no.kristiania.pgr200.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;
import org.postgresql.core.SqlCommand;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ConferenceTopicDaoTest {
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
    public void shouldInsertConferenceTopics() throws SQLException {
        ConferenceTopicDao dao = new ConferenceTopicDao(createDataSource());
        ConferenceTopic topic = new ConferenceTopic("Gardening");

        dao.insert(topic);
        assertThat(dao.listTopics()).extracting(ConferenceTopic::getTitle).contains("Gardening");
    }

    @Test
    public void shouldRetrieveOneTopic() throws SQLException {
        ConferenceTopicDao topicDao = new ConferenceTopicDao(createDataSource());
        ConferenceTopic topic = new ConferenceTopic("Cool Topic");

        topicDao.insert(topic);
        assertThat(topicDao.retrieveTopic(1).getId()).isEqualTo(1);
    }

    @Test
    public void shouldDeleteTopic() throws SQLException {
        ConferenceTopicDao topicDao = new ConferenceTopicDao(createDataSource());
        ConferenceTopic topic1 = new ConferenceTopic("Gardening");
        ConferenceTopic topic2 = new ConferenceTopic("Eating");

        topicDao.insert(topic1);
        topicDao.insert(topic2);
        topicDao.deleteTopic(1);

        assertThat(topicDao.retrieveTopic(topic1.getId())).isEqualTo(null);
    }

    @Test
    public void shouldUpdateTest() throws SQLException {
        ConferenceTopicDao topicDao = new ConferenceTopicDao(createDataSource());
        ConferenceTopic topic = new ConferenceTopic("Gardening");

        topicDao.insert(topic);
        topicDao.updateSingleObject(topic.getId(), "topic", "title", "Cooking");
        assertThat(topicDao.retrieveTopic(topic.getId()).getTitle()).isEqualTo("Cooking");
    }

    @Test
    public void shouldListConferenceTopics() throws SQLException {
        ConferenceTopic topic = new ConferenceTopic("Technology");
        ConferenceTopicDao dao = new ConferenceTopicDao(createDataSource());
        dao.insert(topic);
        assertThat(dao.listTopics().size()).isEqualTo(topic.getId());
    }

    @Test
    public void confirmCorrectTopicId() throws SQLException {
        ConferenceTopic topic = new ConferenceTopic("Fiction");
        ConferenceTopicDao dao = new ConferenceTopicDao(createDataSource());
        dao.insert(topic);
        assertThat(dao.listTopics()).extracting(ConferenceTopic::getId).contains(topic.getId());
    }
}