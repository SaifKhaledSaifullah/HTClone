package Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saif.htclone.R;

import Utils.AppConfig;
import Utils.FragmentUtilities;

public class FragmentUserInfo extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView profileImg;
    private TextView greetingsText;
    private TextView phoneText;
    private TextView addressText;
    private TextView setupUserInfo;
    private TextView changeAddressText;
    private TextView changePasswordText;
    private TextView loggOutText;

    private DatabaseReference databaseReference;
    private static final String TAG = FragmentUserInfo.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        // Get  link to the firebase db
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        assignViews(view);

        final String userID=getArguments().getString(AppConfig.PHONE_NUMBER_KEY);
        phoneText.setText("("+userID+")");

        // Get value from root and set on the textView
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String username=dataSnapshot.child(userID).child("Name").getValue(String.class);
                    String address=dataSnapshot.child(userID).child("Address").getValue(String.class);
                    if(!TextUtils.isEmpty(username))
                    {
                        if(!username.contains("null"))
                        {
                            greetingsText.setText("Hello " +username);
                        }

                    }
                    if(!TextUtils.isEmpty(address))
                    {
                        if(!address.contains("null"))
                        {
                            addressText.setText("Address: "+address);
                        }

                    }
                    else {
                        addressText.setText("Address: Not Set");
                    }

                }
                else {
                    Toast.makeText(getActivity(), "No data snapshot", Toast.LENGTH_LONG).show();
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
        return view;
    }

    private void assignViews(View view) {
        profileImg = view.findViewById(R.id.profileImg);
        greetingsText = view.findViewById(R.id.greetingsText);
        phoneText = view.findViewById(R.id.phoneText);
        addressText = view.findViewById(R.id.addressText);
        setupUserInfo = view.findViewById(R.id.setupUserInfo);
        changeAddressText = view.findViewById(R.id.changeAddressText);
        changePasswordText = view.findViewById(R.id.changePasswordText);
        loggOutText = view.findViewById(R.id.loggOutText);

        assignClickListners();
    }

    private void assignClickListners() {
        profileImg.setOnClickListener(this);
        setupUserInfo.setOnClickListener(this);
        changeAddressText.setOnClickListener(this);
        changePasswordText.setOnClickListener(this);
        loggOutText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.profileImg:
                Toast.makeText(getActivity(), "Working on it", Toast.LENGTH_LONG).show();
                break;
            case R.id.setupUserInfo:
                new FragmentUtilities(getActivity()).replaceFragment(R.id.container,new FragmentUpdateInfo());
                break;
            case R.id.changeAddressText:
                new FragmentUtilities(getActivity()).replaceFragment(R.id.container,new FragmentUpdateLocation());
                break;
            case R.id.changePasswordText:
                Toast.makeText(getActivity(), "Working on it", Toast.LENGTH_LONG).show();
                break;
            case R.id.loggOutText:
                logOut();
                break;
        }
    }

    private void logOut() {
        if(!(FirebaseAuth.getInstance().getCurrentUser()==null))
        {
            FirebaseAuth.getInstance().signOut();
            new FragmentUtilities(getActivity()).replaceFragmentWithoutBackTrace(R.id.container,new FragmentLogIn());
        }

    }
}
