package com.example.film_app_joona_manninen.activities;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.film_app_joona_manninen.adapters.MainAdapter;
import com.example.film_app_joona_manninen.R;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        getSupportActionBar().hide();

        ListView listView = findViewById(R.id.listView);

        // Getting intent and getting the title arraylist from the MainActivity
        Intent in = getIntent();
        ArrayList<String> titleList = in.getStringArrayListExtra("com.example.ARRAY_LIST");

        // Setting up animation and MainAdapter to make the listview
        MainAdapter adapter = new MainAdapter(this, titleList.toArray(new String[0]));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation1);
        listView.setAdapter(adapter);

        // OnItemListener so we can open up info of the movie user clicks.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent filmInfoActivity = new Intent(getApplicationContext(), FilmInfoActivity.class);
                // Sending index number to the next activity to get correct information from the list.
                filmInfoActivity.putExtra("com.example.INDEX", i);
                startActivity(filmInfoActivity);
            }
        });
    }
}