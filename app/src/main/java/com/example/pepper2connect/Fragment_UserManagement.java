package com.example.pepper2connect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.pepper2connect.controller.Controller;

public class Fragment_UserManagement extends Fragment {

    MainActivity mainActivity;
    Controller controller;
    int intPos = -1;

    public Fragment_UserManagement(Controller controller, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.controller = controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vRoot = inflater.inflate(R.layout.fragment__user_management, container, false);
        initiateUserManagementControls(vRoot);
        // Inflate the layout for this fragment
        return vRoot;
    }

    private void initiateUserManagementControls(View vRoot) {

        ImageButton ibUMPicture = vRoot.findViewById(R.id.ibUMPicture);
        controller.setUMNewPicture(ibUMPicture);
     //   ibNewPicture.setOnClickListener();

        ibUMPicture.setOnClickListener(view -> {
            if (mainActivity.checkAndRequestPermissions(mainActivity)) {
                mainActivity.chooseImage(mainActivity);
            }
        });

        EditText etUMFirstName = vRoot.findViewById(R.id.etUMFirstName);
        controller.setEtUMFirstName(etUMFirstName);

        EditText etUMLastName = vRoot.findViewById(R.id.etUMLastName);
        controller.setEtUMLastName(etUMLastName);

        EditText etUMPassword = vRoot.findViewById(R.id.etUMPassword);
        controller.setEtUMPassword(etUMPassword);

        EditText etUMUserName = vRoot.findViewById(R.id.etUMUserName);
        controller.setEtUMUserName(etUMUserName);

        EditText etUMTitle = vRoot.findViewById(R.id.etUMTitle);
        controller.setEtUMTitle(etUMTitle);

        Spinner spUMRole = vRoot.findViewById(R.id.spUMRole);
        controller.setSpUMRole(spUMRole);


        Button btnUMSaveChanges = vRoot.findViewById(R.id.btnUMSaveChanges);
        btnUMSaveChanges.setOnClickListener(view -> {
            controller.updateEmployee();
        });

        Button btnUMDeleteUser = vRoot.findViewById(R.id.btnUMDeleteUser);
        btnUMDeleteUser.setOnClickListener(view -> {
            controller.deleteEmployee();
        });

        Button btnUMPrevious = vRoot.findViewById(R.id.btnUMPrevious);
        btnUMPrevious.setOnClickListener(view -> {
            intPos = intPos-1;
            controller.starFillUserManagement(intPos);
        });

        Button btnUMNext = vRoot.findViewById(R.id.btnUMNext);
        btnUMNext.setOnClickListener(view -> {
            intPos = intPos+1;
            controller.starFillUserManagement(intPos);
        });

    }
}