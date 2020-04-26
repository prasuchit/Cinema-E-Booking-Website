package edu.uga.cinemaapp.viewmodel;

import lombok.Data;

@Data
public class BookingVM {

    private int id;
    private int numAdult;
    private int numChild;
    private int numOld;
    private int showtime_id;
}