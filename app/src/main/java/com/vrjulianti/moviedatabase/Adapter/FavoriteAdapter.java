package com.vrjulianti.moviedatabase.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FaveViewHolder>
{
    private Cursor movieList;
    private Context context;

    public FavoriteAdapter(Cursor items, Context context)
    {
        this.context = context;
        replaceAll(items);
    }

    public void replaceAll(Cursor items)
    {
        movieList = items;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_favorite, parent, false);
        return new FaveViewHolder(view);
    }

    public Movie getMovie(int position)
    {
        if (!movieList.moveToPosition(position))
            throw new IllegalStateException("Position Invalid");
        return new Movie(movieList);
    }

    @Override
    public void onBindViewHolder(@NonNull FaveViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        final Movie movies = getMovie(position);

        String image_url = BuildConfig.BASE_URL_IMG + "/w342//" + movies.getPosterPath();

        Glide.with(context)
                .load(image_url)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(holder.moviePoster);

        holder.movieTitle.setText(movies.getTitle());
        holder.dates.setText(DateTime.getLongDate(movies.getReleaseDate()));
        holder.movieDesc.setText(movies.getOverview());

        holder.detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE_ITEM, new Gson().toJson(movies));
                context.startActivity(intent);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE_ITEM, new Gson().toJson(movies));
                context.startActivity(intent);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, movies.getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, movies.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, movies.getTitle() + "\n\n" + movies.getOverview());
                context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share)));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if (movieList == null) return 0;

        return movieList.getCount();
    }

    class FaveViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.fave_moviePoster)
        ImageView moviePoster;

        @BindView(R.id.fave_movieTitle)
        TextView movieTitle;

        @BindView(R.id.fave_movieDates)
        TextView dates;

        @BindView(R.id.fave_movieDesc)
        TextView movieDesc;

        @BindView(R.id.btn_fave_detail)
        Button detail;

        @BindView(R.id.btn_fave_share)
        Button share;

        @BindView(R.id.card_favorite)
        CardView cardView;

        FaveViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}