package com.example.pepper2connect.controller;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.pepper2connect.Crypto.Decryption;
import com.example.pepper2connect.Model.User;
import com.example.pepper2connect.R;
import com.example.pepper2connect.client.Client;
import com.example.pepper2connect.messages.Message;
import com.example.pepper2connect.messages.MessageInsert;
import com.example.pepper2connect.messages.MessageSystem;
import com.example.pepper2connect.messages.MessageType;
import com.example.pepper2connect.messages.MessageUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Controller {
    // public static volatile Boolean isClientConnected = false;
    // public static volatile Boolean isLoggedIn = false;
    /**TODO COMMENT isClientConnected, isLoggedIn
     *
     */
    public static volatile Boolean isClientConnected = true;
    public static volatile Boolean isLoggedIn = true;
    private Client client;
    Resources resources = Resources.getSystem();

    final private String strServerIP = "127.10.10.15";
    final private int intPort = 10284;
    Decryption decryption = new Decryption();

    private User currentUser;

    EditText etLogServerCon, etPatientInformation, etLoginPassword, etLoginUserName, etProfileFirstName, etProfileTitle, etProfileLastName;

    String strUsername = "Test";

    TextView tvLoginInformation;


    //New User Controlls
    EditText etNuFirstName, etNuTitle, etNuLastName, etNuPassword, etNuUserName;
    String strPicture;
    Spinner spTitle, spRole;
    int intNuRoleID, intNuTitleID;
    //----

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private String strBufferPatientInfo = "", strBufferLogServerCon = "";

    public Controller() {

    }

    public void connect2Pepper(String strUsername, String strPassword) {
        if (!isLoggedIn && !isClientConnected) {
            if (strUsername != null) {
                if (!strUsername.isEmpty()) {
                    if (strPassword != null) {
                        if (!strPassword.isEmpty()) {
                            this.strUsername = strUsername;

                            try {
                                currentUser = null;
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
    }

    /**
     * Fragment_Login
     */

    public void clientSuccessfulLogin(MessageSystem msgSys) {
        showInformation(msgSys);
        isLoggedIn = true;
    }

    public void disconnectFromPepper(MessageSystem msgSys) {
        if (client != null && isClientConnected && isLoggedIn) {
            showInformation(msgSys);

            appendLogServerCon(msgSys);
            client.disconnect();
        }
    }

    public void showInformation(Message msgSys) {
        String strMessage;
        if (tvLoginInformation != null) {
            switch (msgSys.getType()) {
                case LogOut:
                    alertDialogBuilder.setTitle(resources.getText(R.string.Logged_Out_Title));
                    alertDialogBuilder.setMessage(resources.getText(R.string.Logged_Out_Text));
                    alertDialogBuilder.setPositiveButton(resources.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

                    tvLoginInformation.setText(resources.getText(R.string.Logged_Out_Text));
                    tvLoginInformation.setTextColor(Color.parseColor("#FF000000"));
                    break;
                case Unsuccessful_LogIn:
                    alertDialogBuilder.setTitle(resources.getText(R.string.UnSuc_Login_Title));
                    alertDialogBuilder.setMessage(resources.getText(R.string.UnSuc_Login_Text));
                    alertDialogBuilder.setPositiveButton(resources.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    tvLoginInformation.setText(resources.getText(R.string.UnSuc_Login_Text));
                    tvLoginInformation.setTextColor(Color.parseColor("#b50000"));
                    break;
                case Successful_LogIn:
                    alertDialogBuilder.setTitle(resources.getText(R.string.Suc_Login_Title));
                    alertDialogBuilder.setMessage(resources.getText(R.string.Suc_Login_Text));
                    alertDialogBuilder.setPositiveButton(resources.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    tvLoginInformation.setText("You have successfully Logged in!.");
                    tvLoginInformation.setTextColor(Color.parseColor("#00B612"));
                    break;
                case Error:

                    strMessage = ((MessageSystem) msgSys).getStrSystemNotification();
                    alertDialogBuilder.setTitle(resources.getText(R.string.Error_Title));
                    alertDialogBuilder.setMessage(resources.getText(R.string.Error_Text) + strMessage);
                    alertDialogBuilder.setPositiveButton(resources.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    tvLoginInformation.setText(resources.getText(R.string.Error_Text) + strMessage);
                    tvLoginInformation.setTextColor(Color.parseColor("#b50000"));
                    break;
                case Unsuc_NewUserAdded:
                    strMessage = ((MessageSystem) msgSys).getStrSystemNotification();
                    alertDialogBuilder.setTitle(resources.getText(R.string.Unsuc_NewUserAdded_Title));
                    alertDialogBuilder.setMessage(resources.getText(R.string.Unsuc_NewUserAdded_Text) + strMessage);
                    break;
                case Suc_NewUserAdded:

                    strMessage = ((MessageSystem) msgSys).getStrSystemNotification();
                    alertDialogBuilder.setTitle("Successfully created new User");
                    alertDialogBuilder.setMessage(strMessage);
                    break;

            }
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    /**
     * Fragment_Server
     */

    /**
     * TODO decomment sendSysMessage
     */
    public void clientLogOut() {
        if (isClientConnected) {
            MessageSystem msgSysTest = new MessageSystem("");
            msgSysTest.setType(MessageType.LogOut);

            client.sendSysMessage(msgSysTest);
        }
    }

    /**
     * TODO decomment sendSysMessage
     */
    public void testServerConnection() {
        if (isClientConnected) {
            MessageSystem msgSysTest = new MessageSystem("");
            msgSysTest.setType(MessageType.Test);

            client.sendSysMessage(msgSysTest);
        }
    }

    public void appendLogServerCon(MessageSystem messageSystem) {

        String strAppendString = "";
        strAppendString += "Last Message Type received: \n" + messageSystem.getType().toString();

        if (etLogServerCon != null) {
            appendText2EditText(strAppendString, etLogServerCon);
        } else {

            if (strBufferLogServerCon == null) {
                strBufferLogServerCon = stringBuffer(strAppendString, strBufferLogServerCon);

            } else {

                strBufferLogServerCon += stringBuffer(strAppendString, strBufferLogServerCon);
            }
        }
    }

    public void checkLogServerConBuffer() {
        if (strBufferLogServerCon != null) {
            if (!strBufferLogServerCon.isEmpty()) {
                etLogServerCon.append(strBufferLogServerCon);
                strBufferLogServerCon = "";
            }
        }
    }

    /**
     * Fragment_Profile
     */

    public void fillProfile(Fragment fragment_profile) {
        if (currentUser != null && etProfileTitle != null && etProfileFirstName != null && etProfileLastName != null) {
            etProfileTitle.setText(currentUser.getStrTitle());
            etProfileFirstName.setText(currentUser.getStrFirstname());
            etProfileLastName.setText(currentUser.getStrLastname());


        }
    }

    /**
     * Patient Information
     */


    public void appendPatientInformation(MessageSystem messageSystem) {
        try {
            if (etPatientInformation != null) {
                String strAppendString = decryption.decrypt(messageSystem.getStrSystemNotification());

                appendText2EditText(strAppendString, etPatientInformation);
            } else {
                if (strBufferLogServerCon == null) {
                    strBufferLogServerCon = stringBuffer(decryption.decrypt(messageSystem.getStrSystemNotification()), strBufferPatientInfo);
                } else {

                    strBufferLogServerCon += stringBuffer(decryption.decrypt(messageSystem.getStrSystemNotification()), strBufferPatientInfo);
                }

            }
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err += "";
        }
    }

    public void checkPatientInfoBuffer() {
        if (strBufferPatientInfo != null) {
            if (!strBufferPatientInfo.isEmpty()) {
                etPatientInformation.append(strBufferPatientInfo);
                strBufferPatientInfo = "";
            }
        }
    }

    /**
     * NewUser
     */

    public void addNewUser() {
        MessageInsert msgNu = new MessageInsert(MessageType.NewUser
                , etNuTitle.getText().toString()
                , etNuFirstName.getText().toString()
                , etNuLastName.getText().toString()
                , strPicture
                , (int) spRole.getSelectedItemId()
                , etNuUserName.getText().toString()
                , etNuPassword.getText().toString());
        client.sendInsertMessage(msgNu);
    }



    /**
     * General
     */

    public void fillCurrentUser(MessageUser msgU) {
        try {


            if (msgU != null) {
                currentUser = new User(Integer.parseInt(decryption.decrypt(msgU.getStrUserID()))
                        , decryption.decrypt(msgU.getStrTitle())
                        , decryption.decrypt(msgU.getStrFirstname())
                        , decryption.decrypt(msgU.getStrLastname())
                        , decryption.decrypt(msgU.getStrPicture())
                        , msgU.getIntRoleID());
            }
        }catch (Exception ex){
            String err = ex.getMessage();
            err +="";
        }
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
            etAppend.append("\n");
        }
    }

    private String stringBuffer(String strAppendText, String strBuffer) {
        if (strBuffer != null) {
            strBuffer += "\n ------------- \n "
                    + getDateTime()
                    + " \n  "
                    + strAppendText
                    + "\n ------------- \n";

        } else {
            strBuffer = "\n ------------- \n "
                    + getDateTime() + " \n  "
                    + strAppendText
                    + "\n ------------- \n";
        }
        return strBuffer;
    }

    public String getDateTime() {
        Date currentDateTime = Calendar.getInstance().getTime();
        System.out.println("Current time => " + currentDateTime);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy - HH:mm", Locale.getDefault());
        String formattedDate = df.format(currentDateTime);
        return formattedDate;
    }

    /**
     * Setters for Controls
     */


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

    public void setEtProfileFirstName(EditText etProfileFirstName) {
        this.etProfileFirstName = etProfileFirstName;

    }

    public void setEtProfileLastName(EditText etProfileLastName) {
        this.etProfileLastName = etProfileLastName;

    }

    public void setEtProfileTitle(EditText etProfileTitle) {
        this.etProfileTitle = etProfileTitle;

    }

    public void setEtNuFirstName(EditText etNuFirstName) {
        this.etNuFirstName = etNuFirstName;
    }

    public void setEtNuTitle(EditText etNuTitle) {
        this.etNuTitle = etNuTitle;
    }

    public void setEtNuLastName(EditText etNuLastName) {
        this.etNuLastName = etNuLastName;
    }

    public void setEtNuPassword(EditText etNuPassword) {
        this.etNuPassword = etNuPassword;
    }

    public void setEtNuUserName(EditText etNuUserName) {
        this.etNuUserName = etNuUserName;
    }

    public void setStrPicture(String strPicture) {
        this.strPicture = strPicture;
    }

    public void setIntNuRoleID(int intNuRoleID) {
        this.intNuRoleID = intNuRoleID;
    }

    public void setIntNuTitleID(int intNuTitleID) {
        this.intNuTitleID = intNuTitleID;
    }

    public void setSpTitle(Spinner spTitle) {
        this.spTitle = spTitle;
    }

    public void setSpRole(Spinner spRole) {
        this.spRole = spRole;
    }
}
