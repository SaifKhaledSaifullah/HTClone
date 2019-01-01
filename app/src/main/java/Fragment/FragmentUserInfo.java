package Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saif.htclone.R;
import com.theartofdev.edmodo.cropper.CropImage;

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

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String userID;
    private Uri mImageUri;
    private String uri;
    private static final String TAG = FragmentUserInfo.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        // Get  link to the firebase db
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        storageReference = FirebaseStorage.getInstance().getReference("User");

        assignViews(view);

        userID = getArguments().getString(AppConfig.PHONE_NUMBER_KEY);
        phoneText.setText("(" + userID + ")");

        // Get value from root and set on the textView
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child(userID).child("Name").getValue(String.class);
                    String address = dataSnapshot.child(userID).child("Address").getValue(String.class);
                    uri = dataSnapshot.child(userID).child("profilePic").getValue(String.class);
                    Log.e("SURI", "Image: " + uri);
                    if (!TextUtils.isEmpty(username)) {
                        if (!username.contains("null")) {
                            greetingsText.setText("Hello " + username);
                        }

                    }
                    if (!TextUtils.isEmpty(uri)) {
                        if (!uri.equals("null")) {
                            Glide.with(getContext())
                                    .load(uri)
                                    .apply(new RequestOptions().placeholder(R.drawable.user).error(R.drawable.user).circleCropTransform())
                                    .into(profileImg);
                        }

                    }
                    if (!TextUtils.isEmpty(address)) {
                        if (!address.contains("null")) {
                            addressText.setText("Address: " + address);
                        }

                    } else {
                        addressText.setText("Address: Not Set");
                    }

                } else {
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
                CropImage.activity()

                        .start(getContext(), this);
                break;
            case R.id.setupUserInfo:
                new FragmentUtilities(getActivity()).replaceFragment(R.id.container, new FragmentUpdateInfo());
                break;
            case R.id.changeAddressText:
                new FragmentUtilities(getActivity()).replaceFragment(R.id.container, new FragmentUpdateLocation());
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
        if (!(FirebaseAuth.getInstance().getCurrentUser() == null)) {
            FirebaseAuth.getInstance().signOut();
            new FragmentUtilities(getActivity()).replaceFragmentWithoutBackTrace(R.id.container, new FragmentLogIn());
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = storageReference.child(userID
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    databaseReference.child(userID).child("profilePic").setValue(uri.toString())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.e("pp", "Success");
                                                }
                                            });
                                    //Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                mImageUri = result.getUri();

                Glide.with(this)
                        .load(mImageUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profileImg);
                uploadFile();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("MainActivity", error.getMessage());
            }
        }
    }
}
