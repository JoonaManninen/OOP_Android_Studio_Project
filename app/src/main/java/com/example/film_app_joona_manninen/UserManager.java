package com.example.film_app_joona_manninen;

// Made by: Joona Manninen 28.7.2022 Object-oriented programming course project (Film-app)
// Sources used in making of this project: https://www.youtube.com/watch?v=lEIRIDMynos
// https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec10/adding-salt-to-a-hash-(intermediate)
// https://stackoverflow.com/

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.film_app_joona_manninen.data_classes.User;

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
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserManager {
    // JSNOArray and User object array which store information of users so we can use the data in methods.
    private static JSONArray userDataJSONArray = new JSONArray();
    private static ArrayList<User> userObjects = new ArrayList<>();
    User users = new User();

    public UserManager(){}


    // Method which is used to check login credentials and returns true if user credentials are correct.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean logIn(String username, String password){

        for(int i = 0; i < userDataJSONArray.length(); i++){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject = userDataJSONArray.getJSONObject(i);
                if (username.equals(jsonObject.getString("Username"))) {
                    boolean bool = checkPassword(password, jsonObject.getString("Salt"), jsonObject.getString("Password"));
                    if (bool){
                        return true;
                    } else{return false;}
                }
            } catch (JSONException | InvalidKeySpecException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    // This method checks if the password is correct and returns true if it is matching the username.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkPassword (String password, String strOldSalt, String strPsw) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {


        byte[] salt = fromHex(strOldSalt);
        byte [] oldPsw = fromHex(strPsw);

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65569, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashPassword = factory.generateSecret(spec).getEncoded();

        if(Arrays.equals(oldPsw, hashPassword)){
            return true;
        } else{
            return false;
        }
    }
    // Method which checks that password checks all the conditions
    public boolean checkPasswordStrength (String password) {
        // Setting up the variables
        int passowordLength = 12;
        int specialChar = 0, num = 0, upChar = 0, lowChar = 0;

        if (password.length() < passowordLength) {
            return false;
        } else {
            for (int i = 0; i < password.length(); i++) {
                // Getting password characters one at a time
                char ch = password.charAt(i);
                // Checking that all the attributes of good password are matched
                if (Character.isDigit(ch)){
                    num = 1;
                } else if (Character.isUpperCase(ch)){
                    upChar = 1;
                } else if (Character.isLowerCase(ch)){
                    lowChar = 1;
                } else {
                    specialChar = 1;
                }
            }
        }
        if (upChar==1 && lowChar==1 && num==1 && specialChar==1) {
            return true;
        } else {
            return false;
        }
    }

    // Method which salts and hashes the password.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void HashData(String username , String password) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {

        // First we generate random 16-byte salt with using SHA1PRNG pseudo random number generator algorithm so it's harder to figure out the salt.
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        // Hashing the password
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65569, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashPassword = factory.generateSecret(spec).getEncoded();

        // Making strings from byte arrays so we can save them to file.
        String strSalt = toHex(salt);
        String strPsw = toHex(hashPassword);

        User user = new User(username, strPsw, strSalt);
        userObjects.add(user);

        return;
    }

    // Method which writes userdata to JSON file and saves it.
    public boolean WriteUserData(Context c){

        // Saving userdata to userdata.json
        File file = new File(c.getFilesDir(), "userdata.json");
        // JSONObject where data is first stored
        JSONObject jsonObject = new JSONObject();
        FileWriter fileWriter = null;

        try {
            // Getting username, salt and password from User arraylist
            int index = userObjects.size() - 1;
            users = userObjects.get(index);
            jsonObject.put("Username", users.getUsername());
            jsonObject.put("Password", users.getPassword());
            jsonObject.put("Salt", users.getSalt());
            // Adding JSONObject to JSONArray and make it string to add it in the file.
            userDataJSONArray.put(jsonObject);
            String json = userDataJSONArray.toString(2);

            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method that gets the already saved entries and returns them as JSONArray
    public void getJSONArray(Context c){

        // getOlderUserData returns all the user credentials which are already saved in JSON file.
        String json = getOldUserData(c);

        if (json != null) {
            try {
                userDataJSONArray = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    // Method which gets all the oldEntries from the JSON file.
    private String getOldUserData(Context context){

        String json = null;

        try {

            InputStream inputStream = context.openFileInput("userdata.json");

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
    // This method is used to turn byte arrays to hexadecimal strings so we can store salt and password to the JSON file
    public static String toHex (byte[] array){

        BigInteger bigInteger = new BigInteger(1, array);
        String hex = bigInteger.toString(16);

        // Getting padding length
        int paddingLength = (array.length * 2) - hex.length();

        // If there is padding
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
    // This method is used to turn strings into byte arrays so we can confirm user credentials when logging in
    public static byte [] fromHex (String hex) {
        //  Create byte array with half of the hex string length because the hex string is twice as big
        byte[] bytes = new byte[hex.length() / 2];

        for (int i = 0; i < bytes.length; i++){

            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2* i + 2), 16);
        }
        return bytes;
    }

}
