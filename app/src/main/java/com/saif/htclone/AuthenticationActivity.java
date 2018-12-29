package com.saif.htclone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Fragment.FragmentLogIn;
import Fragment.FragmentUserInfo;
import Utils.AppConfig;
import Utils.FragmentUtilities;

public class AuthenticationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (!(currentUser == null)) {
            FragmentUserInfo fragmentUserInfo = new FragmentUserInfo();
            Bundle args = new Bundle();
            args.putString(AppConfig.PHONE_NUMBER_KEY, currentUser.getEmail().replace("@ht.com", ""));
            fragmentUserInfo.setArguments(args);

            new FragmentUtilities(this)
                    .replaceFragmentWithoutBackTrace(R.id.container, fragmentUserInfo);

        } else {
            new FragmentUtilities(this).replaceFragmentWithoutBackTrace
                    (R.id.container, new FragmentLogIn());
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }

}