package Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.saif.htclone.R;

import java.util.PriorityQueue;

import Utils.FragmentUtilities;

public class FragmentLogIn extends Fragment implements View.OnClickListener {
    private View view;
    private EditText etPhone;
    private EditText etPassword;
    private TextView tvSignUp;
    private Button btnSignIn;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private static final String TAG = FragmentLogIn.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_in, container, false);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        etPhone=view.findViewById(R.id.etPhone);
        etPassword=view.findViewById(R.id.etPassword);
        tvSignUp=view.findViewById(R.id.tvSignUp);
        btnSignIn=view.findViewById(R.id.btnSignIn);
        tvSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        return view;
    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        String mEmail=email+"@ht.com";

        //    showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(mEmail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithEmail:success");
                            FragmentUpdateInfo fragmentUpdateInfo = new FragmentUpdateInfo();
                            new FragmentUtilities(getActivity())
                                    .replaceFragmentWithoutBackTrace(R.id.container, fragmentUpdateInfo);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            tvSignUp.setText("Authentication failed.");
                        }

                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = etPhone.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etPhone.setError("Required.");
            valid = false;
        } else {
            etPhone.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvSignUp:
                FragmentSignup fragmentSignup=new FragmentSignup();
                new FragmentUtilities(getActivity()).replaceFragment(R.id.container,fragmentSignup);
                break;
            case R.id.btnSignIn:
                if (!validateForm()) {
                    break;
                }
                signIn(etPhone.getText().toString(),etPassword.getText().toString());
                break;
        }

    }
}
