package com.vrjulianti.moviedatabase.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.vrjulianti.moviedatabase.API.APIMovie;
import com.vrjulianti.moviedatabase.BuildConfig;
import com.vrjulianti.moviedatabase.Model.Movie;
import com.vrjulianti.moviedatabase.Model.Responses;
import com.vrjulianti.moviedatabase.R;
import com.vrjulianti.moviedatabase.Reminder.DailyReceiver;
import com.vrjulianti.moviedatabase.Reminder.ReleaseTodayReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSetting extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String LANGUAGE = "en-us";
    private int currentPage = 1;
    private String region = null;

    private APIMovie apiClient = null;
    private Retrofit retrofit = null;
    private Call<Responses> call;

    private List<Movie> nowPlayMovies = new ArrayList<>();

    private DailyReceiver dailyAlarmReceiver = new DailyReceiver();
    private ReleaseTodayReceiver releaseTodayReminder = new ReleaseTodayReceiver();

    @BindString(R.string.key_daily_reminder)
    String keyDailyReminder;

    @BindString(R.string.key_release_reminder)
    String keyReleaseReminder;

    @BindString(R.string.key_setting_language)
    String keySettingLanguage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        ButterKnife.bind(this, getActivity());

        SwitchPreference switchDailyReminder = (SwitchPreference) findPreference(keyDailyReminder);
        switchDailyReminder.setOnPreferenceChangeListener(this);
        SwitchPreference switchUpcomingReminder = (SwitchPreference) findPreference(keyReleaseReminder);
        switchUpcomingReminder.setOnPreferenceChangeListener(this);
        findPreference(keySettingLanguage).setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        boolean isSet = (boolean) newValue;

        if (key.equals(keyDailyReminder)) {
            if (isSet) {
                dailyAlarmReceiver.setRepeatingAlarm(getActivity());
            } else {
                dailyAlarmReceiver.cancelAlarm(getActivity());
            }
        } else {
            if (isSet) {
                getTodayRelease();
            } else {
                releaseTodayReminder.cancelAlarm(getActivity());
            }
        }

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(keySettingLanguage)) {
            Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(languageIntent);
        }
        return true;
    }

    private void getTodayRelease() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String currentDate = sdf.format(date);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        APIMovie movieApiService = retrofit.create(APIMovie.class);
        call = movieApiService.getUpcomingMovie(BuildConfig.ApiKey, currentPage, "US");
        call.enqueue(new Callback<Responses>() {

            @Override
            public void onResponse(Call<Responses> call, Response<Responses> response) {

                if (response.isSuccessful()) {
                    List<Movie> todayMovie = new ArrayList<>();
                    nowPlayMovies = response.body().getResults();
                    for (int i = 0; i < nowPlayMovies.size(); i++) {

                        Movie movie = nowPlayMovies.get(i);
                        Date movieDate = dateFormatter(movie.getReleaseDate());

                        if (movieDate.equals(dateFormatter(currentDate))) {
                            todayMovie.add(movie);
                        }

                    }
                    releaseTodayReminder.setRepeatingAlarm(getActivity(), todayMovie);
                } else {
                    Toast.makeText(getActivity(), "gagal menambahkan Alarm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Responses> call, Throwable t) {

                Toast.makeText(getActivity(), "gagal menambahkan Alarm", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Date dateFormatter(String movieDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(movieDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
