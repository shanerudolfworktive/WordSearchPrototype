package com.rudolf.shane.duolingochallenger.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shane on 6/24/16.
 */
public class GameDisplayResponseModel {
    @SerializedName("source_language")
    public String sourceLanguage;
    public String word;
    @SerializedName("target_language")
    public String targetLanguage;
    @SerializedName("character_grid")
    public ArrayList<ArrayList<String>> characterGrid;
    @SerializedName("word_locations")
    public WordLocation wordLocations;

//    public static class CharacterGrid{
//        @SerializedName("character_grid")
//        public ArrayList<String> characterGrid;
//    }

    public static class WordLocation{
        @SerializedName("word_locations")
        HashMap<String, String> wordLocations;
    }
}
