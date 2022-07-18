package com.example.pepper2connect;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pepper2connect.controller.Controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Controller controller = new Controller();

    private boolean isCreatedPatient = false;
    private boolean isCreatedLogin = false;
    private boolean isCreatedProfile = false;
    private boolean isCreatedServer = false;

    Resources resources = Resources.getSystem();
    Fragment_Login frgLogin = new Fragment_Login();
    Fragment_Profile frgProfile = new Fragment_Profile(this, controller);
    Fragment_PatientInformation frgPatient = new Fragment_PatientInformation();
    Fragment_ServerConnection frgServer = new Fragment_ServerConnection();
    public FragmentManager frgMng = getSupportFragmentManager();
    Fragment activeFragment = frgLogin;

    Button btnTestConnection, btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (controller == null) {
            controller = new Controller();
        }

        try {
            alertDialogBuilder = new AlertDialog.Builder(this);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);

            frgMng.beginTransaction().add(R.id.container, frgServer, "frgServer").hide(frgServer).commit();
            frgMng.beginTransaction().add(R.id.container, frgPatient, "frgPatient").hide(frgPatient).commit();
            frgMng.beginTransaction().add(R.id.container, frgProfile, "frgProfile").hide(frgProfile).commit();
            frgMng.beginTransaction().add(R.id.container, frgLogin, "frgLogin").commit();

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

                                    controller.fillProfile(frgProfile);
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
                }
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
            EditText etProfileTitle = findViewById(R.id.etProfileTitle);
            etProfileTitle.setKeyListener(null);
            etProfileTitle.setFocusable(false);
            controller.setEtProfileTitle(etProfileTitle);

            EditText etProfileFirstName = findViewById(R.id.etProfileFirstName);
            etProfileFirstName.setKeyListener(null);
            etProfileFirstName.setFocusable(false);
            controller.setEtProfileFirstName(etProfileFirstName);

            EditText etProfileLastName = findViewById(R.id.etProfileLastName);
            etProfileLastName.setKeyListener(null);
            etProfileFirstName.setFocusable(false);
            controller.setEtProfileLastName(etProfileLastName);


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
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText etLoginUserName = findViewById(R.id.etLoginUserName);
                    controller.setEtLoginUsername(etLoginUserName);
                    EditText etLoginPassword = findViewById(R.id.etLoginPassword);
                    controller.setEtLoginPassword(etLoginPassword);
                    TextView tvLoginInformation = findViewById(R.id.tvLoginInformation);
                    controller.setTvLoginInformation(tvLoginInformation);

                    controller.connect2Pepper(etLoginUserName.getText().toString(), etLoginPassword.getText().toString());
                }
            });
            isCreatedLogin = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            isCreatedLogin = false;
        }
    }

    public FragmentManager getFrgMng() {
        return frgMng;
    }

    public Fragment getActiveFragment() {
        return activeFragment;
    }

    public void setActiveFragment(Fragment activeFragment) {
        this.activeFragment = activeFragment;
    }

    public Controller getController() {
        return controller;
    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    public void imageCapturer() {
        Intent image = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //requestPermissionLauncher.launch("Camera Permission");
        //activityGetImage.launch(image);
        checkPermission();
    }

    public void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        activityGetImage.launch(i);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ActivityResultLauncher<Intent> activityGetImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        ImageButton ibNewPicture = findViewById(R.id.ibNewPicture);
        if (result.getResultCode()
                == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null
                    && data.getData() != null) {
                Uri selectedImageUri = data.getData();


                controller.setStrNewUserPicture(selectedImageUri.toString());

                Bitmap selectedImageBitmap;
                try {
                    selectedImageBitmap
                            = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            selectedImageUri);
                    ibNewPicture.setImageBitmap(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    class RequestCode {
        static final int INTERNAL_STORAGE = 100;
        static final int EXTERNAL_STORAGE = 101;
        static final int CAMERA_PERMISSION_CODE = 102;
    }

}