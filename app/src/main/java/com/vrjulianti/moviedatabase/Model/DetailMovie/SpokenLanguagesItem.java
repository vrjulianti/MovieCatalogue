package com.vrjulianti.moviedatabase.Model.DetailMovie;

/*
 * Created by omrobbie.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 9/27/17 9:25 AM.
 */

import com.google.gson.annotations.SerializedName;

public class SpokenLanguagesItem
{

    @SerializedName("name")
    private String name;

    @SerializedName("iso_639_1")
    private String iso6391;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

}