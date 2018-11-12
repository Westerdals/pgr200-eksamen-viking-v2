package no.kristiania.pgr200.database;

import org.flywaydb.core.Flyway;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


class ArgumentReader {

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



    ArgumentReader(String[] arguments) throws SQLException, IOException {
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

    ArgumentReader() {

    }

    int getStatusCode() {
        return this.statusCode;
    }

    String getBody() {
        return this.body;
    }

    private void readArguments() throws IOException, SQLException {
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
            case "update":
                if(objectArgument == null) {
                    sb.append("Update failed");
                    this.body = sb.toString();
                    this.statusCode = 404;
                    break;
                }
                try {update(Integer.parseInt(titleArgument));  //Converts string input to integer id
                } catch (NumberFormatException e) {
                    sb.append("update failed: id has to be a number");
                    this.body = sb.toString();
                }
                break;
            case "delete":
                if(objectArgument == null) {
                    sb.append("Delete failed");
                    this.body = sb.toString();
                    this.statusCode = 404;
                    break;
                } try {delete(Integer.parseInt(titleArgument));  //Converts string input to integer id
            } catch (NumberFormatException e) {
                sb.append("retrieve failed: id has to be a number");
                this.body = sb.toString();
            }
                break;
            default:
                sb.append("Unknown command");
                this.body = sb.toString();
                this.statusCode = 404;
                break;
        }
    }


    void reset() throws IOException {
        ConferenceDatabaseProgram program = new ConferenceDatabaseProgram();
        Flyway flyway = new Flyway();
        flyway.setDataSource(program.createDataSource());
        flyway.clean();
        flyway.migrate();
        sb.append("Successfully reset the database");
        this.statusCode = 200;
        this.body = sb.toString();
    }

    private int insert() throws SQLException {
        if (arguments.length > 4) {
            topic = new ConferenceTopic(topicArgument);
            talk = new ConferenceTalk(titleArgument, descriptionArgument, topicArgument);

            List<ConferenceTopic> topics = topicDao.listTopics();
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
            sb.append("Successfully inserted ").append(titleArgument).append(" with topic: ").append(topicArgument).append(" into conference_talks");
            this.body = sb.toString();
            this.statusCode = 201;
            return 201;
        } else if (objectArgument.equals("talk") && arguments.length > 3) {
            talk = new ConferenceTalk(titleArgument, descriptionArgument);
            sb.append("Successfully inserted ").append(titleArgument).append(" into conference_talk");
            talkDao.insert(talk);
            this.body = sb.toString();
            this.statusCode = 201;
            return 201;
        } else if(objectArgument.equals("topic")) { //Insert topic
            List<ConferenceTopic> topics = topicDao.listTopics();
            if(topics.stream().map(ConferenceTopic::getTitle)
                    .anyMatch(topic -> topic.toLowerCase().equals(titleArgument.toLowerCase()))) {
                sb.append("topic ").append(titleArgument).append(" already exists");
                this.body = sb.toString();
                this.statusCode = 202;
                return 201;
            }
            topic = new ConferenceTopic(titleArgument);
            topicDao.insert(topic);
            sb.append("Successfully inserted ").append(titleArgument).append(" into topic");
            this.body = sb.toString();
            this.statusCode = 201;
            return 201;
        }
        sb.append("Unknown Command");
        this.body = sb.toString();
        this.statusCode = 404;
        return 404;
    }

    private void delete(int id) throws SQLException {
        if(talkDao.retrieveTalk(id) != null) {
            String talkTitle = talkDao.retrieveTalk(id).getTitle();
            talkDao.deleteTalk(id);
            sb.append("Successfully deleted conference talk ").append(talkTitle);
            this.body = sb.toString();
            this.statusCode = 200;
        } else {
            sb.append("The talk you tried to delete does not exist");
            this.body = sb.toString();
            this.statusCode = 404;
        }
    }

