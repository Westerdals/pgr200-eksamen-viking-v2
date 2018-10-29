package no.kristiania.pgr200.database;

import java.util.Arrays;

public class ConferenceTopic implements BaseModel {
    private String title;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getTable() {
        return "topic";
    }

    @Override
    public String[] getColumns() {
        return new String[] {
                "id", "title"
        };
    }

    @Override
    public String[] getColumnsWithValue() {
        return Arrays.stream(new String[]{
                "id", "title",
        }).filter(column -> this.getColumnValue(column) != null)
                .filter(column ->  !(column.equals("id") && this.id == 0))
                .toArray(String[]::new);
    }

    @Override
    public Object getColumnValue(String columnName) {
        switch (columnName) {
            case "id": return this.id;
            case "title": return this.title;
        }
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ConferenceTopic (String title) {
        this.title = title;
    }

    public ConferenceTopic() {

    }

    @Override
    public String toString() {
        return "ConferenceTopic{" +
                "title='" + title + '\'' +
                ", id=" + id +
                '}';
    }
}
