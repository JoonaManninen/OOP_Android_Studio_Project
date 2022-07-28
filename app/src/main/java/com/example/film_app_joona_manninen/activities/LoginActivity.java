package com.example.film_app_joona_manninen.activities;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.film_app_joona_manninen.R;
import com.example.film_app_joona_manninen.UserManager;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private final UserManager userManager = new UserManager();

    private static int languageChecker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText2);
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText2);
        Button languageBtn = (Button) findViewById(R.id.languageButton);

        userManager.getJSONArray(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent RegisterActivity = new Intent(getApplicationContext(), com.example.film_app_joona_manninen.activities.RegisterActivity.class);
                startActivity(RegisterActivity);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // Checking if the user credentials match
                boolean bool = userManager.logIn(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                if (bool == true){
                    Intent MainActivity = new Intent(getApplicationContext(), com.example.film_app_joona_manninen.activities.MainActivity.class);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.signInGood), Toast.LENGTH_LONG).show();
                    startActivity(MainActivity);
                } else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.signInBad), Toast.LENGTH_LONG).show();
                }
            }
        });

        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking with int languageChecker which language is used at the time.
                if (languageChecker == 0) {
                    languageChecker ++;
                    setLocal(LoginActivity.this, "fi");
                    finish();
                    startActivity(getIntent());
                } else{
                    languageChecker --;
                    setLocal(LoginActivity.this, "en");
                    finish();
                    startActivity(getIntent());
                }
            }
        });

    }

    public void setLocal(Activity activity, String language){

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}