package com.example.pepper2connect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.pepper2connect.controller.Controller;

public class Fragment_PatientInformation extends Fragment {

    private Controller controller;

    public Fragment_PatientInformation(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vRoot = inflater.inflate(R.layout.fragment__patient_information, container, false);
        initiatePatientControls(vRoot);

        return vRoot;
    }



    public void initiatePatientControls(View vRoot) {
        try {
            EditText etPatientInformation = vRoot.findViewById(R.id.etPatientInformation);
            etPatientInformation.setFocusable(false);
            etPatientInformation.setKeyListener(null);
            controller.setEtPatientInformation(etPatientInformation);
            //isCreatedPatient = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            //isCreatedPatient = false;
        }
    }

}