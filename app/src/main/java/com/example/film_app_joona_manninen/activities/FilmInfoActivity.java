package com.example.film_app_joona_manninen.activities;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.film_app_joona_manninen.FilmList;
import com.example.film_app_joona_manninen.R;

public class FilmInfoActivity extends AppCompatActivity {

    // FilmList object so we can get the film info
    private final FilmList fl = new FilmList();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_info);

        // All TextViews which show film data
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView productionTextView = (TextView) findViewById(R.id.productionTextView);
        TextView lengthTextView = (TextView) findViewById(R.id.lengthTextView);
        TextView genreTextView = (TextView) findViewById(R.id.genreTextView);
        TextView descTextView = (TextView) findViewById(R.id.descTextView);
        Button makeReviewBtn = (Button) findViewById(R.id.makeReviewBtn);

        Intent in = getIntent();
        int index = in.getIntExtra("com.example.INDEX",-1);

        // Using strings to implement language change
        String productionYear = getResources().getString(R.string.productionYear);
        String strLength = getResources().getString(R.string.movieLength);
        String strGenres = getResources().getString(R.string.genres);
        // Setting TextViews to show film info
        titleTextView.setText(fl.getTitle(index));
        productionTextView.setText(productionYear + fl.getProductionyear(index));
        lengthTextView.setText(strLength + fl.getLength(index) + "min");
        genreTextView.setText(strGenres + fl.getGenres(index));
        descTextView.setText(fl.getDesc(index));

        // OnClickListener for review button so we can get to ReviewActivity
        makeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String title = fl.getTitle(index);
                // Sending desired film title to the review activity where user makes review.
                Intent ReviewActivity = new Intent(getApplicationContext(), ReviewActivity.class);
                ReviewActivity.putExtra("com.example.TITLE",title);

                startActivity(ReviewActivity);


            }
        });

    }
}