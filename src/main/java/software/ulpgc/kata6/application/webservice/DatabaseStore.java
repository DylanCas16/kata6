package software.ulpgc.kata6.application.webservice;

import software.ulpgc.kata6.architecture.Model.Movie;
import software.ulpgc.kata6.architecture.io.Store;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DatabaseStore implements Store {
    private final Connection connection;

    public DatabaseStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stream<Movie> movies() {
        List<Movie> movies = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet query = statement.executeQuery("SELECT * FROM movies");
            while (query.next()) {
                movies.add(readMovieFrom(query));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies.stream();
    }

    private Movie readMovieFrom(ResultSet query) throws SQLException {
        return new Movie(query.getString(1), query.getInt(2), query.getInt(3));
    }
}
