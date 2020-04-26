package edu.uga.cinemaapp.viewmodel;

import lombok.Data;

@Data
public class ShowtimeVM {

    private int id;
    private String name;
    private String date;
    private String time;
    private String duration;
    private int availability;
    // private int movie_id; 
    private int hall_id;
}