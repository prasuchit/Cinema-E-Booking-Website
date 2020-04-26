package edu.uga.cinemaapp.viewmodel;

import lombok.Data;

@Data
public class ResetPasswdVM {

    private String oldPasswd;
    private String newPasswd;
    private String confPasswd;

}