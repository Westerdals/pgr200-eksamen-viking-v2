package no.kristiania.pgr200.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
