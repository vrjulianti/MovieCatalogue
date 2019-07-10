package com.vrjulianti.moviedatabase.API;

import com.vrjulianti.moviedatabase.Model.DetailMovie.DetailMovie;
import com.vrjulianti.moviedatabase.Model.Responses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIMovie
{
    @GET("movie/top_rated")
    Call<Responses> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/latest")
    Call<DetailMovie> getLatestMovie(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<Responses> getPopularMovie(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("search/movie")
    Call<Responses> getSearchMovie(@Query("api_key") String apiKey, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<DetailMovie> getDetailMovie(@Path("movie_id") String movie_id, @Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<Responses> getUpcomingMovie(@Query("api_key") String apiKey, @Query("page") int page , @Query("region") String region);

    @GET("movie/now_playing")
    Call<Responses> getNowPlaying(@Query("api_key") String apiKey, @Query("page") int page , @Query("region") String region);

}