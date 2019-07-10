package com.vrjulianti.moviedatabase.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Responses {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> results = new ArrayList<>();

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("dates")
    private Dates date;

    public Responses()
    {

    }

    public Dates getDate()
    {
        return date;
    }

    public void setDate(Dates date)
    {
        this.date = date;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public List<Movie> getResults()
    {
        return results;
    }

    public void setResults(List<Movie> results)
    {
        this.results = results;
    }

    public int getTotalResults()
    {
        return totalResults;
    }

    public void setTotalResults(int totalResults)
    {
        this.totalResults = totalResults;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

}
