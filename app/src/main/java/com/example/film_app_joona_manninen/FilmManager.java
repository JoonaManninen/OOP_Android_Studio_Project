package com.example.film_app_joona_manninen;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

// This class is responsible for saving reviews user makes.

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.film_app_joona_manninen.data_classes.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FilmManager {

    // These variables are static so they are not cleared when activities are changed and we can use data from them in other activities.
    private static ArrayList<Entry> allEntryObjects = new ArrayList<>();
    private static JSONArray entriesJSON = new JSONArray();
    private static HashMap<String, String> entriesHashMap = new HashMap<String, String>();

    Entry entryObject = new Entry();

    public FilmManager(){}

    public FilmManager(Context c){
        // Making JSONArray containing all the old entries from JSON file
        getJSONArray(c);
        // Making object arraylist for further use.
        makeSavedEntryList();
        // Making HashMap from entry titles so we can see if we are about to add copy
        EntryHashMap();
    }

    public boolean saveEntry(String title, String comment, float stars, LocalDate date, Context c){

        Entry entry = new Entry(title, comment, stars, date);
        // Adding entry object to arraylist consisting all the entries.
        allEntryObjects.add(entry);
        // Saving the entry to JSON file
        boolean bool = saveEntryJSON(c);
        return bool;
    }
    // method which deletes chosen Entry
    public void deleteEntry(int index, String title, Context c){

        allEntryObjects.remove(index);

        for (int i = 0; i < entriesJSON.length(); i++){
            try {
                JSONObject jObject = entriesJSON.getJSONObject(i);
                if (jObject.getString("Title").equals(title)){
                    entriesJSON.remove(i);
                    String json = entriesJSON.toString(2);
                    this.writeJSON(json, c);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Deleting title from HashMap so application knows if new Entry is copy or not.
        entriesHashMap.remove(title);
        return;
    }

    // Method which makes ArrayList from all the reviews user has saved and made so list can be displayed in listview
    public ArrayList<String> ReviewTitleArrayList(){

        ArrayList<String> reviewTitleArrayList = new ArrayList<>();
        // Clearing the titleArrayList before use so it's guaranteed to be empty
        reviewTitleArrayList.removeAll(reviewTitleArrayList);

        allEntryObjects.sort((o2,o1) -> Float.compare(o1.getStars(), o2.getStars()));

        for (int i = 0; i < allEntryObjects.size(); i++){
            entryObject = allEntryObjects.get(i);
            reviewTitleArrayList.add(entryObject.getTitle());
        }

        return reviewTitleArrayList;
    }

    // This method sends back the title of chosen review
    public String getTitle(int i){

        entryObject = allEntryObjects.get(i);

        return entryObject.getTitle();
    }

    // This method sends back the rating of chosen review
    public float getRating(int i){

        entryObject = allEntryObjects.get(i);

        return entryObject.getStars();
    }

    // This method sends back the comment of chosen review.
    public String getComment(int i){

        entryObject = allEntryObjects.get(i);

        return entryObject.getComment();
    }

    // This method sends back the date of chosen review
    public String getDate(int i){

        entryObject = allEntryObjects.get(i);

        LocalDate date = entryObject.getDate();

        return date.toString();
    }

    // Method which saves new Entry
    private boolean saveEntryJSON(Context c){

        JSONArray jsonArray = new JSONArray();

        // Integer to message latter part of the method if writing the file is necessary.
        int check = 0;
        // Getting last entry from the object array list so we can see if it is copy or we add it to the JSON file.
        int index = allEntryObjects.size() - 1;

        // Getting different instances of the filmObject
        entryObject = allEntryObjects.get(index);

        if (entriesHashMap.containsKey(entryObject.getTitle())){
            allEntryObjects.remove(index); // Removing duplicate
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                // Adding the info to jsonObject
                jsonObject.put("Title", entryObject.getTitle());
                jsonObject.put("Date", entryObject.getDate());
                jsonObject.put("Comment", entryObject.getComment());
                jsonObject.put("StarRating", entryObject.getStars());
                // Add entry also into HashMap which contains all saved titles so copies are not added.
                entriesHashMap.put(jsonObject.getString("Title"),"");
                check = 1;
                if (check != 0) {
                    try {
                        // Append the new array to the end of old one to get evey film added to the JSON file
                        entriesJSON.put(jsonObject);
                        String json = entriesJSON.toString(2);
                        // Calling writing method.
                        boolean bool = this.writeJSON(json, c);
                        return bool;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Saving the reviews on JSON file
    private boolean writeJSON(String json, Context c){

        // Setting up the file to save Entries
        File file = new File(c.getFilesDir(), "entries.json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // With this method I can read the JSON file which is saved from previous uses of the app and can compare it to new xml list and see if there is copies.
    // I chose HashMap as it is quicker than going through arraylist with for loop if you have large number of entries to check from.
    private void EntryHashMap(){

        if(entriesJSON != null) {
            try {
                for (int i = 0; i < entriesJSON.length(); i++){
                    JSONObject jsonObject = entriesJSON.getJSONObject(i);
                    entriesHashMap.put(jsonObject.getString("Title"),"");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    // Method that gets the already saved entries and returns them as JSONArray
    private void getJSONArray(Context c){

        String json = getOldEntries(c);

        if (json != null) {
            try {
                entriesJSON = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    // Method which gets all the saved Entries to arraylist full of Entry objects.
    private void makeSavedEntryList(){

        for(int i = 0; i < entriesJSON.length(); i++){
            try {
                JSONObject jsonObject = entriesJSON.getJSONObject(i);
                float star = Float.parseFloat(jsonObject.getString("StarRating"));
                LocalDate date = LocalDate.parse(jsonObject.getString("Date"));
                // making new Entry object with data from JSON fileget
                Entry entry = new Entry(jsonObject.getString("Title"), jsonObject.getString("Comment"), star, date); allEntryObjects.add(entry);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Method which gets all the oldEntries from the JSON file.
    private String getOldEntries(Context context){

        String json = null;

        try {

            InputStream inputStream = context.openFileInput("entries.json");

            if ( inputStream != null ) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String stringline = null;
                while ( (stringline = br.readLine()) != null ) {
                    sb.append(stringline).append("\n");
                }
                json = sb.toString();
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("", "Can not read file: " + e.toString());
        }
        return json;
    }
}
