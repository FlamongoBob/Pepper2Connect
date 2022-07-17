package com.example.pepper2connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pepper2connect.controller.Controller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Controller controller = new Controller();

    private boolean isCreatedPatient = false;
    private boolean isCreatedLogin = false;
    private boolean isCreatedProfile = false;
    private boolean isCreatedServer = false;

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
                            if (!activeFragment.getTag().equals(frgMng.findFragmentByTag("frgServer").getTag())) {

                                frgMng.beginTransaction().hide(activeFragment).show(frgServer).commit();
                                activeFragment = frgServer;

                                if (!isCreatedServer) {
                                    initiateServerControls();
                                }
                                controller.checkLogServerConBuffer();
                                return true;

                            } else {
                                return false;
                            }
                        case R.id.navigation_Logout:
                            if(controller.isLoggedIn) {
                                alertDialogBuilder.setTitle("Logout");
                                alertDialogBuilder.setMessage("Are you sure, You wanted to Log out ?");
                                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(MainActivity.this, "You will be Logged out now.", Toast.LENGTH_LONG).show();
                                        controller.clientLogOut();
                                    }
                                });
                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "You will stay logged in.", Toast.LENGTH_LONG).show();
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
            err += "\n MEGA NOICE";
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
}