package com.example.film_app_joona_manninen.data_classes;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;


public class Entry {

    private LocalDate date;
    private String comment;
    private float stars;
    private String title;

    @RequiresApi(api = Build.VERSION_CODES.O)

    public Entry(){}

    public Entry(String title, String comment, float stars, LocalDate date){
        this.date = date;
        this.comment = comment;
        this.stars = stars;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public float getStars() {
        return stars;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

}
