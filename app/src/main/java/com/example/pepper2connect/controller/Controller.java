package com.example.pepper2connect.controller;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

    EditText etLogServerCon, etPatientInformation, etLoginPassword, etLoginUserName;


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

    //Profile Controlls
    EditText etProfileLastName, etProfileTitle, etProfileFirstName, etProfileRole, etProfileConfidentialInfo, etProfileUserName, etProfilePassword;
    ImageView ivProfilePicture;
    Button btnProfileUpdate;
    RadioGroup rgProfile;
    RadioButton rb_RConfidentalProfile, rb_NConfidentalProfile;

    public void fillProfile() {
        if (currentUser != null && etProfileTitle != null && etProfileFirstName != null && etProfileLastName != null
                && etProfileRole != null && etProfileConfidentialInfo != null && etProfileUserName != null && etProfilePassword != null
                && rb_RConfidentalProfile != null && rb_NConfidentalProfile != null
        ) {
            setIBNewPicture(StringToBitMap(currentUser.getStrPicture()), ivProfilePicture);

            etProfileTitle.setText(currentUser.getStrTitle());
            etProfileFirstName.setText(currentUser.getStrFirstname());
            etProfileLastName.setText(currentUser.getStrLastname());
            etProfileUserName.setText(currentUser.getStrUserName());
            etProfilePassword.setText(currentUser.getStrPassword());
            etProfileRole.setText(currentUser.getStrRole());
            etProfileConfidentialInfo.setText(currentUser.getStrFirstname());

            if (currentUser.getIntConfidentialID() == 1) {
                rb_RConfidentalProfile.setChecked(true);
            } else {
                rb_RConfidentalProfile.setChecked(true);
            }

        }
    }


    public void updateProfile() {
        try {
            currentUser.setStrTitle(etProfileTitle.getText().toString());
            currentUser.setStrFirstname(etProfileFirstName.getText().toString());
            currentUser.setStrLastname(etProfileLastName.getText().toString());
            currentUser.setStrPassword(etProfilePassword.getText().toString());

                      /*
            User newUser = new User(currentUser.getIntEmployeeID()
                    , currentUser.getStrTitle()
                    , currentUser.getStrFirstname()
                    , currentUser.getStrLastname()

                    , currentUser.getIntPictureID()
                    , currentUser.getStrPicture()

                    , currentUser.getIntRoleID()
                    , currentUser.getStrRole()

                    , currentUser.getIntUserID()
                    , currentUser.getStrUserName()
                    , currentUser.getStrPassword()

                    , currentUser.getIntConfidentialID()
                    , currentUser.getIntGetsConfidentialInfo()
            );
            */
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
        try {

            int intCheckedID = rgConfidential.getCheckedRadioButtonId();


            if (intCheckedID == rb_NConfidential.getId()) {
                msgI = new MessageI(e.encrypt(etNuTitle.getText().toString())
                        , e.encrypt(etNuFirstName.getText().toString())
                        , e.encrypt(etNuLastName.getText().toString())

                        , e.encrypt(strNewUserPicture)

                        , (int) spRole.getSelectedItemId()

                        , e.encrypt(etNuUserName.getText().toString())
                        , e.encrypt(etNuPassword.getText().toString())

                        , 2
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

                client.sendInsertMessage(msgI);
            }

        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err += "";

            Toast.makeText(mainActivity, "Something went wrong please make sure everything is properly filled out.", Toast.LENGTH_LONG).show();
        }


    }

    public void clearNewUser() {
        etNuTitle.setText("");
        etNuFirstName.setText("");
        etNuLastName.setText("");
        strNewUserPicture = "";
        spRole.setSelection(1);
        etNuUserName.setText("");
        etNuPassword.setText("");
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
        allEmployees =  null;
        MessageSystem msgSys = new MessageSystem("");
        msgSys.setType(MessageType.AllUser);
        client.sendSysMessage(msgSys);

        populateSpinner(spUMRole, arrRoles);
    }

    public void populateArrayAllUsers(MessageUser msgU) {
        if (allEmployees == null) {
            allEmployees = new ArrayList<>();
        } else {
            allEmployees.add(messageUserToUser(msgU));
        }
    }

    public int starFillUserManagement(int intPos) {
        if (intPos >= allEmployees.size()) {
            intPos = 0;
        }

        if (intPos < 0) {

            intPos = 0;
        }

        userCurrentSelectedUm = allEmployees.get(intPos);
        populateUserManagementControlls(allEmployees.get(intPos));
        return intPos;
    }

    public void deleteEmployee() {

        if (currentUser.getIntEmployeeID() == userCurrentSelectedUm.getIntEmployeeID()) {
            alertDialogBuilder.setTitle("Trying To Delete Yourself");
            alertDialogBuilder.setMessage("You are trying to commit forced Log out");
            alertDialogBuilder.setPositiveButton(resources.getText(R.string.alertD_YES), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(mainActivity, "You have deleted yourself and have been logged out from the system.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mainActivity, "Just kidding. Please don't delete yourself. I..I.. I need you!", Toast.LENGTH_SHORT).show();
                }
            });

            alertDialogBuilder.setNegativeButton(resources.getText(R.string.alertD_NO), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(mainActivity, "You did not delete yourself! Great I had faith in you!", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            MessageD messageD = new MessageD(userCurrentSelectedUm.getIntEmployeeID()
                    , userCurrentSelectedUm.getIntUserID()
                    , userCurrentSelectedUm.getIntPictureID());
            client.sendDeleteMessage(messageD);


            allEmployees.remove(userCurrentSelectedUm);
        }

    }

    public void updateEmployee() {

        sendUpdateUser(userCurrentSelectedUm);
    }


    private void populateUserManagementControlls(User user) {
        etUMTitle.setText(user.getStrTitle());
        etUMFirstName.setText(user.getStrFirstname());
        etUMLastName.setText(user.getStrLastname());

        setIBNewPicture(
                StringToBitMap(user.getStrPicture())
                , ibUMPicture
        );

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

    }


    /**
     * General
     */
    /**
     * TODO ON LOGIN
     *
     * @param messageRoles
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
                    , decryption.decrypt(msgU.getStrLastname())

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
                , user.getStrTitle()
                , user.getStrFirstname()
                , user.getStrLastname()

                , user.getIntPictureID()
                , user.getStrPicture()

                , user.getIntUserID()
                , user.getStrUserName()
                , user.getStrPassword()

                , user.getIntRoleID()

                , user.getIntConfidentialID()
                , user.getIntGetsConfidentialInfo());
        client.sendUpadetMessage(msgU);

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
        if (ibNewPicture != null && bmNewUserPicture != null) {
            this.ibNewPicture.setImageBitmap(bmNewUserPicture);
        }
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
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
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

    //Profile Setter

    public void setEtProfileFirstName(EditText etProfileFirstName) {
        this.etProfileFirstName = etProfileFirstName;

    }

    public void setEtProfileLastName(EditText etProfileLastName) {
        this.etProfileLastName = etProfileLastName;

    }

    public void setEtProfileTitle(EditText etProfileTitle) {
        this.etProfileTitle = etProfileTitle;

    }

    public void setEtProfileRole(EditText etProfileRole) {
        this.etProfileRole = etProfileRole;
    }

    public void setEtProfileConfidentialInfo(EditText etProfileConfidentialInfo) {
        this.etProfileConfidentialInfo = etProfileConfidentialInfo;
    }

    public void setEtProfileUserName(EditText etProfileUserName) {
        this.etProfileUserName = etProfileUserName;
    }

    public void setEtProfilePassword(EditText etProfilePassword) {
        this.etProfilePassword = etProfilePassword;
    }

    public void setIvProfilePicture(ImageView ivProfilePicture) {
        this.ivProfilePicture = ivProfilePicture;
    }

    public void setBtnProfileUpdate(Button btnProfileUpdate) {
        this.btnProfileUpdate = btnProfileUpdate;
    }

    public void setRgProfile(RadioGroup rgProfile) {
        this.rgProfile = rgProfile;
    }

    public void setRb_RConfidentalProfile(RadioButton rb_RConfidentalProfile) {
        this.rb_RConfidentalProfile = rb_RConfidentalProfile;
    }

    public void setRb_NConfidentalProfile(RadioButton rb_NConfidentalProfile) {
        this.rb_NConfidentalProfile = rb_NConfidentalProfile;
    }

    // New User Setters
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

    //User Management Setters

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
