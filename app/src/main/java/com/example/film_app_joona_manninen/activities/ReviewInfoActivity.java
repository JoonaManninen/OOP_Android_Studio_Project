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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.film_app_joona_manninen.FilmManager;
import com.example.film_app_joona_manninen.R;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReviewInfoActivity extends AppCompatActivity {

    private FilmManager fm = new FilmManager();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_info);

        TextView titleReviewTextView = findViewById(R.id.titleReviewTextView);
        RatingBar reviewRatingBar = findViewById(R.id.reviewRatingBar);
        TextView commentTextView = findViewById(R.id.commentTextView5);
        TextView dateTextView = findViewById(R.id.dateTextView6);
        Button deleteEntryBtn = findViewById(R.id.deleteEntryBtn);

        Intent in = getIntent();
        int index = in.getIntExtra("com.example.INDEX1", -1);

        // Setting title
        titleReviewTextView.setText(fm.getTitle(index));
        // Setting rating
        reviewRatingBar.setRating(fm.getRating(index));
        // Setting comment from review
        commentTextView.setText(fm.getComment(index));
        // Setting date
        dateTextView.setText(getResources().getString(R.string.reviewDate) + fm.getDate(index));

        deleteEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Deleting users review
                fm.deleteEntry(index, fm.getTitle(index), getApplicationContext());

                // When user presses the back button application needs to showcase new list without deleted Entry
                Intent ReviewListActivity = new Intent(getApplicationContext(), ReviewListActivity.class);
                // Getting updated titles of the reviews so we can make listview and sending it to ReviewListActivity
                ArrayList<String> reviewList = fm.ReviewTitleArrayList();
                ReviewListActivity.putExtra("com.example.REVIEW_ARRAY_LIST", reviewList);

                startActivity(ReviewListActivity);
            }
        });
    }
}