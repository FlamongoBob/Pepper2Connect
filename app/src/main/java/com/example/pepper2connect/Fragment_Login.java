package com.example.pepper2connect;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pepper2connect.controller.Controller;

public class Fragment_Login extends Fragment {

    Controller controller;
    Button btnLogin;

    public Fragment_Login(Controller controller) {
        // Required empty public constructor
        this.controller = controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vRoot = inflater.inflate(R.layout.fragment__login, container, false);

        initiateLoginControls(vRoot);
        return vRoot;
    }



    public void initiateLoginControls(View vRoot) {
        try {

            EditText etLoginUserName = vRoot.findViewById(R.id.etLoginUserName);
            controller.setEtLoginUsername(etLoginUserName);
            etLoginUserName.setText("ADMIN");

            EditText etLoginPassword = vRoot.findViewById(R.id.etLoginPassword);
            controller.setEtLoginPassword(etLoginPassword);
            etLoginPassword.setText("ADMIN");

            btnLogin = vRoot.findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(view -> {
                TextView tvLoginInformation = vRoot.findViewById(R.id.tvLoginInformation);
                controller.setTvLoginInformation(tvLoginInformation);
                controller.connect2Pepper(etLoginUserName.getText().toString(), etLoginPassword.getText().toString());
            });
            //isCreatedLogin = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            //isCreatedLogin = false;
        }
    }



}