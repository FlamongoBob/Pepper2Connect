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
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pepper2connect.Service.LocalService;
import com.example.pepper2connect.controller.Controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final Controller controller = new Controller(this);

    private BottomNavigationView bottomNavigationView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    Fragment_Login frgLogin = new Fragment_Login(controller,this);
    Fragment_Profile frgProfile = new Fragment_Profile(this, controller);
    Fragment_PatientInformation frgPatient = new Fragment_PatientInformation(controller);
    Fragment_ServerConnection frgServer = new Fragment_ServerConnection(controller);

    Fragment_NewUser fragment_newUser = new Fragment_NewUser(controller, this);
    Fragment_UserManagement fragment_userManagement = new Fragment_UserManagement(controller, this);

    public FragmentManager frgMng = getSupportFragmentManager();
    Fragment activeFragment = frgLogin;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private int intRequestCode = -1;

    LocalService mService;
    boolean mBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions(this);
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
                try {
                    if (activeFragment.getTag().equals(frgMng.findFragmentByTag("frgNewUser").getTag())) {
                        frgMng.beginTransaction().hide(activeFragment).commit();
                    }

                    switch (item.getItemId()) {
                        case R.id.navigation_Login:
                            if (!controller.isLoggedIn) {
                                if (!activeFragment.getTag().equals(frgMng.findFragmentByTag("frgLogin").getTag())) {
                                    frgMng.beginTransaction().hide(activeFragment).show(frgLogin).commit();
                                    activeFragment = frgLogin;

                                    return true;
                                }
                                break;
                            } else {
                                alertDialogBuilder.setTitle("Already Logged IN");
                                alertDialogBuilder.setMessage(getText(R.string.Not_Logged_In_Text));
                                alertDialogBuilder.setPositiveButton(getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(MainActivity.this, getText(R.string.Page_not_Changed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        case R.id.navigation_PatientInformation:
                            if (!activeFragment.getTag().equals(frgMng.findFragmentByTag("frgPatient").getTag())) {
                                if (controller.isLoggedIn) {
                                    frgMng.beginTransaction().hide(activeFragment).show(frgPatient).commit();
                                    activeFragment = frgPatient;

                                    controller.checkPatientInfoBuffer();

                                    return true;
                                } else {
                                    alertDialogBuilder.setTitle(getText(R.string.Not_Logged_In_Title));
                                    alertDialogBuilder.setMessage(getText(R.string.Not_Logged_In_Text));
                                    alertDialogBuilder.setPositiveButton(getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Toast.makeText(MainActivity.this, getText(R.string.Page_not_Changed), Toast.LENGTH_SHORT).show();
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

                                    controller.clearProfile();
                                    controller.fillProfile();

                                    return true;
                                } else {
                                    alertDialogBuilder.setTitle(getText(R.string.Not_Logged_In_Title));
                                    alertDialogBuilder.setMessage(getText(R.string.Not_Logged_In_Text));
                                    alertDialogBuilder.setPositiveButton(getText(R.string.alertD_OK), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Toast.makeText(MainActivity.this, getText(R.string.Page_not_Changed), Toast.LENGTH_SHORT).show();
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

                                alertDialogBuilder.setTitle(this.getText(R.string.Log_Out_Title));
                                alertDialogBuilder.setMessage(this.getText(R.string.Log_Out_Text));
                                alertDialogBuilder.setPositiveButton(this.getText(R.string.alertD_YES), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(MainActivity.this, getText(R.string.Yes_Log_Out_Text), Toast.LENGTH_LONG).show();
                                        controller.clientLogOut();
                                        controller.clearProfile();

                                        if(controller.getIntRoleID() == 1){
                                            frgProfile.intPos = 0;
                                            controller.clearUserManagement();
                                            controller.clearNewUser();
                                        }
                                        controller.clearPatientInfo();

                                        unbindService(connection);
                                        frgMng.beginTransaction().hide(activeFragment).show(frgLogin).commit();

                                    }
                                });
                                alertDialogBuilder.setNegativeButton(this.getText(R.string.alertD_NO), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, getText(R.string.No_Log_Out_Text), Toast.LENGTH_LONG).show();
                                    }
                                });

                                alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }

                            break;
                    }
                } catch (Exception ex) {
                    String err = "";
                    err = ex.getMessage();
                    err += "";
                }
                return false;
            });

        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err += "\n ";
        }
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context
                , Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int cameraPermission = ContextCompat.checkSelfPermission(context
                , Manifest.permission.CAMERA);

        int intNotificationPermission =ContextCompat.checkSelfPermission(context
                , Manifest.permission.POST_NOTIFICATIONS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (intNotificationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.POST_NOTIFICATIONS);
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

                if (activeFragment.getTag().equals(frgMng.findFragmentByTag("frgNewUser").getTag())
                        | activeFragment.getTag().equals(frgMng.findFragmentByTag("frgUserManagement").getTag())) {

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

    public void backToLogin(){
        frgMng.beginTransaction().hide(activeFragment).show(frgLogin).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try{
            Intent intent = new Intent(this, LocalService.class);
            startService(intent);
            this.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        }catch(Exception ex){
            String err ="";
            err =ex.getMessage();
            err +="";

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService.appClosedDisconnect();

        mBound = false;
        unbindService(connection);

    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public Fragment_Login getFrgLogin() {
        return frgLogin;
    }

    public Fragment_Profile getFrgProfile() {
        return frgProfile;
    }

    public Fragment_PatientInformation getFrgPatient() {
        return frgPatient;
    }

    public Fragment_ServerConnection getFrgServer() {
        return frgServer;
    }



}