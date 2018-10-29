package no.kristiania.pgr200.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractDao {

    protected final DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public <T> T retrieveSingleObject(String sql, ResultSetMapper<T> mapper, int id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try(ResultSet rs = statement.executeQuery()) {
                    if(rs.next()) {
                        T genericList = mapper.mapResultSet(rs);    //TODO: Seperate class for printing
                        System.out.println(genericList.toString());
                        return mapper.mapResultSet(rs);
                    }
                    return null;
                }
            }
        }
    }

    protected <T> List<T> list(String sql, ResultSetMapper<T> mapper) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet rs = statement.executeQuery()) {
                    List<T> result = new ArrayList<>();
                    while (rs.next()) {
                        T genericList = mapper.mapResultSet(rs);    //TODO: Seperate class for printing
                        System.out.println(genericList.toString());
                        result.add(genericList);
                    }
                    return result;
                }
            }
        }

    }

    protected <T extends BaseModel> boolean insert(T model) {
        try(Connection conn = dataSource.getConnection()) {
            String[] columns = model.getColumnsWithValue();
            if (columns.length == 0) return false;
            String sql = "insert into " +  model.getTable() + " (" + String.join(", ", columns) + ") " +
                    "values ("+ String.join(", ", Arrays.stream(columns).map(s -> "?").collect(Collectors.toList())) + ")";
            try (PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                int count = 1;
                for (String column :columns) {
                    statement.setObject(count++, model.getColumnValue(column));
                }
                statement.executeUpdate();

                try(ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    model.setId(resultSet.getInt("id"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}