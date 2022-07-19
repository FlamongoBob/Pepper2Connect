package com.example.pepper2connect;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.pepper2connect.controller.Controller;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Fragment_Profile extends Fragment {
    MainActivity mainActivity;
    Controller controller;

    Resources resources = Resources.getSystem();
    FragmentManager frgMng_Profile;
    Fragment activeFragment_frgMngProfile;
    Fragment_NewUser fragment_newUser;// = new Fragment_NewUser();
    private boolean isCreatedNewUser = false;
    private boolean isCreatedUserManagement = false;


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    public Fragment_Profile(MainActivity mainActivity, Controller controller) {
        // Required empty public constructor
        this.mainActivity = mainActivity;
        this.controller = controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container
            , Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_admin_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Toast.makeText(mainActivity, resources.getText(R.string.last_message_rec_Text), Toast.LENGTH_SHORT).show();

      /*  if (activeFragment == null) {
            activeFragment = mainActivity.getActiveFragment();
        }

        if (frgMng == null) {
            frgMng = mainActivity.getFrgMng();
           // frgMng.beginTransaction().add(R.id.container, fragment_newUser, "frgNewUser").hide(fragment_newUser).commit();
        }
        */


        switch (item.getItemId()) {
            case R.id.New_User:
                try {
                    if (!mainActivity.activeFragment.getTag().equals(mainActivity.frgMng.findFragmentByTag("frgNewUser").getTag())) {
                        mainActivity.frgMng.beginTransaction().hide(mainActivity.activeFragment).show(mainActivity.fragment_newUser).commit();
                        mainActivity.activeFragment = mainActivity.fragment_newUser;
                        //mainActivity.setActiveFragment(activeFragment);// = fragment_newUser;
                        // mainActivity.setFrgMng(frgMng);

                        if (!isCreatedNewUser) {
                            initiateNewUserControls();
                        }
                        return true;
                    }


                    return false;
                } catch (Exception ex) {
                    String err = ex.getMessage();
                    err += "";
                }
            case R.id.UserManagement:
                try {
                    if (!mainActivity.activeFragment.getTag().equals(mainActivity.frgMng.findFragmentByTag("frgUserManagement").getTag())) {
                        mainActivity.frgMng.beginTransaction().hide(mainActivity.activeFragment).show(mainActivity.fragment_userManagement).commit();
                        mainActivity.activeFragment = mainActivity.fragment_userManagement;

                        if (!isCreatedUserManagement) {
                            initiateUserManagementControls();
                        }
                        return true;
                    }
                    return false;
                } catch (Exception ex) {
                    String err = ex.getMessage();
                    err += "";
                }
            default:

                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateNewUserControls() {
        ImageButton ibNewPicture = mainActivity.findViewById(R.id.ibNewPicture);
        controller.setIBNewPicture(ibNewPicture);
        ibNewPicture.setOnClickListener(view -> {
            if (mainActivity.checkAndRequestPermissions(mainActivity)) {
                mainActivity.chooseImage(mainActivity);
            }
        });

        EditText etNuFirstName = mainActivity.findViewById(R.id.etNuFirstName);
        controller.setEtNuFirstName(etNuFirstName);

        EditText etNuLastName = mainActivity.findViewById(R.id.etNuLastName);
        controller.setEtNuLastName(etNuLastName);

        EditText etNuPassword = mainActivity.findViewById(R.id.etNuPassword);
        controller.setEtNuPassword(etNuPassword);

        EditText etNuUserName = mainActivity.findViewById(R.id.etNuUserName);
        controller.setEtNuUserName(etNuUserName);

        EditText etNuTitle = mainActivity.findViewById(R.id.etNuTitle);
        controller.setEtNuTitle(etNuTitle);

        Spinner spNuRole = mainActivity.findViewById(R.id.spNuRole);
        controller.setSpRole(spNuRole);

        Button btnAddNewUser = mainActivity.findViewById(R.id.btnAddNewUser);
        btnAddNewUser.setOnClickListener(view -> {
            controller.addNewUser();
        });

        Button btnNewUserClear = mainActivity.findViewById(R.id.btnNewUserClear);
        btnNewUserClear.setOnClickListener(view -> {
            controller.clearNewUser();
        });

    }

    /**
     * TODO MESSAGE GET ALL EMPLOYEES
     */

    private void initiateUserManagementControls() {


        ImageButton ibNewPicture = mainActivity.findViewById(R.id.ibNewPicture);
        controller.setIBNewPicture(ibNewPicture);
        ibNewPicture.setOnClickListener(view -> {
            if (mainActivity.checkAndRequestPermissions(mainActivity)) {
                mainActivity.chooseImage(mainActivity);
            }
        });

        EditText etUMFirstName = mainActivity.findViewById(R.id.etUMFirstName);
        controller.setEtUMFirstName(etUMFirstName);

        EditText etUMLastName = mainActivity.findViewById(R.id.etUMLastName);
        controller.setEtUMLastName(etUMLastName);

        EditText etUMPassword = mainActivity.findViewById(R.id.etUMPassword);
        controller.setEtUMPassword(etUMPassword);

        EditText etUMUserName = mainActivity.findViewById(R.id.etUMUserName);
         controller.setEtUMUserName(etUMUserName);

        EditText etUMTitle = mainActivity.findViewById(R.id.etUMTitle);
         controller.setEtUMTitle(etUMTitle);

        Spinner spUMRole = mainActivity.findViewById(R.id.spUMRole);
         controller.setSpUMRole(spUMRole);

        Button btnUMSaveChanges = mainActivity.findViewById(R.id.btnUMSaveChanges);
        btnUMSaveChanges.setOnClickListener(view -> {
            //controller.addNewUser();
        });

        Button btnUMDeleteUser = mainActivity.findViewById(R.id.btnUMDeleteUser);
        btnUMDeleteUser.setOnClickListener(view -> {
            // controller.clearNewUser();
        });

        Button btnUMPrevious = mainActivity.findViewById(R.id.btnUMPrevious);
        btnUMPrevious.setOnClickListener(view -> {
            // controller.clearNewUser();
        });

        Button btnUMNext = mainActivity.findViewById(R.id.btnUMNext);
        btnUMNext.setOnClickListener(view -> {
            // controller.clearNewUser();
        });

    }


    public void setFragment_newUser(Fragment_NewUser fragment_newUser) {
        this.fragment_newUser = fragment_newUser;
    }
}