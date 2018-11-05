package no.kristiania.pgr200.database;
import org.flywaydb.core.Flyway;
import java.io.IOException;
import java.sql.SQLException;
public class ArgumentReader {
    private String[] arguments;
    private String methodArgument;
    private String objectArgument;
    private String titleArgument;
    private String descriptionArgument;
    private String topicArgument;
    private ConferenceTalk talk;
    private ConferenceTopic topic;
    private ConferenceTalkDao talkDao;
    private ConferenceTopicDao topicDao;
    public ArgumentReader(String[] arguments) throws SQLException, IOException {
        this.arguments = arguments;
        this.talkDao = new ConferenceTalkDao(ConferenceDatabaseProgram.createDataSource());
        this.topicDao = new ConferenceTopicDao(ConferenceDatabaseProgram.createDataSource());
        for(int i = 0; i < arguments.length; i++) {
            if(i == 0) { methodArgument = arguments[i]; }
            else if(i == 1){ objectArgument = arguments[i]; }
            else if(i == 2) { titleArgument = arguments[i]; }
            else if(i == 3) { descriptionArgument = arguments[i]; }
            else if(i == 4) { topicArgument = arguments[i]; }
        }
        switch (methodArgument) {
            case "reset":
                reset();
                break;
            case "insert":
                if(titleArgument == null) {
                    System.out.println("insert failed: insert command needs a title to insert");
                    break;
                }
                insert();
                break;
            case "retrieve":
                if(titleArgument == null) {
                    System.out.println("retrieve failed: retrieve command needs an id");
                    break;
                }
                retrieve(Integer.parseInt(titleArgument));  //Converts string input to integer id
                break;
            case "list":
                if(objectArgument == null) {
                    System.out.println("list failed: specify which table you wish to list   ");
                    break;
                }
                list();
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }
    private void reset() throws IOException {
        ConferenceDatabaseProgram program = new ConferenceDatabaseProgram();
        Flyway flyway = new Flyway();
        flyway.setDataSource(program.createDataSource());
        flyway.clean();
        flyway.migrate();
    }
    private void insert() {
        if (arguments.length > 4) {
            topic = new ConferenceTopic(topicArgument);
            topicDao.insert(topic);
            talk = new ConferenceTalk(titleArgument, descriptionArgument, topicArgument);
            System.out.println("Successfully inserted " + titleArgument + " with topic: " + topicArgument + " into conference_talks");
        } else if (objectArgument.equals("talk") && arguments.length > 3) {
            talk = new ConferenceTalk(titleArgument, descriptionArgument);
            System.out.println("Successfully inserted " + titleArgument + " into conference_talk");
        } else if(objectArgument.equals("topic")) {
            topic = new ConferenceTopic(titleArgument);
            topicDao.insert(topic);
            System.out.println("Successfully inserted " + titleArgument + " into topic");
            return;
        } else System.out.println("Unknown Command.");
        talkDao.insert(talk);
    }
    private void retrieve(int id) throws SQLException {
        if(objectArgument.equals("talk")) {
            if(id > talkDao.listTalks().size()) {
                System.out.println("There is no talk with id " + id);
                return;
            }
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s %20s %20s %10s %10s", "|", "ID", "|", "Title", "|", "Description", "|", "Topic", "|"));
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
            System.out.println(String.format("%1s %2s %1s %15s %1s %20s %20s %10s %10s", "|", talkDao.retrieveTalk(id).getId(), "|", talkDao.retrieveTalk(id).getTitle(), "|", talkDao.retrieveTalk(id).getDescription(), "|", talkDao.retrieveTalk(id).getTopic(), "|"));
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
        } else if (methodArgument.equals("retrieve") && objectArgument.equals("topic")) {
            if(id > topicDao.listTopics().size()) {
                System.out.println("There is no topic with id " + id);
                return;
            }
            System.out.println(String.format("%s", "--------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s", "|", "ID", "|", "Title", "|"));
            System.out.println(String.format("%s", "--------------------------------------"));
            System.out.println(String.format("%1s %2s %1s %15s %15s", "|", topicDao.retrieveTopic(id).getId(), "|",  topicDao.retrieveTopic(id).getTitle(), "|"));
            System.out.println(String.format("%s", "--------------------------------------"));
        } else System.out.println("Unknown command");
    }
    private void list() throws SQLException {
        if (objectArgument.equals("talks") && arguments.length <= 2) {
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s %20s %20s %10s %10s", "|", "ID", "|", "Title", "|", "Description", "|", "Topic", "|"));
            for (ConferenceTalk talk : talkDao.listTalks()) {
                System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
                System.out.println(String.format("%1s %2s %1s %15s %15s %20s %20s %10s %10s", "|", talk.getId(), "|", talk.getTitle(), "|", talk.getDescription(), "|", talk.getTopic(), "|"));
            }
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
        } else if (objectArgument.equals("topics")) {
            System.out.println(String.format("%s", "--------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s", "|", "ID", "|", "Title", "|"));
            for (ConferenceTopic topic : topicDao.listTopics()) {
                System.out.println(String.format("%s", "--------------------------------------"));
                System.out.println(String.format("%1s %2s %1s %15s %15s", "|", topic.getId(), "|", topic.getTitle(), "|"));
            }
            System.out.println(String.format("%s", "--------------------------------------"));
        } else if(objectArgument.equals("talks") && titleArgument.equals("with") && descriptionArgument.equals("topic") && topicArgument != null) {
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s %20s %20s %10s %10s", "|", "ID", "|", "Title", "|", "Description", "|", "Topic", "|"));
            for (ConferenceTalk talk : talkDao.listConferenceTalkWithTopic(topicArgument)) {
                System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
                System.out.println(String.format("%1s %2s %1s %15s %15s %20s %20s %10s %10s", "|", talk.getId(), "|", talk.getTitle(), "|", talk.getDescription(), "|", talk.getTopic(), "|"));
            }
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
        }
        else System.out.println("Unknown command");
    }
}