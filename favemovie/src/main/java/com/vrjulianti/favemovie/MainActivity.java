package com.vrjulianti.favemovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_fave)
    RecyclerView rvFave;

    private FaveAdapter faveAdapter;
    private Cursor faveList;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(getResources().getString(R.string.favorite));
        setRv_fave();
        new LoadDataAsync().execute();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        new LoadDataAsync().execute();
    }

    private void setRv_fave()
    {
        faveAdapter = new FaveAdapter(faveList, this);
        rvFave.setLayoutManager(new LinearLayoutManager(this));
        rvFave.addItemDecoration(new DividerItemDecoration(rvFave.getContext(), DividerItemDecoration.VERTICAL));
        rvFave.setAdapter(faveAdapter);

        ItemClickSupport.addTo(rvFave).setOnItemClickListener(new ItemClickSupport.OnItemClickListener()
        {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v)
            {
                MovieItem movie = faveAdapter.getMovie(position);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, movie.getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, movie.getTitle() + "\n\n" + movie.getOverview());
                startActivity(Intent.createChooser(intent,getResources().getString(R.string.share)));

            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class LoadDataAsync extends AsyncTask<Void, Void, Cursor>
    {

        @Override
        protected Cursor doInBackground(Void... voids)
        {
            return getContentResolver().query(
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

            faveList = cursor;
            faveAdapter.replaceAll(faveList);
        }
    }

}
