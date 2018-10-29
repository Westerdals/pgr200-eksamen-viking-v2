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

    public ConferenceTopic retrieve(int id) throws SQLException {
        return retrieveSingleObject("select * from topic where id = ?", this::mapToTopic, id);
    }

    public List<ConferenceTopic> list() throws SQLException {
        return list("select * from topic", this::mapToTopic);
    }

    private ConferenceTopic mapToTopic(ResultSet rs) throws SQLException {
        ConferenceTopic topic = new ConferenceTopic();
        topic.setId(rs.getInt("id"));
        topic.setTitle(rs.getString("title"));
        return topic;
    }

    public void insertTopic(ConferenceTopic topic) throws SQLException {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "insert into topic (TITLE) values (?)";
            try (PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, topic.getTitle());
                statement.executeUpdate();
                try(ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    topic.setId(resultSet.getInt("id"));
                }
            }
        }
    }
}


