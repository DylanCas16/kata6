package software.ulpgc.kata6.application.webservice;

import io.javalin.Javalin;
import io.javalin.http.Context;
import software.ulpgc.kata6.architecture.Model.Movie;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    private static final File database = new File("moviesApi.db");

    public static void main(String[] args) {
        try {
            Connection connection = openConnection();
            DatabaseRecorder recorder = new DatabaseRecorder(connection);
            DatabaseStore store = new DatabaseStore(connection);

            Javalin app = Javalin.create();

            app.get("/", Main::index);
            app.get("/movies", context -> getMovies(context, store));
            app.get("/movie", context -> getMovie(context, store));
            app.post("/movie", context -> createMovie(context, recorder));
            app.delete("/movie", context -> deleteMovie(context, recorder));

            app.start(8080);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteMovie(Context context, DatabaseRecorder recorder) {
        String title = context.queryParam("title");
        boolean result = recorder.delete(title);
        if (result) {
            context.status(200);
            context.result("Movie " + title + "deleted");
        } else {
            context.status(404);
            context.result("Movie " + title + "not found");
        }
    }

    private static void createMovie(Context context, DatabaseRecorder recorder) {
        String title = context.queryParam("title");
        Integer year = context.queryParamAsClass("year", Integer.class).getOrDefault(-1);
        Integer duration = context.queryParamAsClass("duration", Integer.class).getOrDefault(-1);

        if (title == null) {
            context.status(400);
            context.result("Movie must have at least a title");
        }
        else {
            Movie movie = new Movie(title, year, duration);
            recorder.put(Stream.of(movie));
            context.status(200);
            context.json(movie);
        }
    }

    private static void getMovie(Context context, DatabaseStore store) {
        String title = context.queryParam("title");
        List<Movie> movies = store.movies().filter(movie -> movie.title().equals(title)).toList();

        if (movies.isEmpty()) {
            context.status(404);
            context.result("Movie " + title + " not found");
        } else {
            context.status(200);
            context.json(movies);
        }
    }

    private static void getMovies(Context context, DatabaseStore store) {
        List<Movie> movies = store.movies().toList();

        if (movies.isEmpty()) {
            context.status(404);
            context.result("Movies nof found");
        }
        else {
            context.status(200);
            context.json(movies);
        }
    }

    private static void index(Context context) {
        context.status(200);
        context.result("""
                Path options:
                get: "/" index
                
                get: "/movies" to get all movies from the database
                get: "/movie?title=param" to get a specific movie
                post: "/movie?title=param&year=param&duration=param" to create a movie
                delete: "/movie?title=param" to delete a movie
                """);
    }

    private static Connection openConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + database.getAbsolutePath());
        connection.setAutoCommit(false);
        return connection;
    }
}
