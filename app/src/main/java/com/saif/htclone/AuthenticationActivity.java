package com.saif.htclone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Fragment.FragmentLogIn;
import Fragment.FragmentSignup;
import Utils.FragmentUtilities;

public class AuthenticationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private FragmentLogIn fragmentLogIn;
    private FragmentSignup fragmentSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        fragmentLogIn=new FragmentLogIn();
        fragmentSignup=new FragmentSignup();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(!(currentUser==null))
        {
            Toast.makeText(this, "Current User Not Null", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Current User  Null", Toast.LENGTH_LONG).show();
        }
        new FragmentUtilities(this).addFragment(R.id.container,fragmentSignup);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }
}