package com.sosokan.android.ui.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sosokan.android.R;
import com.sosokan.android.adapter.CitiesAdapter;
import com.sosokan.android.adapter.StatesAdapter;
import com.sosokan.android.models.City;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.ui.fragment.HomeFragment;
import com.sosokan.android.utils.GPSTracker;
import com.sosokan.android.utils.LocaleHelper;
import com.sosokan.android.utils.PermissionGrantedHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by macintosh on 2/26/17.
 */

public class DialogCities extends DialogFragment implements OnMapReadyCallback {

// CITIES

    RecyclerView rvCities;
    CitiesAdapter citiesAdapter;


    RecyclerView rvStates;
    StatesAdapter statesAdapter;
    List<String> states;
    List<City> cities;
    Map<String, List<City>> mapCities;

    TextView tvCityDialogCities;
    TextView tvStateDialogCities;
    com.google.android.gms.maps.MapFragment mapFragment;
    private GoogleMap googleMap;
    PermissionGrantedHelper permissionGrantedHelper;
    String address;
    Location location;
    Activity activity;

    private View view;
    City citySelected;

    FragmentManager fragmentManager;
    Dialog dialog;
    RelativeLayout rlLocation;

    RelativeLayout rlState;
    RelativeLayout rlCities;

    PrefManager prefManager;

