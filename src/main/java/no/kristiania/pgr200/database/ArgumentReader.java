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
        this.methodArgument = arguments[0];
        this.objectArgument = arguments[1];
        this.talkDao = new ConferenceTalkDao(ConferenceDatabaseProgram.createDataSource());
        this.topicDao = new ConferenceTopicDao(ConferenceDatabaseProgram.createDataSource());

        if (arguments.length > 4) {
            this.topicArgument = arguments[4];
        } else System.out.println("Unknown Command");

        if (methodArgument.equals("insert")) {
            this.titleArgument = arguments[2];
            this.descriptionArgument = arguments[3];
            insert();
        } else if (methodArgument.equals("retrieve")) {
            this.titleArgument = arguments[2];
            this.descriptionArgument = arguments[3];
            retrieve();
        } else if (methodArgument.equals("list")) {
            list();
        } else {
            System.out.println("Unknown command");
        }
    }


    public void insert() throws SQLException {
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

    public void retrieve() throws SQLException {
        if(methodArgument.equals("retrieve") && objectArgument.equals("talk")) {
            talkDao.retrieve(1);
        } else if (methodArgument.equals("retrieve") && objectArgument.equals("topic")) {
            topicDao.retrieve(1);
        } else System.out.println("Unknown command");
    }

    public void list() throws SQLException {
        if(methodArgument.equals("list") && objectArgument.equals("talks")) {
            talkDao.list();
        } else if (methodArgument.equals("retrieve") && objectArgument.equals("topic")) {
            topicDao.list();
        } else System.out.println("Unknown command");
    }
}
