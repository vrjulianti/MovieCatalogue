package com.vrjulianti.moviedatabase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vrjulianti.moviedatabase.Activity.DetailActivity;
import com.vrjulianti.moviedatabase.BuildConfig;
import com.vrjulianti.moviedatabase.Model.Movie;
import com.vrjulianti.moviedatabase.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";


    private List<Movie> movieList = new ArrayList<>();
    private static Movie movie;
    private Context mContext;

    public static Movie getMovie(){
        return movie;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovie(List<Movie> movieList){
        this.movieList = movieList;
    }

    public static void setMovie(Movie movie) {
        UpcomingAdapter.movie = movie;
    }

    public UpcomingAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.movieList = movies;
    }

    @Override
    public UpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_upcoming, parent, false);
        return new UpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UpViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String image_url = BuildConfig.BASE_URL_IMG + "/w342//" + movieList.get(position).getPosterPath();
        //Movie movie = mListMovie.get(position);

        Glide.with(holder.itemView.getContext())
                .load(image_url)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(holder.moviePoster);

        holder.movieTitle.setText(movieList.get(position).getTitle());

        holder.movieDesc.setText(movieList.get(position).getOverview());

        holder.dates.setText(movieList.get(position).getReleaseDate());

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + movieList.get(position));

                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE_ITEM, new Gson().toJson(movieList.get(position)));
                holder.itemView.getContext().startActivity(intent);

            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, movieList.get(position).getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, movieList.get(position).getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, movieList.get(position).getTitle() + "\n\n" + movieList.get(position).getOverview());
                holder.itemView.getContext().startActivity(Intent.createChooser(intent, holder.itemView.getContext().getResources().getString(R.string.share)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    static class UpViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.up_moviePoster)
        ImageView moviePoster;

        @BindView(R.id.up_movieTitle)
        TextView movieTitle;

        @BindView(R.id.up_movieDates)
        TextView dates;

        @BindView(R.id.up_movieDesc)
        TextView movieDesc;

        @BindView(R.id.btn_up_detail)
        Button detail;

        @BindView(R.id.btn_up_share)
        Button share;


        UpViewHolder(View v)
        {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}