package edu.uga.cinemaapp.viewmodel;

import lombok.Data;

@Data
public class AddUserVM {

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private int active; // 1 is active 0 is inactive
}