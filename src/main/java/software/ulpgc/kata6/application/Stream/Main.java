package software.ulpgc.kata6.application.Stream;

import software.ulpgc.kata6.application.Desktop;
import software.ulpgc.kata6.application.MovieDeserializer;
import software.ulpgc.kata6.application.RemoteStore;
import software.ulpgc.kata6.architecture.Model.Movie;
import software.ulpgc.kata6.architecture.viewmodel.Histogram;
import software.ulpgc.kata6.architecture.viewmodel.HistogramBuilder;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Desktop.create().display(histogram()).setVisible(true);
    }

    private static Histogram histogram() {
        return HistogramBuilder
                .with(movies()).title("Movies per year").x("Year").y("Count").legend("Movies").use(Movie::year);
    }

    private static Stream<Movie> movies() {
        return new RemoteStore(MovieDeserializer::fromTsv).movies().limit(1000);
    }
}
