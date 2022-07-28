package com.example.film_app_joona_manninen.activities;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.example.film_app_joona_manninen.FilmList;
import com.example.film_app_joona_manninen.FilmManager;
import com.example.film_app_joona_manninen.R;

import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private static int check = 0;
    // Making FilmList object so we can use it to access methods
    private final FilmList filmL = new FilmList();
    // Making FilmManagerobject so we can use it to access methods
    private final FilmManager filmManager = new FilmManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting the Finnkino URL from strings.xml
        String URL = getResources().getString(R.string.url);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button browseBtn = (Button) findViewById(R.id.browseBtn);
        Button reviewBtn = (Button) findViewById(R.id.reviewBtn);
        Button logOffBtn = (Button) findViewById(R.id.logOffBtn);
        // Making sure these methods run only once when application is started so they don't mess up datastructures
        if (check == 0){

            // Setting up the FilmList class and it's needed datastructures
            FilmList fl = new FilmList(getApplicationContext());
            // Getting new films from Finnkino API and saving them to JSON file
            filmL.filmXmlParser(URL, getApplicationContext());
            // Setting up the FilmManager class and it's needed datastructures
            FilmManager fm = new FilmManager(getApplicationContext());

            check = check + 1;
        }
        logOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        // Setting OnClickListener to the button which opens every movie shown in Finnkino
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ListViewActivity = new Intent(getApplicationContext(), com.example.film_app_joona_manninen.activities.ListViewActivity.class);
                // Getting all titles of the movies so we can make listview and sending it to ListViewActivity
                ArrayList<String> titleList = filmL.TitleArrayList();
                ListViewActivity.putExtra("com.example.ARRAY_LIST", titleList);
                startActivity(ListViewActivity);

            }
        });
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ReviewListActivity = new Intent(getApplicationContext(), com.example.film_app_joona_manninen.activities.ReviewListActivity.class);
                // Getting all titles of the reviews so we can make listview and sending it to ReviewListActivity
                ArrayList<String> reviewList = filmManager.ReviewTitleArrayList();
                ReviewListActivity.putExtra("com.example.REVIEW_ARRAY_LIST", reviewList);

                startActivity(ReviewListActivity);

            }
        });
    }
    // Method which keeps user on the MainActivity if he presses back button.
    @Override
    public void onBackPressed() {}

}
