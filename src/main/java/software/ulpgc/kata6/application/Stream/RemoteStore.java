package software.ulpgc.kata6.application.Stream;

import software.ulpgc.kata6.architecture.Model.Movie;
import software.ulpgc.kata6.architecture.io.Store;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class RemoteStore implements Store {
    private static final String remoteUrl = "https://datasets.imdbws.com/title.basics.tsv.gz";
    private final Function<String, Movie> deserialize;

    public RemoteStore(Function<String, Movie> deserialize) {
        this.deserialize = deserialize;
    }


    @Override
    public Stream<Movie> movies() {
        try {
            return loadFrom(new URL(remoteUrl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> loadFrom(URL url) throws IOException {
        return loadFrom(url.openConnection());
    }

    private Stream<Movie> loadFrom(URLConnection urlConnection) throws IOException {
        return loadFrom(unzip(urlConnection.getInputStream()));
    }

    private Stream<Movie> loadFrom(InputStream inputStream) {
        return loadFrom(toReader(inputStream));
    }

    private Stream<Movie> loadFrom(BufferedReader reader) {
        return reader.lines().skip(1).map(deserialize);
    }

    private BufferedReader toReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private InputStream unzip(InputStream inputStream) throws IOException {
        return new GZIPInputStream(new BufferedInputStream(inputStream));
    }
}
