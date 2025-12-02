package software.ulpgc.kata6.application.DB;

import software.ulpgc.kata6.architecture.Model.Movie;
import software.ulpgc.kata6.architecture.io.Store;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class DatabaseStore implements Store {
    private final Connection connection;

    public DatabaseStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stream<Movie> movies() {
        try {
            return moviesIn(query());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> moviesIn(ResultSet query) {
        return Stream.generate(()->movieIn(query)).takeWhile(Objects::nonNull);
    }

    private Movie movieIn(ResultSet query) {
        try {
            return query.next() ? readMovieFrom(query) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie readMovieFrom(ResultSet query) throws SQLException {

        return new Movie(query.getString(1), query.getInt(2), query.getInt(3));
    }

    private ResultSet query() throws SQLException {
        return connection.createStatement().executeQuery("SELECT title, year, duration FROM movies");
    }
}
