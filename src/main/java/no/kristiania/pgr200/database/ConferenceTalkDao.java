package no.kristiania.pgr200.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class ConferenceTalkDao {

    private DataSource dataSource;

    public ConferenceTalkDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createTableIfNotExists() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().executeUpdate("create table if not exists CONFERENCE_TALK (TITLE varchar primary key, DESCRIPTION text)");
        }
    }

    public List<ConferenceTalk> listTalks () {
        try (Connection conn = dataSource.getConnection()) {
            String query = "select * from conference_talk";
            try(PreparedStatement statement = conn.prepareStatement(query)) {
                try(ResultSet rs = statement.executeQuery()) {
                    List<ConferenceTalk> result = new ArrayList<>();
                    while(rs.next()) {
                        ConferenceTalk confTalk = new ConferenceTalk();
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

    public void insertTalk(String title, String description) throws SQLException {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "insert into CONFERENCE_TALK (TITLE, DESCRIPTION) values (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, title);
                statement.setString(2, description);

                statement.executeUpdate();
            }
        }
    }
}
