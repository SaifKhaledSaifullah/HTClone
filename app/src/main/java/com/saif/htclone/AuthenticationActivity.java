package com.saif.htclone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import Fragment.FragmentLogIn;
import Utils.FragmentUtilities;

public class AuthenticationActivity extends AppCompatActivity {

    private FragmentLogIn fragmentLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentLogIn=new FragmentLogIn();
        new FragmentUtilities(this).addFragment(R.id.container,fragmentLogIn);

    }
}