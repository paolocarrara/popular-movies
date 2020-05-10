package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView moviesRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] dataSet = {"Comida", "Fresca"};

        moviesRecyclerView = (RecyclerView) findViewById(R.id.movies_rv);

        layoutManager = new GridLayoutManager(this, 2);

        moviesAdapter = new MoviesAdapter(dataSet);

        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setAdapter(moviesAdapter);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView view;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            view = (TextView) itemView;
        }

        public void setText (String text) {
            this.view.setText(text);
        }
    }

    class MoviesAdapter extends RecyclerView.Adapter<MainActivity.MovieViewHolder> {
        String[] dataSet;

        public MoviesAdapter (String[] _dataSet) {
            dataSet = _dataSet;
        }

        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView view = new TextView(parent.getContext());
            MovieViewHolder viewHolder = new MovieViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            holder.setText(dataSet[position]);
        }

        @Override
        public int getItemCount() {
            return dataSet.length;
        }
    }
}
