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
import com.saif.htclone.R;

import Utils.FragmentUtilities;

public class FragmentPassWordSet extends Fragment {
    private View view;
    private TextView passwordHeadingText;
    private Button btnSignIn;
    private EditText passwordField;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private static final String TAG = FragmentPassWordSet.class.getSimpleName();
    private String phoneNumber = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password_entry, container, false);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        phoneNumber = getArguments().getString("pNumber");

        passwordHeadingText = view.findViewById(R.id.passwordHeadingText);
        passwordField = view.findViewById(R.id.passwordField);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordField.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    passwordField.setError("Enter Password");
                } else {

                    if (!TextUtils.isEmpty(phoneNumber)) {
                        String email = phoneNumber + "@ht.com";
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.e(TAG, "createUserWithEmail:success");
                                            FragmentUpdateInfo fragmentUpdateInfo = new FragmentUpdateInfo();
                                            new FragmentUtilities(getActivity())
                                                    .replaceFragmentWithoutBackTrace(R.id.container, fragmentUpdateInfo);
                                            //passwordHeadingText.setText("Successful");

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            passwordHeadingText.setText("Failure");
                                            Toast.makeText(getActivity(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            // updateUI(null);
                                        }

                                        // [START_EXCLUDE]
                                        // hideProgressDialog();
                                        // [END_EXCLUDE]
                                    }
                                });
                    }

                }
            }
        });

        return view;
    }


}
