package com.example.film_app_joona_manninen.data_classes;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

public class Film {
    private String title;
    private String description;
    private String productionYear;
    private String length;
    private String genres;

    public Film(String title1, String description1, String productionYear1, String length1, String genres1){
        this.title = title1;
        this.description = description1;
        this.productionYear = productionYear1;
        this.length = length1;
        this.genres = genres1;
    }

    public Film(){}

    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        return genres;
    }

}
