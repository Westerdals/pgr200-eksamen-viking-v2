package no.kristiania.pgr200.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
public class ConferenceTalkDao extends AbstractDao {

    private DataSource dataSource;

    public ConferenceTalkDao(DataSource dataSource) {
        super(dataSource);

        this.dataSource = dataSource;
    }

    public ConferenceTalk retrieve(int id) throws SQLException {
        return retrieveSingleObject("select * from conference_talk where id = ?", this::mapToTalk, id);
    }

    public List<ConferenceTalk> list() throws SQLException {
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
                .stream().filter(s -> topic.equals(s.getTopic())).collect(Collectors.toList());
    }

}