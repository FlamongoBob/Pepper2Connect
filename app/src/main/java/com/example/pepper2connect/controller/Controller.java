package com.example.pepper2connect.controller;

import android.app.Activity;

import com.example.pepper2connect.client.Client;

public class Controller {
    public static volatile Boolean isClientConnected = false;
    public static volatile Boolean isLoggedIn = false;
    private Client client;
    protected Activity runningActivity;

    public Controller(Activity runningActivity){

        this.runningActivity = runningActivity;
    }

    public void connect2Peppper(String ipAddress, int intPort, String strUsername, String strPassword){

    }

    public void disconnectFromPepper(){
        if(client!= null && isClientConnected){
            client.disconnect();
        }
    }





}
