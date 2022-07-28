package com.example.film_app_joona_manninen.activities;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.film_app_joona_manninen.FilmManager;
import com.example.film_app_joona_manninen.R;

import java.time.LocalDate;

// This activity is for making reviews of the chosen movie.

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText reviewEditText;
    private FilmManager fm = new FilmManager();
    protected LocalDate date;
    private int checkDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        TextView reviewTitleTextView = (TextView) findViewById(R.id.titleReviewTextView);
        ratingBar = (RatingBar) findViewById(R.id.reviewRatingBar);
        reviewEditText = (EditText) findViewById(R.id.reviewEditText);
        Button saveEntryBtn = (Button) findViewById(R.id.deleteEntryBtn);
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        // Setting the movie title textview
        Intent in = getIntent();
        String title = in.getStringExtra("com.example.TITLE");
        reviewTitleTextView.setText(title);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Making if statement to separate when month is 10 digits or 1 digit to get it parsed correctly.
                checkDate ++;
                if (month < 10) {
                    if (day < 10){
                        String dateCal = year + "-0" + (month + 1) + "-0" + day;
                        date = LocalDate.parse(dateCal);
                    } else{
                        String dateCal = year + "-0" + (month + 1) + "-" + day;
                        date = LocalDate.parse(dateCal);
                    }
                } else{
                    if (day < 10) {
                        String dateCal = year + "-" + (month + 1) + "-0" + day;
                        date = LocalDate.parse(dateCal);
                    } else {
                        String dateCal = year + "-" + (month + 1) + "-" + day;
                        date = LocalDate.parse(dateCal);
                    }
                }
                }
            });
        saveEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When user pushes the button users review is saved as Entry object and in JSON file.
                if (checkDate == 0){
                    date = LocalDate.now();
                }
                String comment = "-";
                if (reviewEditText.getText().toString() != null) {
                    comment = reviewEditText.getText().toString();
                }
                float stars = ratingBar.getRating();
                boolean check = fm.saveEntry(title, comment, stars, date, getApplicationContext());
                if (check) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.reviewSaved), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.alreadyReviewed), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}