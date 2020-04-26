package edu.uga.cinemaapp.viewmodel;

import lombok.Data;
import java.util.List;

@Data
public class OrderVM {

    private int id;
    private String hallname;
    private String firstname;
    private String lastname;
    private String moviename;
    private double totalcost;
    private int adult;
    private int child;
    private int elderly;
    private String date;
    private String time;
    private List<Integer> seatnumbers;
    private String bankcardnum;
}