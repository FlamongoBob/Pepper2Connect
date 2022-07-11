package com.example.pepper2connect.controller;

import android.app.Activity;
import android.widget.EditText;

import com.example.pepper2connect.client.Client;
import com.example.pepper2connect.messages.MessageSystem;
import com.example.pepper2connect.messages.messageType;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Controller {
    public static volatile Boolean isClientConnected = false;
    public static volatile Boolean isLoggedIn = true;
    private Client client;
    protected Activity runningActivity;
    EditText etLogServerCon;
    String strUsername="Test";


    public Controller(){

    }

    public void connect2Peppper(String strIPAddress, int intPort, String strUsername, String strPassword){
        this.strUsername = strUsername;
        client = new Client(strIPAddress, intPort, strUsername,strPassword,this);

    }

    public void disconnectFromPepper(){
        if(client!= null && isClientConnected){
            client.disconnect();
        }
    }
    public void testServerConnection(){
        String strLogMessage="";

        if(isClientConnected){
            MessageSystem msgSysTest = new MessageSystem("");
            msgSysTest.setType(messageType.Test);
            //client.sendSysMessage(msgSysTest);
            appendText2Log(msgSysTest);
        }
    }

    private void appendText2Log(MessageSystem messageSystem){
        if(etLogServerCon!=null){
            String strAppendString="";
            etLogServerCon.append("\n");
            String strCurrentDateTime = getDateTime();

            String strLoginStatus="";
            if (!isClientConnected){
                strLoginStatus = " not";
            }
            strAppendString =strUsername+ "is currently"+strLoginStatus+" connected \n"
                    +"Last Message received: \n"+getDateTime()+"\n Messagetype: \n" + messageSystem.getType().toString();
            etLogServerCon.append(strAppendString);

        }

    }
    public String getDateTime(){
        Date currentDateTime = Calendar.getInstance().getTime();
        System.out.println("Current time => " + currentDateTime);

        //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
       // String formattedDate = df.format(c);
        return currentDateTime.toString();
    }

    public void setEtLogServerCon(EditText etLogServerCon) {
        this.etLogServerCon = etLogServerCon;
    }
}
