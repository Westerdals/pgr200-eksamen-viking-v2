package no.kristiania.pgr200.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConferenceTopicDao {

        private DataSource dataSource;

        public ConferenceTopicDao(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public List<ConferenceTopic> listTopics () {
            try (Connection conn = dataSource.getConnection()) {
                String query = "select * from topic";
                try(PreparedStatement statement = conn.prepareStatement(query)) {
                    try(ResultSet rs = statement.executeQuery()) {
                        List<ConferenceTopic> result = new ArrayList<>();
                        while(rs.next()) {
                            ConferenceTopic confTopic = new ConferenceTopic();
                            confTopic.setId(rs.getInt("id"));
                            confTopic.setTitle(rs.getString("title"));
                            System.out.println("Title: " + confTopic.getTitle());
                            result.add(confTopic);
                        }
                        return result;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
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


        public void joinTopic(ConferenceTalk talk, ConferenceTopic topic) throws  SQLException {
            try(Connection conn = dataSource.getConnection()) {
                String sql = "SELECT topic.title, conference_talk.title from topic" +
                        "left join conference_talk on topic.id = conference_talk.id";
            }
        }

}
