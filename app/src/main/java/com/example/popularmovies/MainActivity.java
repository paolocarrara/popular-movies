package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import api.Movie;
import api.MoviesClient;
import api.MoviesPopularSuccessResponse;
import api.MoviesTopRatedSuccessResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView moviesRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;
    private MoviesAdapter moviesAdapter;
    private MoviesClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new MoviesClient();

        moviesRecyclerView = (RecyclerView) findViewById(R.id.movies_rv);
        progressBar = (ProgressBar) findViewById(R.id.progress_loader);
        layoutManager = new GridLayoutManager(this, 2);

        ArrayList<Movie> a = new ArrayList<Movie>();
        moviesAdapter = new MoviesAdapter(a);

        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setAdapter(moviesAdapter);

        getPopularMovies();
    }

    private void getPopularMovies () {
        moviesRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if (!isNetworkAvailable(getApplicationContext())) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "No internet access", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<MoviesPopularSuccessResponse> call = client.popular();

        call.enqueue(new Callback<MoviesPopularSuccessResponse>() {
            @Override
            public void onResponse(Call<MoviesPopularSuccessResponse> call, Response<MoviesPopularSuccessResponse> response) {
                MoviesPopularSuccessResponse responseBody = response.body();

                if (responseBody != null) {
                    progressBar.setVisibility(View.GONE);
                    moviesRecyclerView.setVisibility(View.VISIBLE);
                    moviesRecyclerView.setAdapter(new MoviesAdapter(responseBody.getMovies()));
                }
            }

            @Override
            public void onFailure(Call<MoviesPopularSuccessResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error loading the data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTopRatedMovies () {
        moviesRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if (!isNetworkAvailable(getApplicationContext())) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "No internet access", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<MoviesTopRatedSuccessResponse> call = client.topRated();

        call.enqueue(new Callback<MoviesTopRatedSuccessResponse>() {
            @Override
            public void onResponse(Call<MoviesTopRatedSuccessResponse> call, Response<MoviesTopRatedSuccessResponse> response) {
                MoviesTopRatedSuccessResponse responseBody = response.body();

                if (responseBody != null) {
                    progressBar.setVisibility(View.GONE);
                    moviesRecyclerView.setVisibility(View.VISIBLE);
                    moviesRecyclerView.setAdapter(new MoviesAdapter(responseBody.getMovies()));
                }
            }

            @Override
            public void onFailure(Call<MoviesTopRatedSuccessResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error loading the data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private Movie movie;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            view = (View) itemView;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra("movieId", movie.getId());
                    context.startActivity(intent);
                }
            });
        }

        public void setMovie (Movie _movie) {
            movie = _movie;
        }

        public void build () {
            setImage(movie.getPoster_path());
        }

        public void setImage (String posterPath) {
            ImageView iv = view.findViewById(R.id.grid_item_iv);
            Picasso.get().load("https://image.tmdb.org/t/p/w342" + posterPath).into(iv);
        }
    }

    class MoviesAdapter extends RecyclerView.Adapter<MainActivity.MovieViewHolder> {
        ArrayList<Movie> dataSet;

        public MoviesAdapter (ArrayList<Movie> _dataSet) {
            dataSet = _dataSet;
        }

        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

            return new MovieViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            Movie movie = dataSet.get(position);

            holder.setMovie(movie);
            holder.build();
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_popular) {
            getPopularMovies();

            return true;
        } else if (item.getItemId() == R.id.sort_by_top_rated) {
            getTopRatedMovies();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                final Network network = connectivityManager.getActiveNetwork();

                if (network != null) {
                    final NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(network);

                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
                }
            } else {
                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

                for (NetworkInfo tempNetworkInfo : networkInfos) {
                    if (tempNetworkInfo.isConnected()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
