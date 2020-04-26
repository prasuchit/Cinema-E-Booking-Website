package edu.uga.cinemaapp.other;

import java.util.HashMap;
import java.util.Map;

public class ShowtimeMaps {

    public static final Map<Integer, String> availabilityMap = new HashMap<>();

    static {
        availabilityMap.put(0, "Available");
        availabilityMap.put(1, "Unavailable");
    }
}