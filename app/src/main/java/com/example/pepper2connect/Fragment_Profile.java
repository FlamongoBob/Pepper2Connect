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
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.pepper2connect.controller.Controller;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Fragment_Profile extends Fragment {
    MainActivity mainActivity;
    Controller controller;

    int intPos = -1;

    private boolean isCreatedNewUser = false;
    private boolean isCreatedUserManagement = false;


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
        View vRoots = inflater.inflate(R.layout.fragment_profile, container, false);

        initiateProfileControls(vRoots);
        // Inflate the layout for this fragment
        return vRoots;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_admin_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
          switch (item.getItemId()) {
            case R.id.New_User:
                try {
                    if (controller.getIntRoleID() == 1) {
                    if (!mainActivity.activeFragment.getTag().equals(mainActivity.frgMng.findFragmentByTag("frgNewUser").getTag())) {
                        mainActivity.frgMng.beginTransaction().hide(mainActivity.activeFragment).show(mainActivity.fragment_newUser).commit();
                        mainActivity.activeFragment = mainActivity.fragment_newUser;
                        controller.setNURoles();
                        return true;
                    }

                }else {
                return false;
            }


                    return false;
                } catch (Exception ex) {
                    String err = ex.getMessage();
                    err += "";
                }
                break;
            case R.id.Profile:
                try {
                    if (controller.getIntRoleID() == 1) {
                        if (!mainActivity.activeFragment.getTag().equals(mainActivity.frgMng.findFragmentByTag("frgProfile").getTag())) {
                            mainActivity.frgMng.beginTransaction().hide(mainActivity.activeFragment).show(mainActivity.frgProfile).commit();
                            mainActivity.activeFragment = mainActivity.frgProfile;

                            controller.fillProfile();
                            return true;
                        }
                    }else {
                        return false;
                    }

                } catch (Exception ex) {
                    String err = ex.getMessage();
                    err += "";
                }
                break;
            case R.id.UserManagement:
                try {
                    if (controller.getIntRoleID() == 1) {
                        if (!mainActivity.activeFragment.getTag().equals(mainActivity.frgMng.findFragmentByTag("frgUserManagement").getTag())) {
                            mainActivity.frgMng.beginTransaction().hide(mainActivity.activeFragment).show(mainActivity.fragment_userManagement).commit();
                            mainActivity.activeFragment = mainActivity.fragment_userManagement;

                            controller.getAllEmployeeData();

                            intPos = controller.starFillUserManagement(0);
                            return true;
                        }

                    }else {
                        return false;
                    }
                } catch (Exception ex) {
                    String err = ex.getMessage();
                    err += "";
                }
                break;
            default:

                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void initiateProfileControls(View vRoot) {
        try {
            ImageView ivProfilePicture = vRoot.findViewById(R.id.ivProfilePicture);
            ivProfilePicture.setFocusable(false);
            controller.setIvProfilePicture(ivProfilePicture);

            EditText etProfileTitle = vRoot.findViewById(R.id.etProfileTitle);
            //etProfileTitle.setKeyListener(null);
            //etProfileTitle.setFocusable(false);
            controller.setEtProfileTitle(etProfileTitle);

            EditText etProfileFirstName = vRoot.findViewById(R.id.etProfileFirstName);
            //etProfileFirstName.setKeyListener(null);
            //etProfileFirstName.setFocusable(false);
            controller.setEtProfileFirstName(etProfileFirstName);

            EditText etProfileLastName = vRoot.findViewById(R.id.etProfileLastName);
            //etProfileLastName.setKeyListener(null);
            //etProfileLastName.setFocusable(false);
            controller.setEtProfileLastName(etProfileLastName);

            EditText etProfileRole = vRoot.findViewById(R.id.etProfileRole);
            etProfileRole.setKeyListener(null);
            etProfileRole.setFocusable(false);
            controller.setEtProfileRole(etProfileRole);


            EditText etProfileUserName = vRoot.findViewById(R.id.etProfileUserName);
            etProfileUserName.setKeyListener(null);
            etProfileUserName.setFocusable(false);
            controller.setEtProfileUserName(etProfileUserName);

            EditText etProfilePassword = vRoot.findViewById(R.id.etProfilePassword);
            //etProfilePassword.setKeyListener(null);
            //etProfilePassword.setFocusable(false);
            controller.setEtProfilePassword(etProfilePassword);

            RadioButton rb_NConfidentalProfile = vRoot.findViewById(R.id.rb_NConfidentalProfile);
            rb_NConfidentalProfile.setKeyListener(null);
            rb_NConfidentalProfile.setFocusable(false);
            controller.setRb_NConfidentalProfile(rb_NConfidentalProfile);

            RadioButton rb_RConfidentalProfile = vRoot.findViewById(R.id.rb_RConfidentalProfile);
            rb_RConfidentalProfile.setKeyListener(null);
            rb_RConfidentalProfile.setFocusable(false);
            controller.setRb_RConfidentalProfile(rb_RConfidentalProfile);

            Button btnProfileUpdate = vRoot.findViewById(R.id.btnProfileUpdate);
            btnProfileUpdate.setOnClickListener(view -> controller.updateProfile());

            //isCreatedProfile = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            //isCreatedProfile = false;
        }
    }


}