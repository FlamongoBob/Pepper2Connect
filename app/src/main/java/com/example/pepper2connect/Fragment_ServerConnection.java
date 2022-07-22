package com.example.pepper2connect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.pepper2connect.controller.Controller;

public class Fragment_ServerConnection extends Fragment {
    private Button btnTestConnection;
    private Controller controller;


    public Fragment_ServerConnection(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vRoot = inflater.inflate(R.layout.fragment__server_connection, container, false);
        initiateServerControls(vRoot);
        return vRoot;
    }

    public void initiateServerControls(View vRoot) {
        try {


            btnTestConnection = vRoot.findViewById(R.id.btnTestConnection);
            btnTestConnection.setOnClickListener(view -> controller.testServerConnection());

            EditText etLogServerCon = vRoot.findViewById(R.id.etLogServerCon);
            //etLogServerCon.setFocusable(false);
            etLogServerCon.setKeyListener(null);
            etLogServerCon.setHorizontallyScrolling(true);

            controller.setEtLogServerCon(etLogServerCon);
            //isCreatedServer = true;
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            //isCreatedServer = false;
        }
    }

}