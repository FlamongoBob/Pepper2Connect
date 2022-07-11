package com.example.pepper2connect.controller;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pepper2connect.Model.User;
import com.example.pepper2connect.client.Client;
import com.example.pepper2connect.messages.Message;
import com.example.pepper2connect.messages.MessageSystem;
import com.example.pepper2connect.messages.MessageType;
import com.example.pepper2connect.messages.MessageUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Controller {
    public static volatile Boolean isClientConnected = true;
    public static volatile Boolean isLoggedIn = true;
    private Client client;

    final private String strServerIP = "127.10.10.15";
    final private int intPort = 10284;

    private User currentUser;

    EditText etLogServerCon, etPatientInformation, etLoginPassword, etLoginUserName, etProfileFirstName, etProfileTitle, etProfileLastName;

    String strUsername = "Test";

    TextView tvLoginInformation;


    public Controller() {

    }

    public void connect2Peppper(String strUsername, String strPassword) {

        if (strUsername != null) {
            if (!strUsername.isEmpty()) {
                if (strPassword != null) {
                    if (!strPassword.isEmpty()) {
                        this.strUsername = strUsername;

                        try {
                            client = new Client(this.strServerIP, this.intPort, strUsername, strPassword, this);
                        } catch (Exception ex) {
                            String err = "";
                            err = ex.getMessage();

                            strUsername = "";
                        }
                    }
                }
            }
        }
    }

    /**
     * Fragment_Login
     */

    public void clientSuccessfulLogin(MessageSystem msgSys) {
        showLoginStatusInformation(msgSys);
        isLoggedIn = true;
    }

    public void disconnectFromPepper(MessageSystem msgSys) {
        if (client != null && isClientConnected) {
            showLoginStatusInformation(msgSys);

            appendSystemMessage2EditText(msgSys);
            client.disconnect();
        }
    }

    public void showLoginStatusInformation(Message msgSys) {
        if (tvLoginInformation != null) {
            switch (msgSys.getType()) {
                case LogOut:
                    tvLoginInformation.setText("You have Successfully logged out. \n Have a nice day!");
                    tvLoginInformation.setTextColor(Color.parseColor("#FF000000"));
                    break;
                case Unsuccessful_LogIn:
                    tvLoginInformation.setText("Username or Password incorrect, please try again.");
                    tvLoginInformation.setTextColor(Color.parseColor("#b50000"));
                    break;
                case Successful_LogIn:
                    tvLoginInformation.setText("You have successfully Logged in!.");
                    tvLoginInformation.setTextColor(Color.parseColor("#00B612"));
                    break;
                case Error:
                    String strMessage = ((MessageSystem) msgSys).getStrSystemNotification();
                    tvLoginInformation.setText(strMessage);
                    tvLoginInformation.setTextColor(Color.parseColor("#b50000"));
                    break;

            }
        }
    }

    /**
     * Fragment_Server
     */


    public void testServerConnection() {
        if (isClientConnected) {
            MessageSystem msgSysTest = new MessageSystem("");
            msgSysTest.setType(MessageType.Test);

            //client.sendSysMessage(msgSysTest);

            appendSystemMessage2EditText(msgSysTest);
        }
    }

    public void appendSystemMessage2EditText(MessageSystem messageSystem) {
        if (messageSystem.getType().toString().equals(MessageType.Patient.toString())) {
            String strAppendString = messageSystem.getStrSystemNotification();
            appendText2EditText(strAppendString, etPatientInformation);
        }

        if (etLogServerCon != null) {
            String strAppendString = "";
            String strLoginStatus = "";
            if(isClientConnected) {
                if (!isLoggedIn) {
                    strLoginStatus = " not";
                }

                if (messageSystem.getType().toString().equals(MessageType.Successful_LogIn.toString())) {

                    strAppendString = strUsername + "is currently" + strLoginStatus + " connected and logged in. \n";
                }

                strAppendString += "Last Messagetype received: \n" + messageSystem.getType().toString();
            }else {
                strAppendString = "Cannot perform Test if App is not Connected to the Server";
            }

            appendText2EditText(strAppendString, etLogServerCon);

        }
    }

    /**
     * General
     */

    public void fillCurrentUser(MessageUser msgU) {
        currentUser = new User(msgU.getIntUserID(), msgU.getStrTitle(), msgU.getStrFirstname(), msgU.getStrLastname());
    }

    private void appendText2EditText(String strAppendText, EditText etAppend) {
        if (etAppend != null) {
            etAppend.append("\n");
            etAppend.append("-------------");
            etAppend.append("\n");
            etAppend.append(getDateTime());
            etAppend.append("\n");
            etAppend.append(strAppendText);
            etAppend.append("\n");
            etAppend.append("-------------");
        }
    }

    public String getDateTime() {
        Date currentDateTime = Calendar.getInstance().getTime();
        System.out.println("Current time => " + currentDateTime);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy - HH:mm", Locale.getDefault());
        String formattedDate = df.format(currentDateTime);
        return formattedDate;
    }

    public void setEtLogServerCon(EditText etLogServerCon) {
        this.etLogServerCon = etLogServerCon;
    }

    public void setEtPatientInformation(EditText etPatientInformation) {
        this.etPatientInformation = etPatientInformation;
    }

    public void setEtLoginUsername(EditText etLoginUserName) {
        this.etLoginUserName = etLoginUserName;

    }

    public void setEtLoginPassword(EditText etPassword) {
        this.etLoginPassword = etPassword;
    }

    public void setTvLoginInformation(TextView tvLoginInformation) {
        this.tvLoginInformation = tvLoginInformation;
    }
}
