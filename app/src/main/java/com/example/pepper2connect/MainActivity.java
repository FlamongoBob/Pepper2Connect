package com.example.pepper2connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pepper2connect.controller.Controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Controller controller;
    private boolean isCreatedPatient=false;
    private boolean isCreatedLogin=false;
    private boolean isCreatedProfile=false;
    private boolean isCreatedServer=false;

    Fragment frgLogin = new Fragment_Login();
    Fragment frgProfile = new Fragment_Profile();
    Fragment frgPatient = new Fragment_PatientInformation();
    Fragment frgServer = new Fragment_ServerConnection();
    FragmentManager frgMng = getSupportFragmentManager();
    Fragment active = frgLogin;

    Button btnTestConnection, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (controller == null) {
            controller = new Controller();
        }
        initiateControlls();

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
                            if (!active.getTag().equals("frgLogin")) {
                                frgMng.beginTransaction().hide(active).show(frgLogin).commit();
                                active = frgLogin;
                                if(!isCreatedLogin) {
                                    initiateCreateLogin();
                                    initiateCreateServer();
                                }
                                return true;
                            }
                        case R.id.navigation_PatientInformation:
                            if (!active.getTag().equals("frgPatient")) {
                                if (controller.isLoggedIn) {
                                    frgMng.beginTransaction().hide(active).show(frgPatient).commit();
                                    active = frgPatient;

                                    if(!isCreatedPatient) {
                                        initiateCreatePatient();
                                    }
                                    return true;
                                } else {
                                    alertDialogBuilder.setTitle("Not Logged In");
                                    alertDialogBuilder.setMessage("Please login before moving on to other pages.");
                                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Toast.makeText(MainActivity.this, "Page has not been changed", Toast.LENGTH_SHORT).show();
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
                            if (!active.getTag().equals("frgProfile")) {
                                if (controller.isLoggedIn) {
                                    frgMng.beginTransaction().hide(active).show(frgProfile).commit();
                                    active = frgProfile;

                                    if(!isCreatedProfile) {
                                        initiateCreateProfile();
                                    }
                                    return true;
                                } else {
                                    alertDialogBuilder.setTitle("Not Logged In");
                                    alertDialogBuilder.setMessage("Please login before moving on to other pages.");
                                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Toast.makeText(MainActivity.this, "Page has not been changed", Toast.LENGTH_SHORT).show();
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
                            if (!active.getTag().equals("frgServer")) {

                                frgMng.beginTransaction().hide(active).show(frgServer).commit();
                                active = frgServer;

                                if(!isCreatedServer) {
                                    initiateCreateServer();
                                }
                                return true;

                            } else {
                                return false;
                            }
                    }
                    return false;
                }
            });
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err += "\n MEGA NOICE";
        }
    }
    private void initiateControlls(){

    }

    public void initiateCreateServer() {
        try {
            btnTestConnection = findViewById(R.id.btnTestConnection);
            btnTestConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    controller.testServerConnection();
                }
            });

            isCreatedServer = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            isCreatedServer = false;
        }
    }
    public void initiateCreatePatient(){
        try {

            isCreatedPatient = true;
        }catch (Exception ex){
            String err = "";
            err = ex.getMessage();
            isCreatedPatient = false;
        }
    }
    public void initiateCreateProfile() {
        try {

            isCreatedProfile = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            isCreatedProfile = false;
        }
    }

    public void initiateCreateLogin() {
        try {
            btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //controller.connect2Peppper();
                }
            });
            isCreatedLogin = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            isCreatedLogin = false;
        }
    }

    public Controller getController() {
        return controller;
    }
}