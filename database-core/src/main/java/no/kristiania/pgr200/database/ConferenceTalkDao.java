package no.kristiania.pgr200.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
public class ConferenceTalkDao extends AbstractDao {

    private DataSource dataSource;

    public ConferenceTalkDao(DataSource dataSource) {
        super(dataSource);

        this.dataSource = dataSource;
    }

    public ConferenceTalk retrieveTalk(int id) throws SQLException {
        return retrieveSingleObject("select * from conference_talk where id = ?", this::mapToTalk, id);
    }

    public boolean deleteTalk(int id) throws SQLException {
        return deleteSingleObject("delete from conference_talk where id = ?", id);
    }

    public List<ConferenceTalk> listTalks() throws SQLException {
        return list("select * from conference_talk", this::mapToTalk);
    }

    private ConferenceTalk mapToTalk(ResultSet rs) throws SQLException {
        ConferenceTalk talk = new ConferenceTalk();
        talk.setId(rs.getInt("id"));
        talk.setTitle(rs.getString("title"));
        talk.setDescription(rs.getString("description"));
        talk.setTopic(rs.getString("topic"));
        return talk;
    }

    public List<ConferenceTalk> listConferenceTalkWithTopic(String topic) throws SQLException {
        return list("select * from conference_talk", this::mapToTalk)
                .stream().filter(s -> topic.toLowerCase().equals(s.getTopic().toLowerCase())).collect(Collectors.toList());
    }

}