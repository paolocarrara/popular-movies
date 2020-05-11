package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import api.Movie;
import api.MovieSuccessResponse;
import api.MoviesClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetail extends AppCompatActivity {
    Movie movie;
    MoviesClient client;

    ImageView imageThumbnailImageView;
    TextView movieOriginalTitleTextView;
    TextView movieOverviewTextView;
    TextView movieReleaseDateTextView;
    TextView movieRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        client = new MoviesClient();

        Intent intent = getIntent();

        imageThumbnailImageView = findViewById(R.id.thumbnail_iv);
        movieOriginalTitleTextView = findViewById(R.id.original_title_tv);
        movieOverviewTextView = findViewById(R.id.overview_tv);
        movieReleaseDateTextView = findViewById(R.id.release_date_tv);
        movieRatingTextView = findViewById(R.id.vote_average_tv);

        if (intent != null && intent.hasExtra("movieId")) {
            int movieId = intent.getExtras().getInt("movieId");

            getMovie(movieId);
        }
    }

    private void setImage (String posterPath) {
        Picasso.get().load("https://image.tmdb.org/t/p/w342" + posterPath).into(imageThumbnailImageView);
    }

    void getMovie (int movieId) {
        Call<MovieSuccessResponse> call = client.movie(movieId);

        call.enqueue(new Callback<MovieSuccessResponse>() {
            @Override
            public void onResponse(Call<MovieSuccessResponse> call, Response<MovieSuccessResponse> response) {
                MovieSuccessResponse responseBody = response.body();
                if (responseBody != null) {
                    movie = responseBody;
                    movieOriginalTitleTextView.setText(movie.getOriginal_title());
                    movieOverviewTextView.setText(movie.getOverview());
                    movieRatingTextView.setText("Rating: " + String.valueOf(movie.getVote_average()) + "/10");
                    movieReleaseDateTextView.setText("Release date: " + movie.getRelease_date());
                    setImage(movie.getPoster_path());
                }
            }

            @Override
            public void onFailure(Call<MovieSuccessResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MovieDetail.this, "Error loading the data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
