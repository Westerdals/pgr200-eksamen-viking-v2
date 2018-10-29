package no.kristiania.pgr200.database;

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

    public ArgumentReader(String[] arguments) throws SQLException {
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
            case "insert":
                insert();
                break;
            case "retrieve":
                retrieve();
                break;
            case "list":
                list();
                break;
            default:
                System.out.println("Unknown command");
                break;
        }
    }

    private void insert() throws SQLException {
    if (methodArgument.equals("insert") && objectArgument.equals("talk") && arguments.length > 4) {
            talk = new ConferenceTalk(titleArgument, descriptionArgument, topicArgument);
    } else if (objectArgument.equals("talk") && arguments.length > 3) {
            talk = new ConferenceTalk(titleArgument, descriptionArgument);
        } else if(objectArgument.equals("topic")) {
            topic = new ConferenceTopic(titleArgument);
            topicDao.insertTopic(topic);
        } else System.out.println("Unknown Command.");

        talkDao.insertTalk(talk);
    }

    private void retrieve() throws SQLException {
        if(methodArgument.equals("retrieve") && objectArgument.equals("talk")) {
            talkDao.retrieve(1);
        } else if (methodArgument.equals("retrieve") && objectArgument.equals("topic")) {
            topicDao.retrieve(1);
        } else System.out.println("Unknown command");
    }

    private void list() throws SQLException {
        if(methodArgument.equals("list") && objectArgument.equals("talks")) {
            talkDao.list();
        } else if (methodArgument.equals("retrieve") && objectArgument.equals("topic")) {
            topicDao.list();
        } else System.out.println("Unknown command");
    }
}
