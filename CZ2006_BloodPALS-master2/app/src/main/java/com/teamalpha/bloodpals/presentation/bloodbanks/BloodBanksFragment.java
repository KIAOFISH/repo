package com.teamalpha.bloodpals.presentation.bloodbanks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.bloodbanks.BloodBank;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class BloodBanksFragment extends DaggerFragment implements OnMapReadyCallback {

    private static final String TAG = "BloodBanksFragment";

    private BloodBanksViewModel viewModel;
    private GoogleMap map;
    private List<Marker> mapMarkers = new ArrayList<>();

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bloodbanks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_View);
        mapView.getMapAsync(this);

        viewModel = ViewModelProviders.of(this, providerFactory).get(BloodBanksViewModel.class);

        subscribeObservers();

        viewModel.loadBloodBanks();
    }

    private void subscribeObservers() {
        viewModel.getBloodBankData().removeObservers(getViewLifecycleOwner());
        viewModel.getBloodBankData().observe(getViewLifecycleOwner(), new Observer<List<BloodBank>>() {

            @Override
            public void onChanged(List<BloodBank> bloodBanks) {
                Log.e(TAG, "onChanged: blood bank list changed.");
                updateMapMarkers(bloodBanks);
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        android.content.Context context = getContext();
        LatLng sg = getLocationFromAddress(context,"Singapore");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sg, 11));
        map.setInfoWindowAdapter(new BloodBankWindowAdapter(getContext()));
        updateMapMarkers(new ArrayList<BloodBank>());
    }

    private void updateMapMarkers(List<BloodBank> bloodBankList) {
        // clear markers
        for(int i = 0; i < mapMarkers.size(); i++) {
            mapMarkers.get(i).remove();
        }
        mapMarkers.clear();

        // reassign markers
        for(int i = 0 ; i < bloodBankList.size(); i++) {
            // check if whole blood donation type
            if(bloodBankList.get(i).getDonationType().trim().toLowerCase().equals("whole blood")) {
                // add new marker
                LatLng bBLatLng = getLocationFromAddress(getContext(), bloodBankList.get(i).getPostalCode());
                if(bBLatLng != null) {
                    Marker marker = map.addMarker(new MarkerOptions().position(bBLatLng).title(bloodBankList.get(i).getLocation())
                            .snippet("Donation Type: " + bloodBankList.get(i).getDonationType() + "\n"
                                    + "Monday Operating Hours: " + bloodBankList.get(i).getMondayOperatingHour() + "\n"
                                    + "Tuesday Operating Hours: " + bloodBankList.get(i).getTuesdayOperatingHour() + "\n"
                                    + "Wednesday Operating Hours: " + bloodBankList.get(i).getWednesdayOperatingHour() + "\n"
                                    + "Thursday Operating Hours: " + bloodBankList.get(i).getThursdayOperatingHour() + "\n"
                                    + "Friday Operating Hours: " + bloodBankList.get(i).getFridayOperatingHour() + "\n"
                                    + "Saturday Operating Hours: " + bloodBankList.get(i).getSaturdayOperatingHour() + "\n"
                                    + "Sunday Operating Hours: " + bloodBankList.get(i).getSundayOperatingHour() + "\n"
                                    + "Eve of PH Operating Hours: " + bloodBankList.get(i).getEveOfMajorPublicHolidayOperatingHour() + "\n"
                                    + "PH Operating Hours: " + bloodBankList.get(i).getPublicHolidayOperatingHour() + "\n"));
                    mapMarkers.add(marker);
                }

            }
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName("Singapore "+strAddress, 5);
            if (address == null || address.size() <= 0) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

}
