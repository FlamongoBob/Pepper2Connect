package com.example.pepper2connect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pepper2connect.controller.Controller;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_ServerConnection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_ServerConnection extends Fragment {
    View viewServerConnection;

    public Fragment_ServerConnection() {
        // Required empty public constructor
    }

    public static Fragment_ServerConnection newInstance() {
        Fragment_ServerConnection fragment = new Fragment_ServerConnection();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewServerConnection = inflater.inflate(R.layout.fragment__server_connection, container, false);
       /*
        btnTestConnection = (Button) viewServerConnection.findViewById(R.id.btnTestConnection);
        btnTestConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testServerConnection();
            }
        });
        */

        // Inflate the layout for this fragment
        return viewServerConnection;
    }

}