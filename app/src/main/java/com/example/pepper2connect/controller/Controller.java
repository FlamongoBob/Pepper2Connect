package com.example.pepper2connect.controller;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.pepper2connect.Crypto.Decryption;
import com.example.pepper2connect.Crypto.Encryption;
import com.example.pepper2connect.MainActivity;
import com.example.pepper2connect.Model.User;
import com.example.pepper2connect.R;
import com.example.pepper2connect.client.Client;
import com.example.pepper2connect.messages.Message;
import com.example.pepper2connect.messages.MessageI;
import com.example.pepper2connect.messages.MessageSystem;
import com.example.pepper2connect.messages.MessageType;
import com.example.pepper2connect.messages.MessageUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Controller {
    // public static volatile Boolean isClientConnected = false;
    // public static volatile Boolean isLoggedIn = false;
    /**
     * TODO COMMENT isClientConnected, isLoggedIn
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

    MainActivity mainActivity;
    //New User Controlls
    EditText etNuFirstName, etNuTitle, etNuLastName, etNuPassword, etNuUserName;
    String strNewUserPicture;
    Spinner spRole;
    int intNuRoleID, intNuTitleID;
    ImageButton ibNewPicture;
    RadioGroup rgConfidential;
    RadioButton rb_RConfidential, rb_NConfidential;
    //----

    //UserManagment Controlls
    ArrayList<User> allUsers = new ArrayList<>();
    EditText etUMFirstName, etUMTitle, etUMLastName, etUMPassword, etUMUserName;
    String strUMPicture;
    Spinner spUMRole;
    int intUMRoleID, intUserID, intEmployeeID, intPictureID;
    ImageButton ibUMPicture;
    RadioGroup rgConfidentialUM;
    RadioButton rb_RConfidentialUM, rb_NConfidentialUM;
    //----


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private String strBufferPatientInfo = "", strBufferLogServerCon = "";

    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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
                case Suc_IUD:

                    strMessage = ((MessageSystem) msgSys).getStrSystemNotification();
                    alertDialogBuilder.setTitle(resources.getText(R.string.Suc_IUD_Title));
                    alertDialogBuilder.setMessage(resources.getText(R.string.Suc_IUD_Text) + strMessage);

                    etNuTitle.setText("");
                    etNuFirstName.setText("");
                    etNuLastName.setText("");
                    strNewUserPicture = "";
                    spRole.setSelection(0);
                    etNuUserName.setText("");
                    etNuPassword.setText("");

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
     * Fraggment NewUser
     */
    Encryption e = new Encryption();

    public void addNewUser() {
        int intRConfidentialInfoID = -1;
        MessageI msgI = null;
        int intCheckedID = rgConfidential.getCheckedRadioButtonId();


            if (intCheckedID == rb_NConfidential.getId()) {
                msgI = new MessageI(e.encrypt(etNuTitle.getText().toString())
                        , e.encrypt(etNuFirstName.getText().toString())
                        , e.encrypt(etNuLastName.getText().toString())

                        , e.encrypt(strNewUserPicture)

                        , (int) spRole.getSelectedItemId()

                        , e.encrypt(etNuUserName.getText().toString())
                        , e.encrypt(etNuPassword.getText().toString())

                        , 0
                );
            } else if (intCheckedID == rb_RConfidential.getId()) {
                msgI = new MessageI(e.encrypt(etNuTitle.getText().toString())
                        , e.encrypt(etNuFirstName.getText().toString())
                        , e.encrypt(etNuLastName.getText().toString())

                        , e.encrypt(strNewUserPicture)

                        , (int) spRole.getSelectedItemId()

                        , e.encrypt(etNuUserName.getText().toString())
                        , e.encrypt(etNuPassword.getText().toString())

                        , 1
                );
            }

        if (msgI != null) {
            Toast.makeText(mainActivity, "Please Select if the user receives Confidential Information", Toast.LENGTH_SHORT).show();
        } else {
            client.sendInsertMessage(msgI);
        }

    }

    public void clearNewUser() {
        etNuTitle.setText("");
        etNuFirstName.setText("");
        etNuLastName.setText("");
        strNewUserPicture = "";
        spRole.setSelection(0);
        etNuUserName.setText("");
        etNuPassword.setText("");
    }

    public void newPicture() {


    }
    /**
     * UserManagement
     */

    public void getAllEmployeeData(){
        MessageSystem msgSys = new MessageSystem("");
        msgSys.setType(MessageType.AllUser);
        client.sendSysMessage(msgSys);
    }

    public void populateArrayAllUsers(User user){

        allUsers.add(user);
    }

    public void populateUserManagementControlls(){

    }




    /**
     * General
     */

    public void fillCurrentUser(MessageUser msgU) {
        try {


            if (msgU != null) {
                currentUser = new User(msgU.getIntEmployeeID()
                        , decryption.decrypt(msgU.getStrTitle())
                        , decryption.decrypt(msgU.getStrFirstname())
                        , decryption.decrypt(msgU.getStrLastname())

                        , msgU.getIntPictureID()
                        , decryption.decrypt(msgU.getStrPicture())

                        , msgU.getIntRoleID()
                        , decryption.decrypt(msgU.getStrRole())

                        , msgU.getIntUserID()
                        , decryption.decrypt(msgU.getStrUserName())
                        , decryption.decrypt(msgU.getStrLastname())

                        , msgU.getIntGetsConfidentialInfo()
                );
            }
        } catch (Exception ex) {
            String err = ex.getMessage();
            err += "";
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

    public void setRgConfidential(RadioGroup rg) {
        this.rgConfidential = rg;
    }

    public void setRb_RConfidential(RadioButton rb_RConfidential) {
        this.rb_RConfidential = rb_RConfidential;
    }

    public void setRb_NConfidential(RadioButton rb_NConfidential) {
        this.rb_NConfidential = rb_NConfidential;
    }


    public void setIBNewPicture(ImageButton ibNewPicture) {
        this.ibNewPicture = ibNewPicture;
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

    public void setStrNewUserPicture(String strNewUserPicture) {
        this.strNewUserPicture = strNewUserPicture;
    }

    public void setIBNewPicture(Bitmap bmNewUserPicture) {
        if (ibNewPicture != null && bmNewUserPicture != null) {
            this.ibNewPicture.setImageBitmap(bmNewUserPicture);
        }
    }

    public void setIntNuRoleID(int intNuRoleID) {
        this.intNuRoleID = intNuRoleID;
    }

    public void setIntNuTitleID(int intNuTitleID) {
        this.intNuTitleID = intNuTitleID;
    }

    public void setEtNuTitle(EditText etNuTitle) {
        this.etNuTitle = etNuTitle;
    }

    public void setSpRole(Spinner spRole) {
        this.spRole = spRole;
    }

    public void setEtUMFirstName(EditText etUMFirstName) {
        this.etUMFirstName = etUMFirstName;
    }

    public void setEtUMTitle(EditText etUMTitle) {
        this.etUMTitle = etUMTitle;
    }

    public void setEtUMLastName(EditText etUMLastName) {
        this.etUMLastName = etUMLastName;
    }

    public void setEtUMPassword(EditText etUMPassword) {
        this.etUMPassword = etUMPassword;
    }

    public void setEtUMUserName(EditText etUMUserName) {
        this.etUMUserName = etUMUserName;
    }

    public void setStrUMPicture(String strUMPicture) {
        this.strUMPicture = strUMPicture;
    }

    public void setSpUMRole(Spinner spUMRole) {
        this.spUMRole = spUMRole;
    }

    public void setIntUMRoleID(int intUMRoleID) {
        this.intUMRoleID = intUMRoleID;
    }

    public void setIbUMPicture(ImageButton ibUMPicture) {
        this.ibUMPicture = ibUMPicture;
    }

    public void setRgConfidentialUM(RadioGroup rgConfidentialUM) {
        this.rgConfidentialUM = rgConfidentialUM;
    }

    public void setRb_RConfidentialUM(RadioButton rb_RConfidentialUM) {
        this.rb_RConfidentialUM = rb_RConfidentialUM;
    }

    public void setRb_NConfidentialUM(RadioButton rb_NConfidentialUM) {
        this.rb_NConfidentialUM = rb_NConfidentialUM;
    }
}
