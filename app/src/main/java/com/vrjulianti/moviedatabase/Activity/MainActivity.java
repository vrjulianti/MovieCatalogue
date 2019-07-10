package com.vrjulianti.moviedatabase.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.vrjulianti.moviedatabase.Adapter.FragmentAdapter;
import com.vrjulianti.moviedatabase.Fragment.FragmentFavorite;
import com.vrjulianti.moviedatabase.Fragment.FragmentNow;
import com.vrjulianti.moviedatabase.Fragment.FragmentUpcoming;
import com.vrjulianti.moviedatabase.R;
import com.vrjulianti.moviedatabase.Reminder.DailyReceiver;
import com.vrjulianti.moviedatabase.Reminder.ReleaseTodayReceiver;
import com.vrjulianti.moviedatabase.SettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MaterialSearchBar.OnSearchActionListener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewPager_movie)
    ViewPager viewPager;

    @BindView(R.id.layout_tab)
    TabLayout tab;

    @BindView(R.id.searchBar)
    MaterialSearchBar searchBar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    ActionBarDrawerToggle toggle;
    public static String judul;

    private DailyReceiver dailyReceiver;
    private ReleaseTodayReceiver releaseReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        searchBar.setOnSearchActionListener(this);
        searchBar.setNavButtonEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

        initFragment();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dailyReceiver = new DailyReceiver();
        releaseReceiver = new ReleaseTodayReceiver();

    }

    private void initFragment() {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragment(new FragmentNow(), getString(R.string.now_tab));
        fragmentAdapter.addFragment(new FragmentUpcoming(), getString(R.string.up_tab));
        fragmentAdapter.addFragment(new FragmentFavorite(), getString(R.string.fave_tab));

        viewPager.setAdapter(fragmentAdapter);
        tab.setupWithViewPager(viewPager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.removeDrawerListener(toggle);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        judul = String.valueOf(text);
        startActivity(intent);

    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onButtonClicked(int buttonCode) {

        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawerLayout.addDrawerListener(toggle);
                Log.d("checkNavBar", "onResume: NavBar bisa");
                toggle.syncState();
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_lang) {
            Intent localeIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(localeIntent);
        }

        else if( id == R.id.setting){

            Intent intent  = new Intent(this,SettingActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
