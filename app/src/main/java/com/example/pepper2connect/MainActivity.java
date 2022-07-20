package com.example.pepper2connect;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pepper2connect.controller.Controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private final Controller controller = new Controller(this);

    private boolean isCreatedPatient = false;
    private boolean isCreatedLogin = false;
    private boolean isCreatedProfile = false;
    private boolean isCreatedServer = false;

    Resources resources = Resources.getSystem();
    Fragment_Login frgLogin = new Fragment_Login();
    Fragment_Profile frgProfile = new Fragment_Profile(this, controller);
    Fragment_PatientInformation frgPatient = new Fragment_PatientInformation();
    Fragment_ServerConnection frgServer = new Fragment_ServerConnection();

    Fragment_NewUser fragment_newUser = new Fragment_NewUser();
    Fragment_UserManagement fragment_userManagement = new Fragment_UserManagement();

    public FragmentManager frgMng = getSupportFragmentManager();
    Fragment activeFragment = frgLogin;

    Button btnTestConnection, btnLogin;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private int intRequestCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            alertDialogBuilder = new AlertDialog.Builder(this);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);

            frgMng.beginTransaction().add(R.id.container, frgServer, "frgServer").hide(frgServer).commit();
            frgMng.beginTransaction().add(R.id.container, frgPatient, "frgPatient").hide(frgPatient).commit();
            frgMng.beginTransaction().add(R.id.container, frgProfile, "frgProfile").hide(frgProfile).commit();
            frgMng.beginTransaction().add(R.id.container, fragment_newUser, "frgNewUser").hide(fragment_newUser).commit();
            frgMng.beginTransaction().add(R.id.container, fragment_userManagement, "frgUserManagement").hide(fragment_userManagement).commit();
            frgMng.beginTransaction().add(R.id.container, frgLogin, "frgLogin").commit();

            bottomNavigationView.setOnItemSelectedListener(item -> {
                if (activeFragment.getTag().equals(frgMng.findFragmentByTag("frgNewUser").getTag())) {
                    frgMng.beginTransaction().hide(activeFragment).commit();
                }
                switch (item.getItemId()) {
                    case R.id.navigation_Login:
                        if (!activeFragment.getTag().equals(frgMng.findFragmentByTag("frgLogin").getTag())) {
                            frgMng.beginTransaction().hide(activeFragment).show(frgLogin).commit();
                            activeFragment = frgLogin;
                            if (!isCreatedLogin) {
                                initiateLoginControls();
                            }
                            return true;
                        }
                        break;

                    case R.id.navigation_PatientInformation:
                        if (!activeFragment.getTag().equals(frgMng.findFragmentByTag("frgPatient").getTag())) {
                            if (controller.isLoggedIn) {
                                frgMng.beginTransaction().hide(activeFragment).show(frgPatient).commit();
                                activeFragment = frgPatient;

                                if (!isCreatedPatient) {
                                    initiatePatientControls();
                                }
                                controller.checkPatientInfoBuffer();

                                return true;
                            } else {
                                alertDialogBuilder.setTitle(resources.getText(R.string.Not_Logged_In_Title));
                                alertDialogBuilder.setMessage(resources.getText(R.string.Not_Logged_In_Text));
                                alertDialogBuilder.setPositiveButton(resources.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(MainActivity.this, resources.getText(R.string.Page_not_Changed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                return false;
                            }
                        } else {
                            return false;
                        }
                    case R.id.navigation_Profile:
                        if (!activeFragment.getTag().equals(frgMng.findFragmentByTag("frgProfile").getTag())) {
                            if (controller.isLoggedIn) {
                                frgMng.beginTransaction().hide(activeFragment).show(frgProfile).commit();
                                activeFragment = frgProfile;

                                if (!isCreatedProfile) {
                                    initiateProfileControls();
                                }

                                controller.fillProfile();

                                return true;
                            } else {
                                alertDialogBuilder.setTitle(resources.getText(R.string.Not_Logged_In_Title));
                                alertDialogBuilder.setMessage(resources.getText(R.string.Not_Logged_In_Text));
                                alertDialogBuilder.setPositiveButton(resources.getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(MainActivity.this, resources.getText(R.string.Page_not_Changed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                return false;
                            }
                        } else {
                            return false;
                        }
                    case R.id.navigation_ServerConnection:
                        if (!activeFragment.getTag().equals(frgMng.findFragmentByTag("frgServer").getTag())) {

                            frgMng.beginTransaction().hide(activeFragment).show(frgServer).commit();
                            activeFragment = frgServer;

                            if (!isCreatedServer) {
                                initiateServerControls();
                            }

                            ImageView iv = findViewById(R.id.iv_Robot);
                            if (controller.isClientConnected) {
                                iv.setColorFilter(getColor(R.color.connected_Green));
                            } else {

                                iv.setColorFilter(getColor(R.color.disconnected_red));
                            }
                            controller.checkLogServerConBuffer();
                            return true;

                        } else {
                            return false;
                        }
                    case R.id.navigation_Logout:
                        if (controller.isLoggedIn) {
                            alertDialogBuilder.setTitle(resources.getText(R.string.Log_Out_Title));
                            alertDialogBuilder.setMessage(resources.getText(R.string.Log_Out_Text));
                            alertDialogBuilder.setPositiveButton(resources.getText(R.string.alertD_YES), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Toast.makeText(MainActivity.this, resources.getText(R.string.Yes_Log_Out_Text), Toast.LENGTH_LONG).show();
                                    controller.clientLogOut();
                                    frgMng.beginTransaction().hide(activeFragment).show(frgLogin).commit();
                                }
                            });
                            alertDialogBuilder.setNegativeButton(resources.getText(R.string.alertD_NO), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, resources.getText(R.string.No_Log_Out_Text), Toast.LENGTH_LONG).show();
                                }
                            });

                            alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }

                        break;
                }
                return false;
            });
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err += "\n ";
        }
    }

    public void initiateServerControls() {
        try {


            btnTestConnection = findViewById(R.id.btnTestConnection);
            btnTestConnection.setOnClickListener(view -> controller.testServerConnection());

            EditText etLogServerCon = findViewById(R.id.etLogServerCon);
            etLogServerCon.setFocusable(false);
            etLogServerCon.setKeyListener(null);

            controller.setEtLogServerCon(etLogServerCon);
            isCreatedServer = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            isCreatedServer = false;
        }
    }

    public void initiatePatientControls() {
        try {
            EditText etPatientInformation = findViewById(R.id.etPatientInformation);
            etPatientInformation.setFocusable(false);
            etPatientInformation.setKeyListener(null);
            controller.setEtPatientInformation(etPatientInformation);
            isCreatedPatient = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            isCreatedPatient = false;
        }
    }

    public void initiateProfileControls() {
        try {


            ImageView ivProfilePicture = findViewById(R.id.ivProfilePicture);
            ivProfilePicture.setFocusable(false);
            controller.setIvProfilePicture(ivProfilePicture);

            EditText etProfileTitle = findViewById(R.id.etProfileTitle);
            //etProfileTitle.setKeyListener(null);
            //etProfileTitle.setFocusable(false);
            controller.setEtProfileTitle(etProfileTitle);

            EditText etProfileFirstName = findViewById(R.id.etProfileFirstName);
            //etProfileFirstName.setKeyListener(null);
           //etProfileFirstName.setFocusable(false);
            controller.setEtProfileFirstName(etProfileFirstName);

            EditText etProfileLastName = findViewById(R.id.etProfileLastName);
            //etProfileLastName.setKeyListener(null);
            //etProfileLastName.setFocusable(false);
            controller.setEtProfileLastName(etProfileLastName);

            EditText etProfileRole = findViewById(R.id.etProfileRole);
            etProfileRole.setKeyListener(null);
            etProfileRole.setFocusable(false);
            controller.setEtProfileRole(etProfileRole);


            EditText etProfileUserName = findViewById(R.id.etProfileUserName);
            etProfileUserName.setKeyListener(null);
            etProfileUserName.setFocusable(false);
            controller.setEtProfileUserName(etProfileUserName);

            EditText etProfilePassword = findViewById(R.id.etProfilePassword);
            //etProfilePassword.setKeyListener(null);
            //etProfilePassword.setFocusable(false);
            controller.setEtProfilePassword(etProfilePassword);

            RadioButton rb_NConfidentalProfile = findViewById(R.id.rb_NConfidentalProfile);
            rb_NConfidentalProfile.setKeyListener(null);
            rb_NConfidentalProfile.setFocusable(false);
            controller.setRb_NConfidentalProfile(rb_NConfidentalProfile);

            RadioButton rb_RConfidentalProfile = findViewById(R.id.rb_RConfidentalProfile);
            rb_RConfidentalProfile.setKeyListener(null);
            rb_RConfidentalProfile.setFocusable(false);
            controller.setRb_RConfidentalProfile(rb_RConfidentalProfile);

            Button btnProfileUpdate = findViewById(R.id.btnProfileUpdate);
            btnProfileUpdate.setOnClickListener(view -> controller.updateProfile());

            isCreatedProfile = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            isCreatedProfile = false;
        }
    }

    public void initiateLoginControls() {
        try {
            btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(view -> {
                EditText etLoginUserName = findViewById(R.id.etLoginUserName);
                controller.setEtLoginUsername(etLoginUserName);
                EditText etLoginPassword = findViewById(R.id.etLoginPassword);
                controller.setEtLoginPassword(etLoginPassword);
                TextView tvLoginInformation = findViewById(R.id.tvLoginInformation);
                controller.setTvLoginInformation(tvLoginInformation);

                controller.connect2Pepper(etLoginUserName.getText().toString(), etLoginPassword.getText().toString());
            });
            isCreatedLogin = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            isCreatedLogin = false;
        }
    }


    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    // Handled permission Result

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(MainActivity.this);
                }
                break;
        }
    }


    // function to let's the user to choose image from camera or gallery
    public void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, (dialogInterface, i) -> {
            if (optionsMenu[i].equals("Take Photo")) {
                // Open the camera and get the photo
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra("requestCode", 2);
                intRequestCode = 2;
                mLauncher.launch(takePicture);
                // startActivityForResult(takePicture, 0);
            } else if (optionsMenu[i].equals("Choose from Gallery")) {
                // choose from  external storage
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // pickPhoto.putExtra("requestCode", 1);
                intRequestCode = 1;
                mLauncher.launch(pickPhoto);
                //startActivityForResult(pickPhoto , 1);
            } else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }


            }
    );


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (intRequestCode) {
                case 2:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            Bundle extras = data.getExtras();
                            Bitmap selectedImage = (Bitmap) extras.get("data");

                            controller.setStrNewUserPicture(controller.BitMapToString(selectedImage));

                            controller.setIBNewPicture((Bitmap) data.getExtras().get("data"), findViewById(R.id.ibNewPicture));


                        } catch (Exception ex) {
                            String err = "";
                            err = ex.getMessage();
                            err += "";
                        }
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            Uri selectedImage = data.getData();

                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            if (selectedImage != null) {
                                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    String picturePath = cursor.getString(columnIndex);

                                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

                                    controller.setStrNewUserPicture(controller.BitMapToString(bitmap));

                                    controller.setIBNewPicture(bitmap, findViewById(R.id.ibNewPicture));
                                    cursor.close();
                                }
                            }
                        } catch (Exception ex) {
                            String err = "";
                            err = ex.getMessage();
                            err += "";
                        }
                    }
                    break;
            }
        }
    }
}