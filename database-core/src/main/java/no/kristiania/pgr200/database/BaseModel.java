package no.kristiania.pgr200.database;

public interface BaseModel {
    public String getTable();
    public String[] getColumns();
    public String[] getColumnsWithValue();
    public Object getColumnValue(String columnName);
    public void setId(int id);
}