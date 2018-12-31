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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
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

import Utils.AppConfig;

public class FragmentUpdateLocation extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mMapView;
    private View view;
    private DatabaseReference databaseReference;
    private String userID;
    private String userName;
    private String userAddress;
    private String lat;
    private String lang;
    private Button updateLocBtn;
    private static final String TAG = FragmentUpdateLocation.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_update, container, false);
        // Get  link to the firebase db
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        getUserInfo();
        updateLocBtn=view.findViewById(R.id.updateLocBtn);
        //userID = getArguments().getString(AppConfig.PHONE_NUMBER_KEY);
        mMapView = view.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        updateLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation(lat,lang);
            }
        });
        return view;
    }

    private void getUserInfo() {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!(currentUser == null)) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        userName=dataSnapshot.child(currentUser.getEmail().replace("@ht.com","")).child("Name").getValue(String.class);
                        userAddress=dataSnapshot.child(currentUser.getEmail().replace("@ht.com","")).child("Address").getValue(String.class);
                        lat=dataSnapshot.child(currentUser.getEmail().replace("@ht.com","")).child("Lat").getValue(String.class);
                        lang=dataSnapshot.child(currentUser.getEmail().replace("@ht.com","")).child("Lang").getValue(String.class);

                    }
                    else {
                        // Toast.makeText(getActivity(), "No data snapshot", Toast.LENGTH_LONG).show();
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });
        }
        else{

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng userLocation;
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        if(TextUtils.isEmpty(lat)&&TextUtils.isEmpty(lang))
        {
            userLocation = new LatLng(23.7467623, 90.3744364);
        }
        else{
            userLocation=new LatLng(Double.parseDouble(lat),Double.parseDouble(lang));
        }

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                //get latlng at the center by calling
                lat = String.valueOf(mMap.getCameraPosition().target.latitude);
                lang = String.valueOf(mMap.getCameraPosition().target.longitude);
               // Log.e("MDWMM", "LatLang: " + latLong);
            }
        });
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        // Get value from root and set on the textView
        updateLocBtn.setEnabled(true);
    }

    public void updateLocation(String lat,String lang) {

        // pgBar.setVisibility(View.VISIBLE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!(currentUser == null)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Name", userName);
            map.put("Address", userAddress);
            map.put("Lat", lat);
            map.put("Lang", lang);
            // add data with checking if data has stored successfully
            databaseReference.child(currentUser.getEmail().replace("@ht.com", "")).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //  pgBar.setVisibility(View.GONE);
                        // show task successful msg.
                        Toast.makeText(getActivity(), "Location Updated Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        //pgBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Couldn't update location, Please check Internet Connection", Toast.LENGTH_LONG).show();
                        // show task not successful msg.
                    }
                }
            });
        } else {

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
