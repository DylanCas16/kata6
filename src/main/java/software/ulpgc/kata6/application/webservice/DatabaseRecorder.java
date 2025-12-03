package software.ulpgc.kata6.application.webservice;

import software.ulpgc.kata6.architecture.Model.Movie;
import software.ulpgc.kata6.architecture.io.Recorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

public class DatabaseRecorder implements Recorder {
    private final Connection connection;
    private final PreparedStatement statement;

    public DatabaseRecorder(Connection connection) throws SQLException {
        this.connection = connection;
        this.createTableIfRequired();
        this.statement = connection.prepareStatement("INSERT INTO movies (title, year, duration) VALUES (?, ?, ?)");
    }

    private void createTableIfRequired() throws SQLException {
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS movies (title TEXT, year INTEGER, duration INTEGER)");
    }

    @Override
    public void put(Stream<Movie> movies) {
        try {
            movies.forEach(this::record);
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void record(Movie movie) {
        try {
            write(movie);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(Movie movie) throws SQLException {
        statement.setString(1, movie.title());
        statement.setInt(2, movie.year());
        statement.setInt(3, movie.duration());
        statement.addBatch();
    }

    public boolean delete(String title) {
        try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM movies WHERE title = ?")) {
            deleteStatement.setString(1, title);
            int deleted = deleteStatement.executeUpdate();
            return deleted == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
