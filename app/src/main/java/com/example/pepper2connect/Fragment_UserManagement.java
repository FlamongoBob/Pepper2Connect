package com.example.pepper2connect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_UserManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_UserManagement extends Fragment {


    public Fragment_UserManagement() {
        // Required empty public constructor
    }

    public static Fragment_UserManagement newInstance() {
        Fragment_UserManagement fragment = new Fragment_UserManagement();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__user_management, container, false);
    }
}