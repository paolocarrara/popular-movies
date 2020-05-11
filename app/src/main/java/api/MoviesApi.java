package api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {
    @GET("3/movie/{movie_id}")
    Call<MovieSuccessResponse> movie(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("3/movie/popular")
    Call<MoviesPopularSuccessResponse> popular(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<MoviesTopRatedSuccessResponse> topRated(@Query("api_key") String apiKey);
}
