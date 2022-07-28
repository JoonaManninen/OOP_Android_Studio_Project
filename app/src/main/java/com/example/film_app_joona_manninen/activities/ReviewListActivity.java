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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.film_app_joona_manninen.R;
import com.example.film_app_joona_manninen.adapters.ReviewMainAdapter;

import java.util.ArrayList;

public class ReviewListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        getSupportActionBar().hide();

        listView = findViewById(R.id.reviewListView);

        // Getting intent and getting the title arraylist from the MainActivity
        Intent in = getIntent();
        ArrayList<String> reviewTitleList = in.getStringArrayListExtra("com.example.REVIEW_ARRAY_LIST");

        // Setting up animation and MainAdapter to make the listview
        ReviewMainAdapter adapter = new ReviewMainAdapter(this, reviewTitleList.toArray(new String[0]));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent reviewInfoActivity = new Intent(getApplicationContext(), ReviewInfoActivity.class);
                // Sending index number to the next activity to get correct information from the object list.
                reviewInfoActivity.putExtra("com.example.INDEX1", i);
                startActivity(reviewInfoActivity);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

}