package no.kristiania.pgr200.database;

public class ConferenceTalk {


    public ConferenceTalk(String title, String description, String topic) {
        this.title = title;
        this.description = description;
        this.topic = topic;
    }

    private String title, description, topic;
    private int id;

    public int getId() {
        return id;
    }

    public String getTopic(){
        return topic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public ConferenceTalk () {

    }

    @Override
    public String toString() {
        return "_______________________________________________________\n" +
                "| " + title + " | " + description + " | " + id + " | " + " | " + topic +  " | " ;
    }
}
