package com.vrjulianti.moviedatabase.Fragment;

import android.database.Cursor;
import android.os.AsyncTask;
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
import com.vrjulianti.moviedatabase.Adapter.FavoriteAdapter;
import com.vrjulianti.moviedatabase.Adapter.NowplayAdapter;
import com.vrjulianti.moviedatabase.BuildConfig;
import com.vrjulianti.moviedatabase.Database.MovieContractBuilder;
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

public class FragmentFavorite extends Fragment
{
    @BindView(R.id.rv_favorite)
    RecyclerView recyclerView;

    @BindView(R.id.fave_progress)
    ProgressBar progressBar;


    private Cursor list;
    private Movie movie;

    private FavoriteAdapter moviesAdapter;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_favorite, container, false);
        init_widget();
        new loadDataAsync().execute();
        return  v;
    }

    private void init_widget()
    {
        ButterKnife.bind(this, v);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        moviesAdapter = new FavoriteAdapter(list, getActivity());
        recyclerView.setAdapter(moviesAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        new loadDataAsync().execute();
    }

    private class loadDataAsync extends AsyncTask<Void, Void, Cursor>
    {

        @Override
        protected Cursor doInBackground(Void... voids)
        {
            return getActivity().getContentResolver().query(
                    MovieContractBuilder.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        protected void onPostExecute(Cursor cursor)
        {
            super.onPostExecute(cursor);

            list = cursor;
            moviesAdapter.replaceAll(list);
        }
    }



}
