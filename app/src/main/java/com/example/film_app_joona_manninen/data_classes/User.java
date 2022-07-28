package com.example.film_app_joona_manninen.data_classes;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

// This class is for the user credentials and way to access them.

public class User {

    public String username , salt, password;

    public User(){}

    public User(String username, String strPsw, String strSalt){
        this.username = username;
        this.password = strPsw;
        this.salt = strSalt;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getUsername() {
        return username;
    }
}