    private void update(int id) throws SQLException {
        if(descriptionArgument.equals("topic")) {
            List<ConferenceTopic> topics = topicDao.listTopics();
            if (topics.stream().map(ConferenceTopic::getTitle)
                    .noneMatch(topic -> topic.toLowerCase().equals(topicArgument.toLowerCase()))) {
                topic = new ConferenceTopic(topicArgument);
                topicDao.insert(topic);
            } else {
                topicArgument = topics.stream().map(ConferenceTopic::getTitle)
                        .filter(topic -> topic.toLowerCase().equals(topicArgument.toLowerCase()))
                        .findFirst().get();
                talkDao.updateSingleObject(id, "conference_talk", descriptionArgument, topicArgument);
            }
        }

        if(talkDao.retrieveTalk(id) != null) {
            talkDao.updateSingleObject(id, "conference_talk", descriptionArgument, topicArgument);
            sb.append("Successfully updated conference talk ").append(id).append(" with ").append(topicArgument).append(" in ").append(descriptionArgument);
            this.body = sb.toString();
            this.statusCode = 200;
        } else {
            sb.append("The talk you tried to update does not exist");
            this.body = sb.toString();
            this.statusCode = 404;
        }
    }

    private void retrieve(int id) throws SQLException {
        if(objectArgument.equals("talk")) {
            if(id > talkDao.listTalks().size()) {
                sb.append("There is no talk with id ").append(id);
                this.statusCode = 404;
                this.body = sb.toString();
                return;
            }
            try {
                sb.append(talkDao.retrieveTalk(id).getId()).append(" ").append(talkDao.retrieveTalk(id).getTitle()).append(" ").append(talkDao.retrieveTalk(id).getDescription()).append(" ").append(talkDao.retrieveTalk(id).getTopic());
            } catch (NullPointerException e) {
                sb.append("id ").append(id).append(" does not exist in conference talk");
                this.body = sb.toString();
                this.statusCode = 404;
                return;
            }
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        } else if (methodArgument.equals("retrieve") && objectArgument.equals("topic")) {
            if(id > topicDao.listTopics().size()) {
                sb.append("There is no topic with id ").append(id);
                this.statusCode = 404;
                this.body = sb.toString();
                return;
            }
            sb.append(topicDao.retrieveTopic(id).getId()).append(" ").append(topicDao.retrieveTopic(id).getTitle()).append(" ");
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        }
        sb.append("Unknown command");
        this.body = sb.toString();
        this.statusCode = 404;
    }

    private void list() throws SQLException {
        if (objectArgument.equals("talks") && arguments.length <= 2) {
            if(talkDao.listTalks().isEmpty()) {
                sb.append("There are no talks in conference talk");
                this.body = sb.toString();
                this.statusCode = 404;
                return;
            }
            for (ConferenceTalk talk : talkDao.listTalks()) {
                sb.append(talk.getId()).append(" ").append(talk.getTitle()).append(" ").append(talk.getDescription()).append(" ").append(talk.getTopic()).append(" \n");
            }
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        } else if(objectArgument.equals("talks") && titleArgument.equals("with") && descriptionArgument != null) {
            List<ConferenceTopic> topics = topicDao.listTopics();
            if (topics.stream().map(ConferenceTopic::getTitle)
                    .anyMatch(topic -> topic.toLowerCase().equals(descriptionArgument.toLowerCase()))) {
                for (ConferenceTalk talk : talkDao.listConferenceTalkWithTopic(descriptionArgument)) {
                    sb.append(talk.getId()).append(" ").append(talk.getTitle()).append(" ").append(talk.getDescription()).append(" ").append(talk.getTopic()).append(" \n");
                }
                this.body = sb.toString();
                this.statusCode = 200;
                return;
            } else {
                sb.append("there are not talks with ").append(descriptionArgument).append(" as topic");
                this.body = sb.toString();
                this.statusCode = 404;
                return;
            }
        }

        if (objectArgument.equals("topics")) {
            for (ConferenceTopic topic : topicDao.listTopics()) {
                sb.append(" ").append(topic.getId()).append(" ").append(topic.getTitle()).append("\n");
            }
            this.body = sb.toString();
            this.statusCode = 200;
            return;
        }
        this.statusCode = 404;
        sb.append("Unknown command");
        this.body = sb.toString();
    }
}