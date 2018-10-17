package no.kristiania.pgr200.database;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class ConferenceTalkDao extends AbstractDao {

    private DataSource dataSource;

    public ConferenceTalkDao(DataSource dataSource) {
        super(dataSource);
    }

    public ConferenceTalk retrieve(int id) throws SQLException {
        return retrieveSingleObject("select * from conference_talk where id = ?", this::mapToTalk, id);
    }

    public List<ConferenceTalk> listTalk() throws SQLException {
        return list("select * from conference_talk", this::mapToTalk);
    }


    public List<ConferenceTalk> listTalks () {
        try (Connection conn = dataSource.getConnection()) {
            String query = "select * from conference_talk";
            try(PreparedStatement statement = conn.prepareStatement(query)) {
                try(ResultSet rs = statement.executeQuery()) {
                    List<ConferenceTalk> result = new ArrayList<>();
                    while(rs.next()) {
                        ConferenceTalk confTalk = new ConferenceTalk();
                        confTalk.setId(rs.getInt("id"));
                        confTalk.setTitle(rs.getString("title"));
                        confTalk.setDescription(rs.getString("description"));
                        System.out.println("Title: " + confTalk.getTitle() + " - Description: " + confTalk.getDescription());
                        result.add(confTalk);
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ConferenceTalk mapToTalk(ResultSet rs) throws SQLException {
        ConferenceTalk talk = new ConferenceTalk();
        talk.setId(rs.getInt("id"));
        talk.setTitle(rs.getString("title"));
        talk.setDescription(rs.getString("description"));
        return talk;
    }


    public void insertTalk(ConferenceTalk talk) throws SQLException {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "insert into CONFERENCE_TALK (TITLE, DESCRIPTION) values (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, talk.getTitle());
                statement.setString(2, talk.getDescription());

                statement.executeUpdate();

                try(ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    talk.setId(resultSet.getInt("id"));
                }
            }
        }
    }
}


