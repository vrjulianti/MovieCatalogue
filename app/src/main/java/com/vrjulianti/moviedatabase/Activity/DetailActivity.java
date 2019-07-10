package com.vrjulianti.moviedatabase.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vrjulianti.moviedatabase.API.APIMovie;
import com.vrjulianti.moviedatabase.BuildConfig;
import com.vrjulianti.moviedatabase.Database.FaveBuilder;
import com.vrjulianti.moviedatabase.Database.MovieContractBuilder;
import com.vrjulianti.moviedatabase.Model.DetailMovie.DetailMovie;
import com.vrjulianti.moviedatabase.Model.Movie;
import com.vrjulianti.moviedatabase.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.CONTENT_URI;
import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_BACKDROP_PATH;
import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_ID;
import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_OVERVIEW;
import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_POSTER_PATH;
import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_RELEASE_DATE;
import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_TITLE;
import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_VOTE_AVERAGE;
import static com.vrjulianti.moviedatabase.Database.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_VOTE_COUNT;

public class DetailActivity extends AppCompatActivity {

    public static String MOVIE_ITEM = "movie_item";
    private Retrofit retrofit;
    private DetailMovie detailMovie;
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private final static String API_KEY = BuildConfig.ApiKey;
    private Gson gson = new Gson();

    @BindView(R.id.imagePoster)
    ImageView posterImage;

    @BindView(R.id.movie_title)
    TextView movieTitle;

    @BindView(R.id.movie_dates)
    TextView datesMovie;

    @BindView(R.id.overview)
    TextView movieDesc;

    @BindView(R.id.movie_popularity)
    TextView popularity;

    @BindView(R.id.button_fave)
    ImageButton faveButton;

    private Boolean isFavorite = false;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String movie_item = getIntent().getStringExtra(MOVIE_ITEM);
        movie =  gson.fromJson(movie_item, Movie.class);
        String id_movie = String.valueOf(movie.getId());
        getData(id_movie);
        loadDataSQLite(movie);
    }


    private void getData(String id)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        APIMovie apiMovie = retrofit.create(APIMovie.class);
        Call<DetailMovie> call = apiMovie.getDetailMovie(id, API_KEY);
        call.enqueue(new Callback<DetailMovie>() {
            @Override
            public void onResponse(Call<DetailMovie> call, Response<DetailMovie> response)
            {
                detailMovie = response.body();
                syncData();

            }

            @Override
            public void onFailure(Call<DetailMovie> call, Throwable t) {

            }
        });
    }
    private void loadDataSQLite(Movie movie)
    {
        FaveBuilder faveHelper = new FaveBuilder(this);
        faveHelper.open();

        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + movie.getId()),
                null,
                null,
                null,
                null
        );

        if (cursor != null)
        {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }
        setFaveIcon();

        faveHelper.close();
    }

    public void setFaveIcon()
    {
        if (isFavorite)
        {
            faveButton.setImageResource(R.drawable.ic_star_black_24dp);
        } else
        {
            faveButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }


    private void FaveSave()
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MOVIE_ID, movie.getId());
        cv.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        cv.put(COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        cv.put(COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        cv.put(COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        cv.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        cv.put(COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());

        getContentResolver().insert(CONTENT_URI, cv);
        Toast.makeText(this, R.string.toast_add_fav, Toast.LENGTH_SHORT).show();
    }

    private void FaveRemove()
    {
        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + movie.getId()),
                null,
                null
        );
        Toast.makeText(this, R.string.toast_remove_fav, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_fave)
    public void Click()
    {
        if (isFavorite)
        {
            FaveRemove();
        } else
        {

            FaveSave();
        }
        isFavorite = !isFavorite;
        setFaveIcon();
    }

    private void syncData()
    {
        Glide.with(this)
                .load(BuildConfig.BASE_URL_IMG + "/w185//" + detailMovie.getPosterPath())
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(posterImage);

        movieTitle.setText(detailMovie.getTitle());

        StringBuilder genres = new StringBuilder();
        int size = detailMovie.getGenres().size();
        for (int i = 0; i < size; i++)
        {
            genres.append("✸ ").append(detailMovie.getGenres().get(i).getName()).append(i + 1 < size ? "\n" : "");
        }

        movieDesc.setText(detailMovie.getOverview());
        popularity.setText(String.valueOf(detailMovie.getPopularity()));
        datesMovie.setText(detailMovie.getReleaseDate());
    }
}