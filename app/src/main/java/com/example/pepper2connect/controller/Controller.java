package com.example.pepper2connect.controller;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.pepper2connect.Crypto.Decryption;
import com.example.pepper2connect.Crypto.Encryption;
import com.example.pepper2connect.MainActivity;
import com.example.pepper2connect.Model.User;
import com.example.pepper2connect.R;
import com.example.pepper2connect.client.Client;
import com.example.pepper2connect.messages.Message;
import com.example.pepper2connect.messages.MessageD;
import com.example.pepper2connect.messages.MessageI;
import com.example.pepper2connect.messages.MessageRoles;
import com.example.pepper2connect.messages.MessageSystem;
import com.example.pepper2connect.messages.MessageType;
import com.example.pepper2connect.messages.MessageU;
import com.example.pepper2connect.messages.MessageUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Controller {
    public static volatile Boolean isClientConnected = false;
    public static volatile Boolean isLoggedIn = false;
    /**
     * TODO COMMENT isClientConnected, isLoggedIn
     */
    //public static volatile Boolean isClientConnected = true;
    //public static volatile Boolean isLoggedIn = true;
    private Client client;
    Resources resources = Resources.getSystem();

    //final private String strIPAdress  = "127.10.10.15"; //= "10.0.2.15";

    final private String strServerIP ="192.168.1.45";// "10.0.2.2";// = "127.10.10.15";
    final private int intPort = 10284; // = 6666; 7777;
    Decryption decryption = new Decryption();

    private User currentUser;

    EditText etLogServerCon, etPatientInformation, etLoginPassword, etLoginUserName;


    String strUsername = "Test";

    TextView tvLoginInformation;

    MainActivity mainActivity;




    private AlertDialog.Builder alertDialogBuilder;
    // private AlertDialog alertDialog;

    private String strBufferPatientInfo = "", strBufferLogServerCon = "";

    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Client connect2Pepper(String strUsername, String strPassword, int intPort, String strIpAddress) {


        if (!isLoggedIn && !isClientConnected) {
            if (strUsername != null) {
                if (!strUsername.isEmpty()) {
                    if (strPassword != null) {
                        if (!strPassword.isEmpty()) {
                            this.strUsername = strUsername;

                            try {
                                currentUser = null;
                                /*
                                client = new Client(
                                        this.strServerIP
                                        , this.intPort
                                        , e.encrypt(strUsername)
                                        , e.encrypt(strPassword)
                                        , this
                                        , this.mainActivity
                                );
                                */

                                client = new Client(
                                        strIpAddress
                                        , intPort
                                        , e.encrypt(strUsername)
                                        , e.encrypt(strPassword)
                                        , this
                                        , this.mainActivity
                                );

                                return client;

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
        return null;
    }

    /**
     * Fragment_Login
     */
    public void backToLogin(){
        mainActivity.backToLogin();
        isLoggedIn = false;
    }

    public void clientSuccessfulLogin(MessageSystem msgSys) {
        showInformation(msgSys);
        isLoggedIn = true;
    }

    public void disconnectFromPepper(MessageSystem msgSys) {
        if (client != null && isClientConnected && isLoggedIn) {
            showInformation(msgSys);

            appendLogServerCon(" sent", msgSys.getType());

            client.disconnect();
        }
    }

    public void showInformation(Message msgSys) {
        String strMessage;
        if (tvLoginInformation != null) {
            try {
                alertDialogBuilder = new AlertDialog.Builder(mainActivity);

                switch (msgSys.getType()) {
                    case LogOut:
                        alertDialogBuilder.setTitle(mainActivity.getText(R.string.Logged_Out_Title));
                        alertDialogBuilder.setMessage(mainActivity.getText(R.string.Logged_Out_Text));
                        alertDialogBuilder.setPositiveButton(mainActivity.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                        tvLoginInformation.setText(mainActivity.getText(R.string.Logged_Out_Text));
                        tvLoginInformation.setTextColor(Color.parseColor("#FF000000"));
                        break;
                    case Unsuccessful_LogIn:
                        alertDialogBuilder.setTitle(mainActivity.getText(R.string.UnSuc_Login_Title));
                        alertDialogBuilder.setMessage(mainActivity.getText(R.string.UnSuc_Login_Text));
                        alertDialogBuilder.setPositiveButton(mainActivity.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        tvLoginInformation.setText(mainActivity.getText(R.string.UnSuc_Login_Text));
                        tvLoginInformation.setTextColor(Color.parseColor("#b50000"));
                        break;
                    case Successful_LogIn:

                        alertDialogBuilder.setMessage(mainActivity.getText(R.string.Suc_Login_Text));
                        alertDialogBuilder.setTitle(String.valueOf(mainActivity.getText(R.string.Suc_Login_Title)));
                        alertDialogBuilder.setPositiveButton(mainActivity.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });


                        tvLoginInformation.setText(mainActivity.getText(R.string.Suc_Login_Text));
                        tvLoginInformation.setTextColor(Color.parseColor("#00B612"));
                        break;
                    case Error:

                        strMessage = ((MessageSystem) msgSys).getStrSystemNotification();
                        alertDialogBuilder.setTitle(mainActivity.getText(R.string.Error_Title));
                        alertDialogBuilder.setMessage(mainActivity.getText(R.string.Error_Text) + strMessage);
                        alertDialogBuilder.setPositiveButton(mainActivity.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        tvLoginInformation.setText(mainActivity.getText(R.string.Error_Text) + strMessage);
                        tvLoginInformation.setTextColor(Color.parseColor("#b50000"));
                        break;
                    case Suc_IUD:

                        strMessage = ((MessageSystem) msgSys).getStrSystemNotification();
                        alertDialogBuilder.setTitle(mainActivity.getText(R.string.Suc_IUD_Title));
                        alertDialogBuilder.setMessage(mainActivity.getText(R.string.Suc_IUD_Text) + strMessage);

                        etNuTitle.setText("");
                        etNuFirstName.setText("");
                        etNuLastName.setText("");
                        strNewUserPicture = "";
                        spNuRole.setSelection(0);
                        etNuUserName.setText("");
                        etNuPassword.setText("");

                        break;

                }
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            } catch (Exception ex) {
                String err = "";
                err = ex.getMessage();
                err += "";
            }

        }
    }

    /**
     * Fragment_Server
     */


    public void clientLogOut() {
        if (isClientConnected) {
            MessageSystem msgSysTest = new MessageSystem("");
            msgSysTest.setType(MessageType.LogOut);

            client.sendSysMessage(msgSysTest);
        }
    }

    public void testServerConnection() {
        if (isClientConnected) {
            MessageSystem msgSysTest = new MessageSystem("");
            msgSysTest.setType(MessageType.Test);

            client.sendSysMessage(msgSysTest);
        }
    }


    public void appendLogServerCon(String strSoR, MessageType msgType) {

        String strAppendString = "";
        strAppendString += "Last Message Type"+ strSoR +": \n " + msgType.toString();

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

    public int getIntRoleID() {
        return currentUser.getIntRoleID();
    }

    /**
     * Fragment_Profile
     */

    //Profile Controlls
    EditText etProfileLastName, etProfileTitle, etProfileFirstName, etProfileRole, etProfileUserName, etProfilePassword;
    ImageView ivProfilePicture;
    Button btnProfileUpdate;
    RadioGroup rgProfile;
    RadioButton rb_RConfidentalProfile, rb_NConfidentalProfile;

    public void fillProfile() {
        if (currentUser != null && etProfileTitle != null && etProfileFirstName != null && etProfileLastName != null
                && etProfileRole != null && etProfileUserName != null && etProfilePassword != null
                && rb_RConfidentalProfile != null && rb_NConfidentalProfile != null
        ) {
            String strPicture = currentUser.getStrPicture();

            Bitmap bmPicture = StringToBitMap(strPicture);
            if (bmPicture != null) {
                setIBNewPicture(bmPicture, ivProfilePicture);
            }

            etProfileTitle.setText(currentUser.getStrTitle());
            etProfileFirstName.setText(currentUser.getStrFirstname());
            etProfileLastName.setText(currentUser.getStrLastname());

            etProfileUserName.setText(currentUser.getStrUserName());
            etProfilePassword.setText(currentUser.getStrPassword());

            etProfileRole.setText(currentUser.getStrRole());

            if (currentUser.getIntConfidentialID() == 1) {
                rb_RConfidentalProfile.setChecked(true);
            } else {
                rb_NConfidentalProfile.setChecked(true);
            }

        }
    }

    public void clearProfile(){
        if(etProfileTitle != null && etProfileFirstName != null && etProfileLastName != null
                && etProfileRole != null && etProfileUserName != null && etProfilePassword != null
                && rb_RConfidentalProfile != null && rb_NConfidentalProfile != null
        ){
            etProfileTitle.setText("");
            etProfileFirstName.setText("");
            etProfileLastName.setText("");
            etProfileRole.setText("");
            etProfileUserName.setText("");
            etProfilePassword.setText("");

            rb_RConfidentalProfile.setChecked(false);
            rb_NConfidentalProfile.setChecked(false);
            ivProfilePicture.setImageDrawable(null);
        }
    }


    public void updateProfile() {
        try {
            currentUser.setStrTitle(etProfileTitle.getText().toString());
            currentUser.setStrFirstname(etProfileFirstName.getText().toString());
            currentUser.setStrLastname(etProfileLastName.getText().toString());
            currentUser.setStrPassword(etProfilePassword.getText().toString());

            sendUpdateUser(currentUser);

        } catch (Exception ex) {

            Toast.makeText(mainActivity, "Something went wrong with the update! Please try again!", Toast.LENGTH_LONG);

            String err = "";
            err = ex.getMessage();
            err += "";
        }
    }

    /**
     * Patient Information
     */


    public void appendPatientInformation(MessageSystem msgSys) {
        try {
            if (etPatientInformation != null) {

                //String strPatientInfo =

                String strAppendString = decryption.decrypt(msgSys.getStrSystemNotification());

                appendText2EditText(strAppendString, etPatientInformation);

                /** TODO APPEND PATIENt INFORMATION CORRECTLY
                 *
                 */
                /*
                String[] parts = strAppendString.split("\\|");
                for (int i = 0; i< parts.length; i++ ){

                    appendText2EditText(parts[i], etPatientInformation);

                }*/



            } else {
                if (strBufferLogServerCon == null) {
                    strBufferLogServerCon = stringBuffer(decryption.decrypt(msgSys.getStrSystemNotification()), strBufferPatientInfo);
                } else {

                    strBufferLogServerCon += stringBuffer(decryption.decrypt(msgSys.getStrSystemNotification()), strBufferPatientInfo);
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

    public void clearPatientInfo(){
        etPatientInformation.setText("");
    }

    /**
     * Fraggment NewUser
     */

    //New User Controlls
    EditText etNuFirstName, etNuTitle, etNuLastName, etNuPassword, etNuUserName;
    String strNewUserPicture;
    Spinner spNuRole;
    int intNuRoleID, intNuTitleID;
    ImageButton ibNewPicture;
    RadioGroup rg_Nu_Confidential;
    RadioButton rb_Nu_RConfidentalInfo, rb_Nu_NConfidentalInfo;
    //----
    Encryption e = new Encryption();

    public void addNewUser() {
        int intRConfidentialInfoID = -1;
        MessageI msgI = null;
        try {

            int intCheckedID = rg_Nu_Confidential.getCheckedRadioButtonId();
            int idh = (int) spNuRole.getSelectedItemId();

            if (intCheckedID == rb_Nu_NConfidentalInfo.getId()) {
                msgI = new MessageI(e.encrypt(etNuTitle.getText().toString())
                        , e.encrypt(etNuFirstName.getText().toString())
                        , e.encrypt(etNuLastName.getText().toString())

                        , e.encrypt(newUserPictureChecker(strNewUserPicture))

                        ,( (int) spNuRole.getSelectedItemId())+1

                        , e.encrypt(etNuUserName.getText().toString())
                        , e.encrypt(etNuPassword.getText().toString())

                        , 2
                );
            } else if (intCheckedID == rb_Nu_RConfidentalInfo.getId()) {
                msgI = new MessageI(e.encrypt(etNuTitle.getText().toString())
                        , e.encrypt(etNuFirstName.getText().toString())
                        , e.encrypt(etNuLastName.getText().toString())

                        , e.encrypt(newUserPictureChecker(strNewUserPicture))

                        ,( (int) spNuRole.getSelectedItemId())+1

                        , e.encrypt(etNuUserName.getText().toString())
                        , e.encrypt(etNuPassword.getText().toString())

                        , 1
                );

            }
            if (msgI != null) {

                client.sendInsertMessage(msgI);
            }

        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err += "";

            Toast.makeText(mainActivity, "Something went wrong please make sure everything is properly filled out.", Toast.LENGTH_LONG).show();
        }


    }
    public void setNURoles(){
        populateSpinner(spNuRole, arrRoles);

    }

    public String newUserPictureChecker(String strNewUserPicture) {
        String strEmpty = "NoPicture";
        if (strNewUserPicture != null) {
            if (!strNewUserPicture.isEmpty()) {
                return strNewUserPicture;
            } else {
                return strEmpty;
            }

        } else {
            return strEmpty;
        }
    }

    public void clearNewUser() {
        etNuTitle.setText("");
        etNuFirstName.setText("");
        etNuLastName.setText("");
        strNewUserPicture = "";
        spNuRole.setSelection(1);
        etNuUserName.setText("");
        etNuPassword.setText("");
        ibNewPicture.setImageDrawable(null);
    }


    /**
     * UserManagement
     */

    //UserManagment Controlls
    ArrayList<User> allEmployees = new ArrayList<>();
    EditText etUMFirstName, etUMTitle, etUMLastName, etUMPassword, etUMUserName;
    String strUMPicture;
    Spinner spUMRole;
    int intUMRoleID, intUserID, intEmployeeID, intPictureID;
    ImageButton ibUMPicture;
    RadioGroup rgConfidentialUM;
    RadioButton rb_RConfidentialUM, rb_NConfidentialUM;
    User userCurrentSelectedUm;

    ArrayList<String> arrRoles = new ArrayList<>();
    //----

    public void getAllEmployeeData() {
        allEmployees = null;
        MessageSystem msgSys = new MessageSystem("");
        msgSys.setType(MessageType.AllUser);
        client.sendSysMessage(msgSys);

        populateSpinner(spUMRole, arrRoles);
    }

    public void clearUserManagement(){

        allEmployees = null;
        etUMTitle.setText("");
        etUMFirstName.setText("");
        etUMLastName.setText("");
        etUMPassword.setText("");
        etUMUserName.setText("");
        ibUMPicture.setImageDrawable(null);
    }

    public void populateArrayAllUsers(MessageUser msgU) {
        if (allEmployees == null) {
            allEmployees = new ArrayList<>();
        }
            allEmployees.add(
                    messageUserToUser(msgU)
            );
        starFillUserManagement(0);

    }



    public int starFillUserManagement(int intPos) {
        if(allEmployees!=null && allEmployees.size()>=1) {
            if (intPos >= allEmployees.size()) {
                intPos = 0;
            }

            if (intPos < 0) {

                intPos = allEmployees.size()-1;
            }

            userCurrentSelectedUm = allEmployees.get(intPos);
            populateUserManagementControlls(allEmployees.get(intPos));
        }
        return intPos;
    }

    public void deleteEmployee() {

        if (currentUser.getIntEmployeeID() == userCurrentSelectedUm.getIntEmployeeID()) {
            alertDialogBuilder.setTitle("Trying To Delete Yourself");
            alertDialogBuilder.setMessage("You are trying to commit forced Log out");
            alertDialogBuilder.setPositiveButton(mainActivity.getText(R.string.alertD_YES), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(mainActivity, "You have deleted yourself and have been logged out from the system.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mainActivity, "Just kidding. Please don't delete yourself. I..I.. I need you!", Toast.LENGTH_SHORT).show();
                }
            });

            alertDialogBuilder.setNegativeButton(mainActivity.getText(R.string.alertD_NO), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(mainActivity, "You did not delete yourself! Great I had faith in you!", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            alertDialogBuilder.setTitle("Trying To Delete User");
            alertDialogBuilder.setMessage("Are You sure you want to delete this User from the System?");
            alertDialogBuilder.setPositiveButton(mainActivity.getText(R.string.alertD_YES), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {


                    MessageD messageD = new MessageD(userCurrentSelectedUm.getIntEmployeeID()
                            , userCurrentSelectedUm.getIntUserID()
                            , userCurrentSelectedUm.getIntPictureID());
                    client.sendDeleteMessage(messageD);
                }
            });
            alertDialogBuilder.setNegativeButton(mainActivity.getText(R.string.alertD_NO), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    Toast.makeText(mainActivity, "You have deleted this User.", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            allEmployees.remove(userCurrentSelectedUm);
        }

    }

    public void updateEmployee() {



        userCurrentSelectedUm.setStrTitle(etUMTitle.getText().toString());
        userCurrentSelectedUm.setStrFirstname(etUMFirstName.getText().toString());
        userCurrentSelectedUm.setStrLastname(etUMLastName.getText().toString());

        userCurrentSelectedUm.setStrPassword(etUMPassword.getText().toString());


        sendUpdateUser(userCurrentSelectedUm);
    }


    private void populateUserManagementControlls(User user) {
        try {
            etUMTitle.setText(user.getStrTitle());
            etUMFirstName.setText(user.getStrFirstname());
            etUMLastName.setText(user.getStrLastname());
            String strPicture = user.getStrPicture();

            if (!strPicture.substring(0, 9).equals("NoPicture")) {
                setIBNewPicture(
                        StringToBitMap(strPicture)
                        , ibUMPicture
                );
            }

            etUMPassword.setText(user.getStrPassword());
            etUMUserName.setText(user.getStrUserName());

            if (user.getIntRoleID() == 2) {
                spUMRole.setSelection(arrRoles.indexOf("User"));
            } else {

                spUMRole.setSelection(arrRoles.indexOf("Admin"));
            }

            if (user.getIntConfidentialID() == 1) {
                rb_RConfidentialUM.setChecked(true);
            } else {

                rb_NConfidentialUM.setChecked(true);
            }
        }catch (Exception ex){
            String err="";
            err = ex.getMessage();
            err+="";
        }

    }

    /**Fragment Logout
     *
     */




    /**
     * General
     */

    public void populateArrayListRoles(MessageRoles messageRoles) {
        arrRoles.add(messageRoles.getStrRole());
    }

    public void populateSpinner(Spinner spinner, ArrayList arrayList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_spinner_item, arrayList);
        spinner.setAdapter(adapter);
    }

    public void fillCurrentUser(MessageUser msgU) {
        try {
            if (msgU != null) {
                currentUser = messageUserToUser(msgU);
            }
        } catch (Exception ex) {
            String err = ex.getMessage();
            err += "";
        }
    }

    private User messageUserToUser(MessageUser msgU) {
        User user = null;
        try {


            user = new User(msgU.getIntEmployeeID()
                    , decryption.decrypt(msgU.getStrTitle())
                    , decryption.decrypt(msgU.getStrFirstname())
                    , decryption.decrypt(msgU.getStrLastname())

                    , msgU.getIntPictureID()
                    , decryption.decrypt(msgU.getStrPicture())

                    , msgU.getIntRoleID()
                    , decryption.decrypt(msgU.getStrRole())

                    , msgU.getIntUserID()
                    , decryption.decrypt(msgU.getStrUserName())
                    , decryption.decrypt(msgU.getStrPassword())

                    , msgU.getIntConfidentialID()
                    , msgU.getIntGetsConfidentialInfo()
            );
        } catch (Exception ex) {
            String err = ex.getMessage();
            err += "";
        }
        return user;
    }

    public void sendUpdateUser(User user) {
        MessageU msgU = new MessageU(user.getIntEmployeeID()
                , e.encrypt(user.getStrTitle())
                , e.encrypt(user.getStrFirstname())
                , e.encrypt(user.getStrLastname())

                , user.getIntPictureID()
                , e.encrypt(user.getStrPicture())

                , user.getIntUserID()
                , e.encrypt(user.getStrUserName())
                , e.encrypt(user.getStrPassword())

                , user.getIntRoleID()

                , user.getIntConfidentialID()
                , user.getIntGetsConfidentialInfo());

        client.sendUpdateMessage(msgU);
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

    public void setIBNewPicture(Bitmap bmNewUserPicture, ImageButton imageButton) {
        ImageView iv = imageButton;
      //  if (ibNewPicture != null && bmNewUserPicture != null) {
        //    this.ibNewPicture.setImageBitmap(bmNewUserPicture);
        //}

        BitmapDrawable bmpDraw = new BitmapDrawable(mainActivity.getResources(), bmNewUserPicture);
        ibNewPicture.setBackground(bmpDraw);



    }


    public void setIBNewPicture(Bitmap bmNewUserPicture, ImageView imageView) {
        try {
            ImageView iv = imageView;
            if (iv != null && bmNewUserPicture != null) {
                iv.setImageBitmap(bmNewUserPicture);
            }
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err += "";
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            String strSubstring = encodedString.substring(0, 9);
            if (strSubstring.equals("NoPicture")) {
                if (!encodedString.isEmpty()) {
                    byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;

                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * Setters for Controls
     */


    public void setEtLogServerCon(EditText etLogServerCon) {
        if (etLogServerCon != null) {
            this.etLogServerCon = etLogServerCon;
        }
    }

    public void setEtPatientInformation(EditText etPatientInformation) {
        if (etPatientInformation != null) {
            this.etPatientInformation = etPatientInformation;
        }
    }

    public void setEtLoginUsername(EditText etLoginUserName) {
        if (etLoginUserName != null) {
            this.etLoginUserName = etLoginUserName;
        }
    }

    public void setEtLoginPassword(EditText etPassword) {
        if (etPassword != null) {
            this.etLoginPassword = etPassword;
        }
    }

    public void setTvLoginInformation(TextView tvLoginInformation) {
        if (tvLoginInformation != null) {
            this.tvLoginInformation = tvLoginInformation;
        }
    }

    //Profile Setter

    public void setEtProfileFirstName(EditText etProfileFirstName) {
        if (etProfileFirstName != null) {
            this.etProfileFirstName = etProfileFirstName;

        }
    }

    public void setEtProfileLastName(EditText etProfileLastName) {
        if (etProfileLastName != null) {
            this.etProfileLastName = etProfileLastName;

        }
    }

    public void setEtProfileTitle(EditText etProfileTitle) {
        if (etProfileTitle != null) {
            this.etProfileTitle = etProfileTitle;

        }
    }

    public void setEtProfileRole(EditText etProfileRole) {
        if (etProfileRole != null) {
            this.etProfileRole = etProfileRole;
        }
    }


    public void setEtProfileUserName(EditText etProfileUserName) {
        if (etProfileUserName != null) {
            this.etProfileUserName = etProfileUserName;
        }
    }

    public void setEtProfilePassword(EditText etProfilePassword) {
        if (etProfilePassword != null) {
            this.etProfilePassword = etProfilePassword;
        }
    }

    public void setIvProfilePicture(ImageView ivProfilePicture) {
        if (ivProfilePicture != null) {
            this.ivProfilePicture = ivProfilePicture;
        }
    }

    public void setBtnProfileUpdate(Button btnProfileUpdate) {
        if (btnProfileUpdate != null) {
            this.btnProfileUpdate = btnProfileUpdate;
        }
    }

    public void setRgProfile(RadioGroup rgProfile) {
        if (rgProfile != null) {
            this.rgProfile = rgProfile;
        }
    }

    public void setRb_RConfidentalProfile(RadioButton rb_RConfidentalProfile) {
        if (rb_RConfidentalProfile != null) {
            this.rb_RConfidentalProfile = rb_RConfidentalProfile;
        }
    }

    public void setRb_NConfidentalProfile(RadioButton rb_NConfidentalProfile) {
        if (rb_NConfidentalProfile != null) {
            this.rb_NConfidentalProfile = rb_NConfidentalProfile;
        }
    }

    // New User Setters
    public void setEtNuFirstName(EditText etNuFirstName) {
        if (etNuFirstName != null) {
            this.etNuFirstName = etNuFirstName;
        }
    }

    public void setRgConfidential(RadioGroup rg_Nu_Confidential) {
        this.rg_Nu_Confidential = rg_Nu_Confidential;
    }

    public void setRb_Nu_RConfidentalInfo(RadioButton rb_Nu_RConfidentalInfo) {
        this.rb_Nu_RConfidentalInfo = rb_Nu_RConfidentalInfo;
    }

    public void setRb_Nu_NConfidentalInfo(RadioButton rb_Nu_NConfidentalInfo) {
        this.rb_Nu_NConfidentalInfo = rb_Nu_NConfidentalInfo;
    }


    public void setIBNewPicture(ImageButton ibNewPicture) {
        if (ibNewPicture != null) {
            this.ibNewPicture = ibNewPicture;
        }
    }

    public void setEtNuLastName(EditText etNuLastName) {
        if (etNuLastName != null) {
            this.etNuLastName = etNuLastName;
        }
    }

    public void setEtNuPassword(EditText etNuPassword) {
        if (etNuPassword != null) {
            this.etNuPassword = etNuPassword;
        }
    }

    public void setEtNuUserName(EditText etNuUserName) {
        if (etNuUserName != null) {
            this.etNuUserName = etNuUserName;
        }
    }

    public void setStrNewUserPicture(String strNewUserPicture) {
        if (!strNewUserPicture.isEmpty()) {
            this.strNewUserPicture = strNewUserPicture;
        }
    }


    public void setIntNuRoleID(int intNuRoleID) {
        this.intNuRoleID = intNuRoleID;

    }

    public void setIntNuTitleID(int intNuTitleID) {
        this.intNuTitleID = intNuTitleID;
    }

    public void setEtNuTitle(EditText etNuTitle) {
        if (etNuTitle != null) {
            this.etNuTitle = etNuTitle;
        }
    }

    public void setSpNuRole(Spinner spNuRole) {
        if (spNuRole != null) {
            this.spNuRole = spNuRole;
        }
    }

    public void setEtUMFirstName(EditText etUMFirstName) {
        if (etUMFirstName != null) {
            this.etUMFirstName = etUMFirstName;
        }
    }

    public void setUMNewPicture(ImageButton ibUMPicture) {
        if (ibUMPicture != null) {
            this.ibUMPicture = ibUMPicture;
        }
    }


    //User Management Setters

    public void setEtUMTitle(EditText etUMTitle) {
        if (etUMTitle != null) {
            this.etUMTitle = etUMTitle;
        }
    }

    public void setEtUMLastName(EditText etUMLastName) {
        if (etUMLastName != null) {
            this.etUMLastName = etUMLastName;
        }
    }

    public void setEtUMPassword(EditText etUMPassword) {
        if (etUMPassword != null) {
            this.etUMPassword = etUMPassword;
        }
    }

    public void setEtUMUserName(EditText etUMUserName) {
        if (etUMUserName != null) {
            this.etUMUserName = etUMUserName;
        }
    }

    public void setStrUMPicture(String strUMPicture) {
        if (!strNewUserPicture.isEmpty()) {
            this.strUMPicture = strUMPicture;
        }
    }

    public void setSpUMRole(Spinner spUMRole) {
        if (spUMRole != null) {
            this.spUMRole = spUMRole;
        }
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
