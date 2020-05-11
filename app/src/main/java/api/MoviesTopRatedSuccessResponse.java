package api;

import java.util.ArrayList;

public class MoviesTopRatedSuccessResponse {
    private int page;
    private ArrayList<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getMovies() {
        return results;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.results = movies;
    }
}
