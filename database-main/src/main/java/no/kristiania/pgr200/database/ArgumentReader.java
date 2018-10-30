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
    private int id;

    private ConferenceTalk talk;
    private ConferenceTopic topic;
    private ConferenceTalkDao talkDao;
    private ConferenceTopicDao topicDao;

    public ArgumentReader(String[] arguments) throws SQLException, IOException, ClassNotFoundException {
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
                insert();
                break;
            case "retrieve":
                if(titleArgument == null) {
                    System.out.println("Retrieve command needs an id");
                    break;
                }
                retrieve(Integer.parseInt(titleArgument));  //Converts string input to integer id
                break;
            case "list":
                list();
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }

    private void reset() throws SQLException, IOException, ClassNotFoundException {
        ConferenceDatabaseProgram program = new ConferenceDatabaseProgram();
        Flyway flyway = new Flyway();
        flyway.setDataSource(program.createDataSource());
        flyway.clean();
    }

    private void insert() {
    if (objectArgument.equals("talk") && arguments.length > 4) {
            talk = new ConferenceTalk(titleArgument, descriptionArgument, topicArgument);
    } else if (objectArgument.equals("talk") && arguments.length > 3) {
            talk = new ConferenceTalk(titleArgument, descriptionArgument);
        } else if(objectArgument.equals("topic")) {
            topic = new ConferenceTopic(titleArgument);
            topicDao.insert(topic);
            return;
        } else System.out.println("Unknown Command.");

        talkDao.insert(talk);
    }

    private void retrieve(int id) throws SQLException {

        if(methodArgument.equals("retrieve") && objectArgument.equals("talk")) {
            if(id > talkDao.list().size()) {
                System.out.println("There is no talk with id " + id);
                return;
            }
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s %20s %20s %10s %10s", "|", "ID", "|", "Title", "|", "Description", "|", "Topic", "|"));
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
            System.out.println(String.format("%1s %2s %1s %15s %15s %20s %20s %10s %10s", "|", talkDao.retrieve(id).getId(), "|", talkDao.retrieve(id).getTitle(), "|", talkDao.retrieve(id).getDescription(), "|", talkDao.retrieve(id).getTopic(), "|"));
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
        } else if (methodArgument.equals("retrieve") && objectArgument.equals("topic")) {
            if(id > topicDao.list().size()) {
                System.out.println("There is no topic with id " + id);
                return;
            }
            System.out.println(String.format("%s", "--------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s", "|", "ID", "|", "Title", "|"));
            System.out.println(String.format("%s", "--------------------------------------"));
            System.out.println(String.format("%1s %2s %1s %15s %15s", "|", topicDao.retrieve(id).getId(), "|",  topicDao.retrieve(id).getTitle(), "|"));
            System.out.println(String.format("%s", "--------------------------------------"));
        } else System.out.println("Unknown command");
    }

    private void list() throws SQLException {
        if (methodArgument.equals("list") && objectArgument.equals("talks")) {
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s %20s %20s %10s %10s", "|", "ID", "|", "Title", "|", "Description", "|", "Topic", "|"));
            for (ConferenceTalk talk : talkDao.list()) {
                System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));
                System.out.println(String.format("%1s %2s %1s %15s %15s %20s %20s %10s %10s", "|", talk.getId(), "|", talk.getTitle(), "|", talk.getDescription(), "|", talk.getTopic(), "|"));
            }
            System.out.println(String.format("%s", "------------------------------------------------------------------------------------------------------"));

        } else if (methodArgument.equals("list") && objectArgument.equals("topics")) {
            System.out.println(String.format("%s", "--------------------------------------"));
            System.out.println(String.format("%1s %1s %1s %15s %15s", "|", "ID", "|", "Title", "|"));
            for (ConferenceTopic topic : topicDao.list()) {
                System.out.println(String.format("%s", "--------------------------------------"));
                System.out.println(String.format("%1s %2s %1s %15s %15s", "|", topic.getId(), "|", topic.getTitle(), "|"));
            }
            System.out.println(String.format("%s", "--------------------------------------"));

        }
    }
}
