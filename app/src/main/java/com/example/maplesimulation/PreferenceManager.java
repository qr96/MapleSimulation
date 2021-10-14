package com.example.maplesimulation;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PreferenceManager {

    public static final String PREFERENCES_NAME = "rebuild_preference";
    private static final int DEFAULT_VALUE_INT = -1;
    private static final String DEFAULT_VALUE_STRING = "";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setInventory(Context context, ArrayList<Equipment> inventory) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new GsonBuilder().create();
        Type arrayType = new TypeToken<ArrayList<Equipment>>() {}.getType();
        String json = gson.toJson(inventory, arrayType);

        editor.putString("inventory", json);
        editor.commit();
    }

    public static ArrayList<Equipment> getInventory(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);

        Type arrayType = new TypeToken<ArrayList<Equipment>>() {}.getType();
        Gson gson = new Gson();
        ArrayList<Equipment> inventory;

        if(value == "") inventory = new ArrayList<Equipment>();
        else inventory = gson.fromJson(value, arrayType);

        return inventory;
    }

    public static void setCharacter(Context context, ArrayList<Character> characters) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new GsonBuilder().create();
        Type arrayType = new TypeToken<ArrayList<Character>>() {}.getType();
        String json = gson.toJson(characters, arrayType);

        editor.putString("character", json);
        editor.commit();
    }

    public static ArrayList<Character> getCharacter(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);

        Type arrayType = new TypeToken<ArrayList<Character>>() {}.getType();
        Gson gson = new Gson();
        ArrayList<Character> characters;

        if(value == "") characters = new ArrayList<Character>();
        else characters = gson.fromJson(value, arrayType);

        return characters;
    }

    /*
    public static void setEquipment(Context context, String key, Equipment equipment) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new GsonBuilder().create();

        String value = gson.toJson(equipment, Equipment.class);

        editor.putString(key, value);
        editor.commit();
    }

    public static Equipment getEquipment(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        Gson gson = new Gson();
        Equipment equipment;

        if(value == "") equipment = new Equipment();
        else equipment = gson.fromJson(value, Equipment.class);

        return equipment;
    }
     */

    public static void setString(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        int value = prefs.getInt(key, DEFAULT_VALUE_INT);
        return value;
    }

    //키값 삭제
    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }

    //데이터 전부 삭제
    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
}
