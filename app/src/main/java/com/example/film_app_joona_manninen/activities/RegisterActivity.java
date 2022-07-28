package com.example.film_app_joona_manninen.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.film_app_joona_manninen.R;
import com.example.film_app_joona_manninen.UserManager;
import com.example.film_app_joona_manninen.data_classes.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText2, passwordEditText2;
    Button registerBtn;
    UserManager userManager = new UserManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText2 = (EditText) findViewById(R.id.usernameEditText2);
        passwordEditText2 = (EditText) findViewById(R.id.passwordEditText2);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String username = usernameEditText2.getText().toString();
                String password = passwordEditText2.getText().toString();

                boolean isStrong = userManager.checkPasswordStrength(password);

                if (isStrong == true){
                    try {
                        // Hashing and salting the password and saving them to User class
                        userManager.HashData(username, password);
                        // Writing user credentials to JSON file
                        boolean bool = userManager.WriteUserData(getApplicationContext());
                        if (bool == true){
                            Intent MainActivity = new Intent(getApplicationContext(), com.example.film_app_joona_manninen.activities.MainActivity.class);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerSuccess), Toast.LENGTH_LONG).show();
                            startActivity(MainActivity);
                        } else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerFailed), Toast.LENGTH_LONG).show();
                        }
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.signInBad), Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}