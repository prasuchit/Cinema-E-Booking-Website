package edu.uga.cinemaapp.viewmodel;

import lombok.Data;

@Data
public class ForgotPasswdVM {

    private String hash;
    private String newPass;
    private String confPass;

}