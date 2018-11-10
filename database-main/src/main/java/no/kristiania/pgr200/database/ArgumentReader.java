package no.kristiania.pgr200.database;
import org.flywaydb.core.Flyway;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

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
    private int statusCode;
    private String body;
    private StringBuilder sb = new StringBuilder();



    public ArgumentReader(String[] arguments) throws SQLException, IOException {
        this.arguments = arguments;
        this.talkDao = new ConferenceTalkDao(ConferenceDatabaseProgram.createDataSource());
        this.topicDao = new ConferenceTopicDao(ConferenceDatabaseProgram.createDataSource());

        for(int i = 0; i < arguments.length; i++) {
            if (i == 0) {
                methodArgument = arguments[i];
            } else if (i == 1) {
                objectArgument = arguments[i];
            } else if (i == 2) {
                titleArgument = arguments[i];
            } else if (i == 3) {
                descriptionArgument = arguments[i];
            } else if (i == 4) {
                topicArgument = arguments[i];
            }
         }

        readArguments();
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getBody() {
        return this.body;
    }

    public int readArguments() throws IOException, SQLException {
        switch (methodArgument) {
            case "reset":
                reset();
                break;
            case "insert":
                if(titleArgument == null) {
                    sb.append("insert failed: insert command needs a title to insert");
                    this.body = sb.toString();
                    this.statusCode = 404;
                    break;
                }
                insert();
                break;
            case "retrieve":
                if(titleArgument == null) {
                    sb.append("retrieve failed: failed: retrieve command needs an id");
                    this.body = sb.toString();
                    this.statusCode = 404;
                    break;
                }
                try {retrieve(Integer.parseInt(titleArgument));  //Converts string input to integer id
                } catch (NumberFormatException e) {
                    sb.append("retrieve failed: id has to be a number");
                    this.body = sb.toString();
                }
                break;
            case "list":
                if(objectArgument == null) {
                    sb.append("list failed: specify which table you wish to list");
                    this.body = sb.toString();
                    this.statusCode = 404;
                    break;
                }
                list();
                break;
            default:
                sb.append("Unknown command");
                this.body = sb.toString();
                this.statusCode = 404;
                break;
        }
        return statusCode;
    }

    public void reset() throws IOException {
        ConferenceDatabaseProgram program = new ConferenceDatabaseProgram();
        Flyway flyway = new Flyway();
        flyway.setDataSource(program.createDataSource());
        flyway.clean();
        flyway.migrate();
        sb.append("Successfully reset the database");
        this.statusCode = 200;
        this.body = sb.toString();
        return;
    }

    public int insert() throws SQLException {
        if (arguments.length > 4) {
            topic = new ConferenceTopic(topicArgument);
            talk = new ConferenceTalk(titleArgument, descriptionArgument, topicArgument);
            sb.append("Successfully inserted " + titleArgument + " with topic: " + topicArgument + " into conference_talks");

            /*
            TODO: Brag about this in documentation
            Handles edgecase where capital topic already exists, and user tries to insert lowercase topic
             */
            List<ConferenceTopic> topics = topicDao.listTopics();
            System.out.println(topics.toString().toLowerCase().contains(topicArgument.toLowerCase()));
            if(topics.stream().map(ConferenceTopic::getTitle)
                    .noneMatch(topic -> topic.toLowerCase().equals(topicArgument.toLowerCase()))) {

                topicDao.insert(topic);
            } else {
                topicArgument = topics.stream().map(ConferenceTopic::getTitle)
                        .filter(topic -> topic.toLowerCase().equals(topicArgument.toLowerCase()))
                        .findFirst().get();
                talk.setTopic(topicArgument);
            }

            talkDao.insert(talk);

            this.body = sb.toString();
            this.statusCode = 200;
            return 200;
        } else if (objectArgument.equals("talk") && arguments.length > 3) {
            talk = new ConferenceTalk(titleArgument, descriptionArgument);
            sb.append("Successfully inserted " + titleArgument + " into conference_talk");
            talkDao.insert(talk);
            this.body = sb.toString();
            this.statusCode = 200;
            return 200;
        } else if(objectArgument.equals("topic")) {
            topic = new ConferenceTopic(titleArgument);
            topicDao.insert(topic);
            sb.append("Successfully inserted " + titleArgument + " into topic");
            this.body = sb.toString();
            this.statusCode = 200;
            return 200;
        }
        sb.append("Unknown Command");
        this.body = sb.toString();
        this.statusCode = 404;
        return 404;
    }

    public void retrieve(int id) throws SQLException {
        if(objectArgument.equals("talk")) {
            if(id > talkDao.listTalks().size()) {
                sb.append("There is no talk with id " + id);
                this.statusCode = 404;
                this.body = sb.toString();
                return;
            }
            sb.append(talkDao.retrieveTalk(id).getId() + " " + talkDao.retrieveTalk(id).getTitle() + " " + talkDao.retrieveTalk(id).getDescription() + " " + talkDao.retrieveTalk(id).getTopic());
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        } else if (methodArgument.equals("retrieve") && objectArgument.equals("topic")) {
            if(id > topicDao.listTopics().size()) {
                sb.append("There is no topic with id " + id);
                this.statusCode = 404;
                this.body = sb.toString();
                return;
            }
            sb.append(topicDao.retrieveTopic(id).getId() + " " + topicDao.retrieveTopic(id).getTitle() + " ");
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        }
        sb.append("Unknown command");
        this.body = sb.toString();
        this.statusCode = 404;
        return;
    }

    public void list() throws SQLException {
        if (objectArgument.equals("talks") && arguments.length <= 2) {
            for (ConferenceTalk talk : talkDao.listTalks()) {
                sb.append(talk.getId() + " " + talk.getTitle() + " " + talk.getDescription() + " " + talk.getTopic() + " \n");
            }
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        } else if (objectArgument.equals("topics")) {
            for (ConferenceTopic topic : topicDao.listTopics()) {
                sb.append(" " + topic.getId() + " " + topic.getTitle() + " ");
            }
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        } else if(objectArgument.equals("talks") && titleArgument.equals("with") && descriptionArgument.equals("topic") && topicArgument != null) {
            for (ConferenceTalk talk : talkDao.listConferenceTalkWithTopic(topicArgument)) {
                sb.append(talk.getId() + " " + talk.getTitle() + " " + talk.getDescription() + " " + talk.getTopic() + " \n");
            }
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        }
        this.statusCode = 404;
        sb.append("Unknown command");
        this.body = sb.toString();
        return;
    }
}