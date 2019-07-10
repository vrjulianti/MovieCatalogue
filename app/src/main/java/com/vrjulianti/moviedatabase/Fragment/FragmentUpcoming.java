package com.vrjulianti.moviedatabase.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vrjulianti.moviedatabase.API.APIMovie;
import com.vrjulianti.moviedatabase.Adapter.UpcomingAdapter;
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

public class FragmentUpcoming extends Fragment
{
    @BindView(R.id.rv_upcoming)
    RecyclerView recyclerView;

    @BindView(R.id.up_progress)
    ProgressBar progressBar;


    private List<Movie> movieList = new ArrayList<>();
    private Responses movieResponse = new Responses();
    private Retrofit retrofit = null;

    private UpcomingAdapter moviesAdapter;
    Call call   ;

    private int currentPage = 1;
    private int totalPages = 1;
    private boolean isStarted = false;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_upcoming, container, false);
        init_widget();
        getData();
        return  v;
    }

    private void init_widget()
    {
        ButterKnife.bind(this, v);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        moviesAdapter = new UpcomingAdapter(getContext(), movieList);
        recyclerView.setAdapter(moviesAdapter);
    }
    public void getData()
    {
        String language = "en_US";
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        APIMovie movieApiService = retrofit.create(APIMovie.class);
        call = movieApiService.getUpcomingMovie(BuildConfig.ApiKey, currentPage, "US");
        call.enqueue(new Callback<Responses>()
        {

            @Override
            public void onResponse(Call<Responses> call, Response<Responses> response) {
                if (response.isSuccessful())
                {
                    totalPages = response.body().getTotalPages();
                    movieList = response.body().getResults();
                    moviesAdapter = new UpcomingAdapter(getContext(),movieList);
                    recyclerView.setAdapter(moviesAdapter);

                } else
                {
                    Toast.makeText(getActivity(), "koneksi gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Responses> call, Throwable t) {

            }
        });
    }



}