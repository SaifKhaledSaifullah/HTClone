package Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saif.htclone.R;

import java.util.HashMap;

public class FragmentUpdateInfo extends Fragment {
    private View view;

    private EditText nameField;
    private EditText addressField;
    private Button btnUpdateInfo;
    private ProgressBar pgBar;

    private String userName = "";
    private String userAddres = "";
    private String lat = "";
    private String lang = "";
    private String profilePic = "";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private DatabaseReference databaseReference;

    private static final String TAG = FragmentUpdateInfo.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_name_address, container, false);
        // Get  link to the firebase db
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!(currentUser == null)) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        userName = dataSnapshot.child(currentUser.getEmail().replace("@ht.com", "")).child("Name").getValue(String.class);
                        userAddres = dataSnapshot.child(currentUser.getEmail().replace("@ht.com", "")).child("Address").getValue(String.class);
                        profilePic = dataSnapshot.child(currentUser.getEmail().replace("@ht.com", "")).child("profilePic").getValue(String.class);
                        lat = dataSnapshot.child(currentUser.getEmail().replace("@ht.com", "")).child("Lat").getValue(String.class);
                        lang = dataSnapshot.child(currentUser.getEmail().replace("@ht.com", "")).child("Lang").getValue(String.class);

                    } else {
                        Toast.makeText(getActivity(), "No data snapshot", Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });
        }


        nameField = view.findViewById(R.id.nameField);
        addressField = view.findViewById(R.id.addressField);
        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);
        pgBar = view.findViewById(R.id.pgBar);
        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });
        return view;
    }

    private void updateUserData() {
        String userName = nameField.getText().toString();
        String address = addressField.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            nameField.setError("Required");
        } else if (TextUtils.isEmpty(address)) {
            addressField.setError("Required");
        } else {
            addData(userName, address);
        }
    }

    public void addData(String name, String address) {

        pgBar.setVisibility(View.VISIBLE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!(currentUser == null)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Name", name);
            map.put("Address", address);
            map.put("Lat", lat);
            map.put("Lang", lang);
            map.put("profilePic", profilePic);
            // add data with checking if data has stored successfully
            databaseReference.child(currentUser.getEmail().replace("@ht.com", "")).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        pgBar.setVisibility(View.GONE);
                        // show task successful msg.
                        Toast.makeText(getActivity(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        pgBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Couldn't update data, Please check Internet Connection", Toast.LENGTH_LONG).show();
                        // show task not successful msg.
                    }
                }
            });
        } else {

        }


    }

}
