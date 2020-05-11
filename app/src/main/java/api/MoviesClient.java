package api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesClient {
    private static String apiKey = "<YOUR_API_KEY>";
    private static MoviesApi client = null;

    public MoviesClient() {
        if (client == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/")
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            client = retrofit.create(MoviesApi.class);
        }
    }

    public Call<MovieSuccessResponse> movie (int movieId) {
        return client.movie(movieId, apiKey);
    }

    public Call<MoviesPopularSuccessResponse> popular () {
        return client.popular(apiKey);
    }

    public Call<MoviesTopRatedSuccessResponse> topRated () {
        return client.topRated(apiKey);
    }
}
