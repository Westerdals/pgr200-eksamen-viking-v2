package no.kristiania.pgr200.database;

public class ConferenceTopic {
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
