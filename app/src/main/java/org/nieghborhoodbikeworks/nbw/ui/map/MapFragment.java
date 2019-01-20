package org.nieghborhoodbikeworks.nbw.ui.map;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.nieghborhoodbikeworks.nbw.DrawerLocker;
import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static String TAG = "Map Fragment";
    private View mView;
    private GoogleMap mMap;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    /**
     * This initializes the UI variables once the fragment starts up, and returns the view
     * to its parent.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Location & Hours");
        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        // Get the view from fragment XML
        mView = inflater.inflate(R.layout.map_fragment, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In our case, we
     * just add a marker near NBW's campus.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install it
     * inside the SupportMapFragment. This method will only be triggered once the user installs Google
     * Play services and returns to the app.
     *
     * @param googleMap The map to be displayed
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Initialize map UI features
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        LatLng NBWcampus = new LatLng(39.962720,-75.201020);

        // Drop a pin on NBW's campus location
        mMap.addMarker(new MarkerOptions().position(NBWcampus)
                .title("Neighborhood Bike Works")
                .snippet("3939 Lancaster Avenue, Philadelphia, PA"))
                .showInfoWindow();

        // Create zoom effect when map is first loaded onto the UI
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NBWcampus, 16));
    }
}