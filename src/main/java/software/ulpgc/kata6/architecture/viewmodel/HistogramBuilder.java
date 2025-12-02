package software.ulpgc.kata6.architecture.viewmodel;

import software.ulpgc.kata6.architecture.Model.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class HistogramBuilder {

    private final Map<String, String> labels;
    private final Stream<Movie> movies;

    private HistogramBuilder(Stream<Movie> movies) {
        this.labels = new HashMap<>();
        this.movies = movies.filter(movie -> movie.year() >= 1900).filter(movie -> movie.year() <= 2000);
    }

    public static HistogramBuilder with(Stream<Movie> movies) {
        if (movies == null) throw new IllegalArgumentException("Movies not found.");
        return new HistogramBuilder(movies);
    }

    public HistogramBuilder title(String label) {
        labels.put("title", label);
        return this;
    }

    public HistogramBuilder x(String label) {
        labels.put("x", label);
        return this;
    }

    public HistogramBuilder y(String label) {
        labels.put("y", label);
        return this;
    }

    public HistogramBuilder legend(String label) {
        labels.put("legend", label);
        return this;
    }

    public Histogram use(Function<Movie, Integer> binarize) {
        Histogram histogram = new Histogram(labels);
        movies.map(binarize).forEach(histogram::add);
        return histogram;
    }
}
