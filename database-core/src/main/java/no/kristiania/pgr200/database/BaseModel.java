package no.kristiania.pgr200.database;

public interface BaseModel {
    String getTable();
    String[] getColumns();
    String[] getColumnsWithValue();
    Object getColumnValue(String columnName);
    void setId(int id);
}