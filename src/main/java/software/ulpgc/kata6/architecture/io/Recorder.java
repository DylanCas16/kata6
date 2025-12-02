package software.ulpgc.kata6.architecture.io;

import software.ulpgc.kata6.architecture.Model.Movie;

import java.util.stream.Stream;

public interface Recorder {
    void put(Stream<Movie> movies);
}
