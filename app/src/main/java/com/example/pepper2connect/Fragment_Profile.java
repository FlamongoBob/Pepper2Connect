package com.example.pepper2connect;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pepper2connect.controller.Controller;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Fragment_Profile extends Fragment {
    MainActivity  mainActivity;
    Controller controller;

    Resources resources = Resources.getSystem();
    FragmentManager frgMng;
    Fragment activeFragment;
    Fragment_NewUser fragment_newUser = new Fragment_NewUser();
    private boolean isCreatedNewUser = false;

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
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(mainActivity, resources.getText(R.string.last_message_rec_Text), Toast.LENGTH_SHORT).show();
        if(activeFragment == null) {
            activeFragment = mainActivity.getActiveFragment();
        }
        if(frgMng == null) {
            frgMng = mainActivity.getFrgMng();
            frgMng.beginTransaction().add(R.id.container, fragment_newUser, "frgNewUser").hide(fragment_newUser).commit();
        }


        switch (item.getItemId()) {
            case R.id.New_User:
                if (!activeFragment.getTag().equals(frgMng.findFragmentByTag("frgNewUser").getTag())) {
                    frgMng.beginTransaction().hide(activeFragment).show(fragment_newUser).commit();
                    activeFragment = fragment_newUser;
                    if (!isCreatedNewUser) {
                        initiateNewUserControls();
                    }
                    return true;
                }
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateNewUserControls(){
        EditText etNuFirstName = mainActivity.findViewById(R.id.etNuFirstName);
        controller.setEtNuFirstName(etNuFirstName);

        EditText etNuLastName = mainActivity.findViewById(R.id.etNuLastName);
        controller.setEtNuLastName(etNuLastName);

        EditText etNuPassword = mainActivity.findViewById(R.id.etNuPassword);
        controller.setEtNuPassword(etNuPassword);

        EditText etNuUserName = mainActivity.findViewById(R.id.etNuUserName);
        controller.setEtNuUserName(etNuUserName);

        Spinner spNuTitle  = mainActivity.findViewById(R.id.spNuTitle);
        controller.setSpTitle(spNuTitle);

        Spinner spNuRole  = mainActivity.findViewById(R.id.spNuRole);
        controller.setSpRole(spNuRole);


    }

}