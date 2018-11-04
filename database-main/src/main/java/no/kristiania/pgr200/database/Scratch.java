package no.kristiania.pgr200.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class Scratch {

    public static void main(String[] args) throws IOException, SQLException {
        new Scratch();
    }

    public Scratch() throws IOException, SQLException {

        LinkedHashSet<ColumnValue> col = new LinkedHashSet<ColumnValue>();
        col.add(new ColumnValue("name", "someadas"));
        col.add(new ColumnValue("description", "asdsaldibassabf"));
        col.add(new ColumnValue("date", 22131));
        col.add(new ColumnValue("year", 123123));

        String sets = "SET " + String.join(", ", col.stream().map(ColumnValue::getSql).collect(Collectors.toList()));
        PreparedStatement s = ConferenceDatabaseProgram.createDataSource().getConnection().prepareStatement(sets);
        System.out.println(s);
        int i = 1;
        for (ColumnValue columnValue : col) {
            s.setObject(i++, columnValue.value);
        }
        System.out.println(s);
    }

    class ColumnValue<V> {
        public String column;
        public V value;

        public ColumnValue(String column, V value) {
            this.column = column;
            this.value = value;
        }

        public String getSql(){
            return column + " = ?";
        }
    }

}
