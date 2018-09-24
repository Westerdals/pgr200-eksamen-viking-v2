package no.kristiania.prg200.http;

public class HttpPath {

    private String path;

    public HttpPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        int indexOfQuestionMark = path.indexOf("?");
        String query = path.substring(indexOfQuestionMark + 1, path.length());
        if(indexOfQuestionMark == -1) {
            return null;
        }

        return query;
    }

    public String getPath() {
        int indexOfQuestionMark = path.indexOf("?");
        if(indexOfQuestionMark == -1) {
            return path;
        }

        String hostName = path.substring(0, indexOfQuestionMark);
        return hostName;
    }
}
