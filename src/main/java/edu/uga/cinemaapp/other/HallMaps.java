package edu.uga.cinemaapp.other;

import java.util.HashMap;
import java.util.Map;

public class HallMaps {

    public static final Map<Integer, String> statusMap = new HashMap<>();

    static {
        statusMap.put(0, "Active");
        statusMap.put(1, "Inactive");
    }
}