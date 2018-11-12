package no.kristiania.pgr200.database;

import java.util.Arrays;

public class ConferenceTalk implements BaseModel {


    private String title, description, topic;
    private int id;

    //Constructor for creating a ConferenceTalk with a topic
    public ConferenceTalk(String title, String description, String topic) {
        this.title = title;
        this.description = description;
        this.topic = topic;
    }

    //Constructor for creating a talk and then adding a topic later
    public ConferenceTalk(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTopic(){
        return topic;
    }

    @Override
    public String getTable() {
        return "conference_talk";
    }

    @Override
    public String[] getColumnsWithValue() {
        return Arrays.stream(new String[]{
                "id", "title", "description", "topic", "timeslot"
        }).filter(column -> this.getColumnValue(column) != null)
                .filter(column ->  !(column.equals("id") && this.id == 0))
                .toArray(String[]::new);
    }

    @Override
    public String[] getColumns() {
        return new String[] {
                "id", "title", "description", "topic", "timeslot"
        };
    }

    @Override
    public Object getColumnValue(String columnName) {
        switch (columnName) {
            case "id": return this.id;
            case "title": return this.title;
            case "description": return this.description;
            case "topic": return this.topic;
        }
        return null;
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
        return title + " " + description + " " + id + " " + " " + topic + " " ;
    }

}