    public static DialogCities newInstance(String title) {
        DialogCities frag = new DialogCities();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),
                R.style.Theme_CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_cities, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);

        dialog.setContentView(view);
        this.dialog = dialog;
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        initializeViews();

    }

    private void initializeViews() {

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.

        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((MapFragment) getActivity().getFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            fragmentManager = getFragmentManager();
            activity = getActivity();
            if (isGoogleMapsInstalled()) {
                if (googleMap != null) {
                    setupMap();
                    Log.e("setUpMapIfNeeded", "onMapReady");
                }
            }
            Log.e("setUpMapIfNeeded", "showDialog");
            // showDialog(getActivity(),getFragmentManager());
            permissionGrantedHelper = new PermissionGrantedHelper(activity);

            if (prefManager == null) prefManager = new PrefManager(activity);
            if (prefManager.getListCities() == null || prefManager.getListCities().size() <= 0) {
                initValueCities(activity);
            } else {
                cities = prefManager.getListCities();
                filterStateByCities();
            }


            initView(activity, dialog, fragmentManager);

            getAddressFromLocation();
            dialog.show();
        }
    }

    public boolean isGoogleMapsInstalled() {
        try {
            getActivity().getPackageManager().getApplicationInfo(
                    "com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
        if (getActivity() != null) {
            try {
                Fragment fragment = (getActivity().getFragmentManager()
                        .findFragmentById(R.id.map));
                FragmentTransaction ft = getActivity().getFragmentManager()
                        .beginTransaction();
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            } catch (Exception e) {

            }

            if(citySelected == null)
            {
                sendBroadcastUnselectCity();
            }
        }

    }

    private void sendBroadcastUnselectCity() {
        Log.e("onDestroyView", "unselected ");
        Intent intent = new Intent(MenuActivity.ACTION_UNSELECT_CITY);
        activity.sendBroadcast(intent);
    }


    private void initView(final Activity activity, final Dialog dialog, FragmentManager fragmentManager) {
        setupRecycleViewCities(dialog);
        setupRecycleViewState(dialog, activity);

        TextView btnCancel = (TextView) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcastUnselectCity();
                dialog.dismiss();
            }
        });

        TextView tvApply = (TextView) dialog.findViewById(R.id.tvApply);
        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.ACTION_SELECT_CITY);
                // intent.putExtra("distanceMax", distanceMax);
                intent.putExtra("citySelected", (Serializable) citySelected);
                activity.sendBroadcast(intent);
                dialog.dismiss();
            }
        });

        rlLocation = (RelativeLayout) dialog.findViewById(R.id.rlLocationDialogCities);
        rlState = (RelativeLayout) dialog.findViewById(R.id.rlStateDialogCities);
        rlCities = (RelativeLayout) dialog.findViewById(R.id.rlCitiesDialogCities);

        ImageView ibBackSelectCategory = (ImageView) dialog.findViewById(R.id.ibBackDialogCities);
        ibBackSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcastUnselectCity();
                dialog.dismiss();

            }
        });
        ImageView ibBackState = (ImageView) dialog.findViewById(R.id.ibBackStateDialogCities);
        ibBackState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLocation.setVisibility(View.VISIBLE);
                rlCities.setVisibility(View.GONE);
                rlState.setVisibility(View.GONE);
            }
        });

        ImageView ibBackCities = (ImageView) dialog.findViewById(R.id.ibBackCitiesDialogCities);
        ibBackCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rlLocation.setVisibility(View.VISIBLE);
                rlCities.setVisibility(View.GONE);
                rlState.setVisibility(View.GONE);
            }
        });


        tvStateDialogCities = (TextView) dialog.findViewById(R.id.tvStateDialogCities);
        tvStateDialogCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLocation.setVisibility(View.GONE);
                rlCities.setVisibility(View.GONE);
                rlState.setVisibility(View.VISIBLE);
            }
        });

        tvCityDialogCities = (TextView) dialog.findViewById(R.id.tvCityDialogCities);
        tvCityDialogCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLocation.setVisibility(View.GONE);
                rlCities.setVisibility(View.VISIBLE);
                rlState.setVisibility(View.GONE);
            }
        });
        mapFragment = (com.google.android.gms.maps.MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getResources().openRawResource(R.raw.cities);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void initValueCities(Activity activity) {
        try {

            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(activity));
            Gson gson = new Gson();
            cities = new ArrayList<>();
            states = new ArrayList<>();
            mapCities = new HashMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                City city = gson.fromJson(jsonArray.get(i).toString(), City.class);
                if (city != null) {
                    cities.add(city);
                    if (mapCities.get(city.getState()) != null) {
                        List<City> citiesOfState = mapCities.get(city.getState());
                        if (citiesOfState == null) {
                            citiesOfState = new ArrayList<>();
                        }
                        citiesOfState.add(city);
                        mapCities.put(city.getState(), citiesOfState);
                    } else {
                        List<City> citiesOfState = new ArrayList<>();
                        citiesOfState.add(city);
                        states.add(city.getState());
                        mapCities.put(city.getState(), citiesOfState);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupRecycleViewCities(Dialog dialog) {
        rvCities = (RecyclerView) dialog.findViewById(R.id.rvCities);
        rvCities.setHasFixedSize(true);
        citiesAdapter = new CitiesAdapter(dialog.getContext(), cities);
        StaggeredGridLayoutManager mLayoutManagerAdvertiseList = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvCities.setHasFixedSize(true);

        rvCities.setItemAnimator(new DefaultItemAnimator());

        rvCities.setAdapter(citiesAdapter);

        rvCities.setLayoutManager(mLayoutManagerAdvertiseList);

        //   recyclerView.setNestedScrollingEnabled(false);
        citiesAdapter.setListener(new CitiesAdapter.Listener() {
            @Override
            public void onItemClick(int position) {
                tvCityDialogCities.setText(cities.get(position).getCity());
                tvStateDialogCities.setText(cities.get(position).getState());
                LatLng latLng = new LatLng(cities.get(position).getLatitude(), cities.get(position).getLongitude());
                displayLocation(latLng, cities.get(position).getCity());
                citySelected = cities.get(position);
                Log.e("citiesAdapter", "position " + position);
                Log.e("citiesAdapter", "cities " + cities);
                rlLocation.setVisibility(View.VISIBLE);
                rlCities.setVisibility(View.GONE);
                rlState.setVisibility(View.GONE);
            }
        });

    }


    private void setupRecycleViewState(final Dialog dialog, final Activity activity) {
        rvStates = (RecyclerView) dialog.findViewById(R.id.rvStates);
        rvStates.setHasFixedSize(true);
        statesAdapter = new StatesAdapter(dialog.getContext(), states);
        StaggeredGridLayoutManager mLayoutManagerAdvertiseList = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvStates.setHasFixedSize(true);

        rvStates.setItemAnimator(new DefaultItemAnimator());

        rvStates.setAdapter(statesAdapter);

        rvStates.setLayoutManager(mLayoutManagerAdvertiseList);

        //   recyclerView.setNestedScrollingEnabled(false);
        statesAdapter.setListener(new StatesAdapter.Listener() {
            @Override
            public void onItemClick(int position) {
                tvStateDialogCities.setText(states.get(position));
                tvCityDialogCities.setText(activity.getResources().getString(R.string.all_cities_lowercase));
                List<City> citiesOfState = mapCities.get(states.get(position));
                if (citiesOfState == null)
                    citiesOfState = new ArrayList<City>();
                cities = citiesOfState;
                citiesAdapter = new CitiesAdapter(dialog.getContext(), cities);
                rvCities.setHasFixedSize(true);

                rvCities.setItemAnimator(new DefaultItemAnimator());

                rvCities.setAdapter(citiesAdapter);
                citiesAdapter.setListener(new CitiesAdapter.Listener() {
                    @Override
                    public void onItemClick(int position) {
                        tvCityDialogCities.setText(cities.get(position).getCity());
                        tvStateDialogCities.setText(cities.get(position).getState());
                        LatLng latLng = new LatLng(cities.get(position).getLatitude(), cities.get(position).getLongitude());
                        displayLocation(latLng, cities.get(position).getCity());
                        citySelected = cities.get(position);
                        Log.e("citiesAdapter", "position " + position);
                        Log.e("citiesAdapter", "cities " + cities);
                        rlLocation.setVisibility(View.VISIBLE);
                        rlCities.setVisibility(View.GONE);
                        rlState.setVisibility(View.GONE);
                    }
                });
                if(citiesOfState!= null && citiesOfState.size()>0)
                {
                    citySelected = citiesOfState.get(0);
                }else{
                    citySelected = null;
                }


                rlLocation.setVisibility(View.VISIBLE);
                rlCities.setVisibility(View.GONE);
                rlState.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        //   setUpMapReady(map);
        Log.e("onMapReady", "onMapReady");

    }


    public void setupMap() {

        Log.e("setupMap", "setupMap");
        mapFragment = (com.google.android.gms.maps.MapFragment) fragmentManager.findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // setUpMapReady(mMap);
                Log.e("setupMap", "onMapReady");
            }
        });
    }

    public void setUpMapReady() {
        LatLng latLng = null;
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            if ((permissionCheck == PackageManager.PERMISSION_GRANTED)) {

                latLng = LocaleHelper.getLocationFromAddress(address, activity);

            } else {

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 42);

            }

        } catch (Exception ex) {
            latLng = new LatLng(0, 0);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionGrantedHelper permissionGrantedHelper = new PermissionGrantedHelper(activity);
            permissionGrantedHelper.checkAndRequestPermissionForMap();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

        }
        if (latLng != null) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(address)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map)));

        }
    }

    public void displayLocation(LatLng latLng, String city) {

        if (latLng != null) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(city)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map)));

        }
    }


    public void getAddressFromLocation() {
        permissionGrantedHelper.checkAndRequestPermissionForMap();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        GPSTracker gpsTracker = new GPSTracker(activity);

        address = gpsTracker.getCompleteAddressString();
        location = gpsTracker.getLocation();
        if (googleMap != null)
            setUpMapReady();

        if (address == null || address.isEmpty()) {
            LatLng latLng = new LatLng(cities.get(0).getLatitude(), cities.get(0).getLongitude());
            displayLocation(latLng, cities.get(0).getCity());
        }
    }

    public void filterStateByCities() {

        states = new ArrayList<>();
        mapCities = new HashMap<>();
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            if (city != null) {
                if (mapCities.get(city.getState()) != null) {
                    List<City> citiesOfState = mapCities.get(city.getState());
                    if (citiesOfState == null) {
                        citiesOfState = new ArrayList<>();
                    }
                    citiesOfState.add(city);
                    mapCities.put(city.getState(), citiesOfState);
                } else {
                    List<City> citiesOfState = new ArrayList<>();
                    citiesOfState.add(city);
                    states.add(city.getState());
                    mapCities.put(city.getState(), citiesOfState);
                }
            }
        }
    }
}
