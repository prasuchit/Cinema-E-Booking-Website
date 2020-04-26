package edu.uga.cinemaapp.other;

import java.util.HashMap;
import java.util.Map;

public class MovieMaps {

    public static final Map<Integer, String> genreMap = new HashMap<>();
    public static final Map<Integer, String> ratingMap = new HashMap<>();
    public static final Map<Integer, String> statusMap = new HashMap<>();
    public static final Map<String, Integer> genreReverseMap = new HashMap<>();

    static {
        genreMap.put(0, "Action");
        genreMap.put(1, "Adventure");
        genreMap.put(2, "Comedy");
        genreMap.put(3, "Drama");
        genreMap.put(4, "Crime");
        genreMap.put(5, "Sci-Fi");
        genreMap.put(6, "Romance");
        

        ratingMap.put(0, "G");
        ratingMap.put(1, "PG");
        ratingMap.put(2, "PG_13");
        ratingMap.put(3, "R");
        ratingMap.put(4, "NC_17");

        statusMap.put(0, "Available");
        statusMap.put(1, "Coming Soon");

        genreReverseMap.put("action", 0);
        genreReverseMap.put("adventure", 1);
        genreReverseMap.put("comedy", 2);
        genreReverseMap.put("drama", 3);
        genreReverseMap.put("crime", 4);
        genreReverseMap.put("sci-fi", 5);
        genreReverseMap.put("science fiction", 5);
        genreReverseMap.put("scifi", 5);
        genreReverseMap.put("science", 5);
        genreReverseMap.put("technology", 5);
        genreReverseMap.put("romance", 6);
    }
}