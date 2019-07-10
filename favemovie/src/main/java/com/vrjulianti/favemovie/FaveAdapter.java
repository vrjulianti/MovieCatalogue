package com.vrjulianti.favemovie;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaveAdapter extends RecyclerView.Adapter<FaveAdapter.FaveHolder>
{

    private Cursor movieList;
    private Context context;

    public FaveAdapter(Cursor items, Context context)
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
    public FaveHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fave_list, viewGroup, false);
        return new FaveHolder(view);
    }

    public MovieItem getMovie(int position)
    {
        if (!movieList.moveToPosition(position))
            throw new IllegalStateException("Position Invalid");
        return new MovieItem(movieList);
    }

    @Override
    public void onBindViewHolder(@NonNull FaveHolder favHolder, int i)
    {
        final MovieItem movies = getMovie(i);

        String image_url = BuildConfig.BASE_URL_IMG + "/w342//" + movies.getPosterPath();

        Glide.with(context)
                .load(image_url)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(favHolder.moviePoster);

        favHolder.movieTitle.setText(movies.getTitle());
        favHolder.dates.setText(DateTime.getLongDate(movies.getReleaseDate()));
        favHolder.movieDesc.setText(movies.getOverview());


    }

    @Override
    public int getItemCount()
    {
        if (movieList == null) return 0;

        return movieList.getCount();
    }

    class FaveHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.fave_moviePoster)
        ImageView moviePoster;

        @BindView(R.id.fave_movieTitle)
        TextView movieTitle;

        @BindView(R.id.fave_movieDates)
        TextView dates;

        @BindView(R.id.fave_movieDesc)
        TextView movieDesc;

        public FaveHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}