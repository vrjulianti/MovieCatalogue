package com.vrjulianti.moviedatabase.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.vrjulianti.moviedatabase.API.APIMovie;
import com.vrjulianti.moviedatabase.Adapter.SearchAdapter;
import com.vrjulianti.moviedatabase.BuildConfig;
import com.vrjulianti.moviedatabase.Model.Movie;
import com.vrjulianti.moviedatabase.Model.Responses;
import com.vrjulianti.moviedatabase.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    Retrofit retrofit = null;
    SearchAdapter moviesAdapter;
    List<Movie> movieList = new ArrayList<>();
    Call<Responses> call;

    public static final String TAG = "doom";
    private String judul = null;

    @BindView(R.id.rv_search)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        judul = MainActivity.judul;
        getData(judul);
    }

    public void getData(String judul)
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        APIMovie movieApiService = retrofit.create(APIMovie.class);
        call = movieApiService.getSearchMovie(BuildConfig.ApiKey, judul);
        call.enqueue(new Callback<Responses>()
        {

            @Override
            public void onResponse(Call<Responses> call, Response<Responses> response) {
                if (response.isSuccessful())
                {
                    movieList = response.body().getResults();
                    moviesAdapter = new SearchAdapter(SearchActivity.this,movieList);
                    recyclerView.setAdapter(moviesAdapter);

                } else
                {
                    Toast.makeText(SearchActivity.this, "koneksi gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Responses> call, Throwable t) {

            }
        });
    }
}

