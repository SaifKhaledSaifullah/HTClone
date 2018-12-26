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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.saif.htclone.R;

import java.util.concurrent.TimeUnit;

import Utils.FragmentUtilities;

public class FragmentSignup extends Fragment  implements View.OnClickListener {
    private View view;

    private LinearLayout phoneNumberGroup;
    private LinearLayout varificationGroup;
    private TextView phoneHeadingText;
    private TextView phoneSubHeadingText;
    private EditText phoneNumberField;
    private EditText verifyCodeField;
    private Button btnSignUp;
    private Button btnVerify;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private static final String TAG = FragmentSignup.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Assign all views
        assignViews(view);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.e(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
               // updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Log.e(TAG, "Invalid phone number");
                    //mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Toast.makeText(getActivity(), "Quota exceeded.", Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
                else {
                    Toast.makeText(getActivity(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
               // updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;

                //mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                phoneNumberGroup.setVisibility(View.GONE);
                varificationGroup.setVisibility(View.VISIBLE);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
        return view;
    }

    private void assignViews(View view) {
        phoneNumberGroup = view.findViewById(R.id.phoneNumberGroup);
        varificationGroup = view.findViewById(R.id.varificationGroup);

        phoneHeadingText = view.findViewById(R.id.phoneHeadingText);
        phoneSubHeadingText = view.findViewById(R.id.phoneSubHeadingText);

        phoneNumberField = view.findViewById(R.id.phoneNumberField);
        verifyCodeField = view.findViewById(R.id.verifyCodeField);

        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnVerify = view.findViewById(R.id.btnVerify);

        // Assign Listeners
        assignClickListerns();
    }

    private void assignClickListerns() {
        btnSignUp.setOnClickListener(this);
        btnVerify.setOnClickListener(this);

    }
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success: ");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            phoneHeadingText.setText("Successfully Signed In");

                           // creatEmailPassAccount(user.getPhoneNumber().replace("+88",""),"Hello2019");

                            FragmentPassWordSet fragmentPassWordSet=new FragmentPassWordSet();
                            Bundle args = new Bundle();
                            args.putString("pNumber",user.getPhoneNumber().replace("+88","") );
                            fragmentPassWordSet.setArguments(args);

                            new FragmentUtilities(getActivity()).replaceFragmentWithoutBackTrace(R.id.container,fragmentPassWordSet);
                            // Log.e(TAG, "signInWithCredential:success");

                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                verifyCodeField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            phoneHeadingText.setText("Couldn't Signed In");

                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    private void creatEmailPassAccount(String email, String pass) {
        // [START create_user_with_email]
        String mEmail=email+"@saif.com";
        mAuth.createUserWithEmailAndPassword(mEmail, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        //updateUI(STATE_INITIALIZED);
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)|| phoneNumber.length()!=11||!phoneNumber.startsWith("01") ){
            phoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(!(currentUser==null))
        {
            phoneHeadingText.setText("Signed in");
            phoneSubHeadingText.setText("User Id: "+currentUser.getUid());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                if (!validatePhoneNumber()) {
                    return;
                }

                startPhoneNumberVerification(phoneNumberField.getText().toString());
                break;
            case R.id.btnVerify:

                String code = verifyCodeField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    verifyCodeField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
        }

    }
    // [END on_start_check_user]
}
