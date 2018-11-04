package no.kristiania.pgr200.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConferenceTopicDao extends AbstractDao {

    private DataSource dataSource;

    public ConferenceTopicDao(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    public ConferenceTopic retrieveTopic(int id) throws SQLException {
        return retrieveSingleObject("select * from topic where id = ?", this::mapToTopic, id);
    }

    public boolean deleteTopic(int id) throws SQLException {
        return deleteSingleObject("delete from topic where id = ?", id);
    }

    public List<ConferenceTopic> listTopics() throws SQLException {
        return list("select * from topic", this::mapToTopic);
    }

    private ConferenceTopic mapToTopic(ResultSet rs) throws SQLException {
        ConferenceTopic topic = new ConferenceTopic();
        topic.setId(rs.getInt("id"));
        topic.setTitle(rs.getString("title"));
        return topic;
    }
}


