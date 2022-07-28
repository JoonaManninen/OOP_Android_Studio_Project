package com.example.film_app_joona_manninen;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

import android.content.Context;
import android.util.Log;

import com.example.film_app_joona_manninen.data_classes.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// In this class we will parse the xml-data, save data to JSON file and make different data structures from the data to make further use.
public class FilmList {

    // object to access Film class
    Film filmObject = (Film) new Film();
    // ArrayList where film info is added from xml file
    private static final ArrayList<Film> filmArrayList = new ArrayList<>();
    // Arraylist where all the films are added from saved data and xml data
    private static final ArrayList<Film> allFilmsArrayList = new ArrayList<>();
    // JSONArray which contains all the film JSON objects
    private static JSONArray detailsJsonArray = new JSONArray();
    // HashMap where film titles are stored to check in case of copies.
    private static HashMap<String, String> filmHashMap = new HashMap<String, String>();


    public FilmList() {
    }

    public FilmList(Context c) {
        // making JSONArray from saved film data
        getJSONArray(c);
        //  Making HashMap from film titles
        MakeFilmHashMap();
        // Making Film object arraylist
        makeFilmObjectList();

    }

    // Public Methods
    // Parsing film data from Finnkino API data
    public void filmXmlParser(String URL, Context c) {


        DocumentBuilder builder = null;

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(URL);

            // Making nodelist where all the xml elements tagged event are stored.
            NodeList nList = doc.getDocumentElement().getElementsByTagName("Event");

            // We go through nodelist and get the data we need from the list
            for (int i = 0; i < nList.getLength(); i++) {

                // making node from the item in the index i
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // Making film object from all the data we want from the xml file.
                    Film film = new Film((element.getElementsByTagName("Title").item(0).getTextContent()),
                            (element.getElementsByTagName("ShortSynopsis").item(0).getTextContent()),
                            (element.getElementsByTagName("ProductionYear").item(0).getTextContent()),
                            (element.getElementsByTagName("LengthInMinutes").item(0).getTextContent()),
                            (element.getElementsByTagName("Genres").item(0).getTextContent()));
                    filmArrayList.add(film);
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        // making the JSON file where films are stored
        makeJSON(c);

        return;
    }

    public ArrayList<String> TitleArrayList(){

        ArrayList<String> titleArrayList = new ArrayList<>();
        // Clearing the titleArrayList before use so it's guaranteed to be empty
        titleArrayList.removeAll(titleArrayList);
        System.out.println(allFilmsArrayList.size());

        for (int i = 0; i < allFilmsArrayList.size(); i++){
            filmObject = allFilmsArrayList.get(i);
            titleArrayList.add(filmObject.getTitle());
            System.out.println(filmObject.getTitle());
        }
        System.out.println(titleArrayList.size());
        return titleArrayList;
    }

    // Returns Film title from the desired Film object
    public String getTitle(int index) {

        filmObject = allFilmsArrayList.get(index);

        return filmObject.getTitle();
    }

    // Returns Film production year from the desired Film object
    public String getProductionyear(int index) {

        filmObject = allFilmsArrayList.get(index);

        return filmObject.getProductionYear();
    }

    // Returns Film length (minutes) from the desired Film object
    public String getLength(int index) {

        filmObject = allFilmsArrayList.get(index);

        return filmObject.getLength();
    }

    // Returns Film genres from the desired Film object
    public String getGenres(int index) {

        filmObject = allFilmsArrayList.get(index);

        return filmObject.getGenres();
    }

    // Returns Film description from the desired Film object
    public String getDesc(int index) {

        filmObject = allFilmsArrayList.get(index);

        return filmObject.getDescription();
    }

    // Private methods
    // Making JSON file where film data is stored
    private void makeJSON(Context c) {

        // Setting all the needed variables.
        File file = new File(c.getFilesDir(), "films.json");
        JSONArray jsonArray = new JSONArray();

        // Integer to message latter part of the method if writing the file is necessary.
        int check = 0;

        for (int i = 0; i < filmArrayList.size(); i++) {

            // Getting different instances of the filmObject
            filmObject = filmArrayList.get(i);

            if (filmHashMap.containsKey(filmObject.getTitle())) {
            } else {
                try {
                    JSONObject jsonObject = new JSONObject();
                    // Adding the info to jsonObject
                    jsonObject.put("Title", filmObject.getTitle());
                    jsonObject.put("Description", filmObject.getDescription());
                    jsonObject.put("ProductionYear", filmObject.getProductionYear());
                    jsonObject.put("Length", filmObject.getLength());
                    jsonObject.put("Genres", filmObject.getGenres());
                    // Putting JsonObjects to JSONArray
                    jsonArray.put(jsonObject);
                    // Adding the new object to the object arraylist consisting all films
                    allFilmsArrayList.add(filmObject);
                    check = 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (check != 0) {
            try {
                // Append the new array to the end of old one to get evey film added to the JSON file
                for (int k = 0; k < jsonArray.length(); k++) {
                    detailsJsonArray.put(jsonArray.getJSONObject(k));
                }
                String json = detailsJsonArray.toString(2);
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(json);
                bufferedWriter.close();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    // With this method I can read the JSON file which is saved from previous uses of the app and can compare it to new xml list and see if there is copies.
    private void MakeFilmHashMap() {

        // Making HashMap of the film titles to check if new titles are already in the JSON file.
        HashMap<String, String> jsonHashMap = new HashMap<String, String>();

        if (detailsJsonArray != null) {
            try {
                for (int i = 0; i < detailsJsonArray.length(); i++) {
                    JSONObject jsonObject = detailsJsonArray.getJSONObject(i);
                    filmHashMap.put(jsonObject.getString("Title"), "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    // Method that gets the already saved films and returns them as JSONArray
    private void getJSONArray(Context c) {

        String json = getJSON(c);
        // Clearing the detailsArrayList before use so it's guaranteed to be empty
        for (int i = 0; i < detailsJsonArray.length(); i++) {
            detailsJsonArray.remove(i);
        }
        if (json != null) {
            try {
                detailsJsonArray = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    // Method which gets all the saved Films to arraylist full of Film objects.
    private void makeFilmObjectList() {

        // Clearing the allFilmsArrayList before use so it's guaranteed to be empty
        allFilmsArrayList.removeAll(allFilmsArrayList);

        for (int i = 0; i < detailsJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = detailsJsonArray.getJSONObject(i);
                Film film = new Film(jsonObject.getString("Title"), jsonObject.getString("Description"),
                        jsonObject.getString("ProductionYear"), jsonObject.getString("Length"),
                        jsonObject.getString("Genres"));
                allFilmsArrayList.add(film);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    // Method which gets saved films from JSON file and returns them as string to be parsed on other methods.
    private String getJSON(Context context) {

        String json = null;

        try {

            InputStream inputStream = context.openFileInput("films.json");

            if (inputStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String stringLine = null;
                while ((stringLine = br.readLine()) != null) {
                    sb.append(stringLine).append("\n");
                }
                json = sb.toString();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("", "Can not read file: " + e.toString());
        }
        return json;
    }

}