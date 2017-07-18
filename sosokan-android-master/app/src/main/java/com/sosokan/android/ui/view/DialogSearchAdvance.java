package com.sosokan.android.ui.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sosokan.android.R;
import com.sosokan.android.adapter.CitiesAdapter;
import com.sosokan.android.adapter.StatesAdapter;
import com.sosokan.android.control.multi.level.menu.DataProviderCategoryApi;
import com.sosokan.android.control.multi.level.menu.ItemInfo;

import com.sosokan.android.control.multi.level.menu.ListCategoryApiAdapter;
import com.sosokan.android.control.multi.level.menu.MultiLevelListView;
import com.sosokan.android.control.multi.level.menu.OnItemCategoryClickListener;

import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.City;

import com.sosokan.android.models.UserSelection;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.ui.fragment.HomeFragment;
import com.sosokan.android.utils.GPSTracker;
import com.sosokan.android.utils.PermissionGrantedHelper;
import com.sosokan.android.utils.comparator.CategoryApiSortComparator;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by macintosh on 2/26/17.
 */

public class DialogSearchAdvance extends DialogFragment implements OnMapReadyCallback {
    private static final String TAG = "DialogSearchAdvance";

    int distanceMax = 500;
    int distanceMin = 0;
    int priceMax = 5000;
    int priceMin = 0;
    int dayMax = 365;
    int dayMin = 0;

    PrefManager prefManager;
    List<City> cities;


    RecyclerView rvCities;
    CitiesAdapter citiesAdapter;
    City citySelected;
    TextView tvCitySearchAdvance, tvCategorySearchAdvance;
    LinearLayout llCitiesDialogSearch, llAdvanceSearchMain, llCategoryDialogSearch;
    CategoryNew category;
    boolean isChineseApp;
    LinearLayout llAllPrice, llAllDistance, llAllDay;
    boolean isAllowDistance, isAllPrice, isAllDay;

    //ImageView rbDistanceSearchAdvance;
    ToggleButton tbAllowDistance;
    ImageView rbPriceSearchAdvance, rbDaySearchAdvance;
    int ic_checked;
    int ic_check;
    EditText edtSearchDialogAdvanceSearch;
    ImageButton ibBackSelectCategory;
    UserSelection userSelection = null;

    private DatabaseReference databaseReference, mDatabaseUser;
    private MultiLevelListView mListView;
    private List<CategoryNew> categories, categoriesChild;
    public String categoryAllId = "All";
    public CategoryNew categoryAll = null;
    public static Map<String, CategoryNew> mapCategory;
    public static Map<String, List<String>> mapCategoryChildren;
    ListCategoryApiAdapter listAdapter;
    public String categorySelectedId;
    CategoryNew categorySelected;
    //boolean isAllowDistance;


    //====== CITY NEW
    ImageView ibBackDialogSearchFromCity, ibBackCitiesDialogSearch, ibBackStateDialogSearch;
    TextView tvStateDialogSearch;
    TextView tvCityDialogSearch;
    TextView tvApplyCityState;
    TextView btnCancelCityState;

    RelativeLayout rlCitiesDialogSearch, rlStateDialogSearch, rlLocationDialogSearch;
    com.google.android.gms.maps.MapFragment mapFragment;
    private GoogleMap googleMap;
    PermissionGrantedHelper permissionGrantedHelper;

    RecyclerView rvStates;
    StatesAdapter statesAdapter;
    List<String> states;
    Map<String, List<City>> mapCities;
    FragmentManager fragmentManager;
    String address;
    Location location;
    Activity activity;
    Dialog dialog;
    private View view;
    static Boolean isFromSplash;

    public static DialogSearchAdvance newInstance(String title, Boolean fromSplash) {
        DialogSearchAdvance frag = new DialogSearchAdvance();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        isFromSplash = fromSplash;
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),
                R.style.Theme_CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_search_advanced, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);

        dialog.setContentView(view);
        this.dialog = dialog;
        return dialog;
    }


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        setUpMapIfNeeded();

    }

    private void initView(final Activity activity, final Dialog dialog, FragmentManager fragmentManager) {
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tvApply = (TextView) dialog.findViewById(R.id.tvApply);
        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.ACTION_SEARCH_ADVANCE);
                intent.putExtra("distanceMax", distanceMax);
                intent.putExtra("distanceMin", distanceMin);
                intent.putExtra("priceMax", priceMax);
                intent.putExtra("priceMin", priceMin);
                intent.putExtra("dayMax", dayMax);
                intent.putExtra("dayMin", dayMin);

                intent.putExtra("citySelected", citySelected);
                intent.putExtra("isAllDay", isAllDay);
                intent.putExtra("isAllowDistance", isAllowDistance);
                intent.putExtra("isAllPrice", isAllPrice);
                intent.putExtra("textSearch", edtSearchDialogAdvanceSearch.getText().toString());
                intent.putExtra("categorySelectedId", categorySelectedId);
                userSelection = new UserSelection();

                activity.sendBroadcast(intent);
                dialog.dismiss();
            }
        });


        ImageView ibBackSearchAdvance = (ImageView) dialog.findViewById(R.id.ibBackSearchAdvance);
        ibBackSearchAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RangeSeekBar rangeSeekBarPrice = (RangeSeekBar) dialog.findViewById(R.id.rangeSeekBarPrice);
        rangeSeekBarPrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                priceMax = (int) maxValue;
                priceMin = (int) minValue;
            }
        });


        RangeSeekBar rangeSeekBarDay = (RangeSeekBar) dialog.findViewById(R.id.rangeSeekBarDay);
        rangeSeekBarDay.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                dayMax = (int) maxValue;
                dayMin = (int) minValue;
            }
        });

        final RangeSeekBar rangeSeekBarDistance = (RangeSeekBar) dialog.findViewById(R.id.rangeSeekBarDistance);
        rangeSeekBarDistance.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                distanceMax = (int) maxValue;
                distanceMin = (int) minValue;
            }
        });


        tvCitySearchAdvance = (TextView) dialog.findViewById(R.id.tvCitySearchAdvance);
        tvCitySearchAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO CICK CITY
                processShowScreenSelectCity();
            }
        });

        ibBackSelectCategory = (ImageButton) dialog.findViewById(R.id.ibBackSelectCategory);
        ibBackSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processWhenSelectedCategory();
            }
        });


        tvCategorySearchAdvance = (TextView) dialog.findViewById(R.id.tvCategorySearchAdvance);
        tvCategorySearchAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScreenForDisplayCategory();
            }
        });

        if (category != null) {
            if (isChineseApp) {
                if (category.getNameChinese() != null && !category.getNameChinese().isEmpty()) {
                    tvCategorySearchAdvance.setText(category.getNameChinese());
                } else {
                    tvCategorySearchAdvance.setText(category.getName());
                }
            } else {
                tvCategorySearchAdvance.setText(category.getName());
            }

        }
        llAdvanceSearchMain = (LinearLayout) dialog.findViewById(R.id.llAdvanceSearchMain);
        llCitiesDialogSearch = (LinearLayout) dialog.findViewById(R.id.llCitiesDialogSearch);
        llCategoryDialogSearch = (LinearLayout) dialog.findViewById(R.id.llCategoryDialogSearch);

        // rbDistanceSearchAdvance = (ImageView) dialog.findViewById(R.id.rbDistanceSearchAdvance);
        tbAllowDistance = (ToggleButton) dialog.findViewById(R.id.tbAllowDistance);
        tbAllowDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllowDistance = !isAllowDistance;
                tbAllowDistance.setChecked(isAllowDistance);
                rangeSeekBarDistance.setEnabled(isAllowDistance);
            }
        });

        rbPriceSearchAdvance = (ImageView) dialog.findViewById(R.id.rbPriceSearchAdvance);
        rbDaySearchAdvance = (ImageView) dialog.findViewById(R.id.rbDaySearchAdvance);

        //    rbDistanceSearchAdvance.setImageResource(ic_check);
        rbPriceSearchAdvance.setImageResource(ic_check);
        rbDaySearchAdvance.setImageResource(ic_check);

        llAllDistance = (LinearLayout) dialog.findViewById(R.id.llAllDistance);

        llAllPrice = (LinearLayout) dialog.findViewById(R.id.llAllPrice);
        llAllPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllPrice = !isAllPrice;
                if (isAllPrice) {
                    rbPriceSearchAdvance.setImageResource(ic_checked);
                } else {
                    rbPriceSearchAdvance.setImageResource(ic_check);
                }


            }
        });
        llAllDay = (LinearLayout) dialog.findViewById(R.id.llAllDay);
        llAllDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isAllDay = !isAllDay;
                if (isAllDay) {
                    rbDaySearchAdvance.setImageResource(ic_checked);
                } else {
                    rbDaySearchAdvance.setImageResource(ic_check);
                }
            }
        });

        edtSearchDialogAdvanceSearch = (EditText) dialog.findViewById(R.id.edtSearchDialogAdvanceSearch);

        //=====
        mapFragment = (com.google.android.gms.maps.MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ibBackDialogSearchFromCity = (ImageView) dialog.findViewById(R.id.ibBackDialogSearchFromCity);
        ibBackDialogSearchFromCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAdvanceSearchMain.setVisibility(View.VISIBLE);
                llCitiesDialogSearch.setVisibility(View.GONE);
                llCategoryDialogSearch.setVisibility(View.GONE);
                rlCitiesDialogSearch.setVisibility(View.GONE);
                rlStateDialogSearch.setVisibility(View.GONE);
                rlLocationDialogSearch.setVisibility(View.GONE);
                if (citySelected != null) {
                    tvCitySearchAdvance.setText(citySelected.getCity());
                }
            }
        });


        ibBackCitiesDialogSearch = (ImageView) dialog.findViewById(R.id.ibBackCitiesDialogSearch);
        ibBackCitiesDialogSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlCitiesDialogSearch.setVisibility(View.GONE);
                rlStateDialogSearch.setVisibility(View.GONE);
                rlLocationDialogSearch.setVisibility(View.VISIBLE);
            }
        });

        ibBackStateDialogSearch = (ImageView) dialog.findViewById(R.id.ibBackStateDialogSearch);
        ibBackStateDialogSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlCitiesDialogSearch.setVisibility(View.GONE);
                rlStateDialogSearch.setVisibility(View.GONE);
                rlLocationDialogSearch.setVisibility(View.VISIBLE);
            }
        });

        tvStateDialogSearch = (TextView) dialog.findViewById(R.id.tvStateDialogSearch);
        tvStateDialogSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAdvanceSearchMain.setVisibility(View.GONE);
                llCitiesDialogSearch.setVisibility(View.VISIBLE);
                llCategoryDialogSearch.setVisibility(View.GONE);
                rlCitiesDialogSearch.setVisibility(View.GONE);
                rlStateDialogSearch.setVisibility(View.VISIBLE);
                rlLocationDialogSearch.setVisibility(View.GONE);
            }
        });
        tvCityDialogSearch = (TextView) dialog.findViewById(R.id.tvCityDialogSearch);
        tvCityDialogSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAdvanceSearchMain.setVisibility(View.GONE);
                llCitiesDialogSearch.setVisibility(View.VISIBLE);
                llCategoryDialogSearch.setVisibility(View.GONE);
                rlCitiesDialogSearch.setVisibility(View.VISIBLE);
                rlStateDialogSearch.setVisibility(View.GONE);
                rlLocationDialogSearch.setVisibility(View.GONE);
            }
        });
        rlCitiesDialogSearch = (RelativeLayout) dialog.findViewById(R.id.rlCitiesDialogSearch);
        rlStateDialogSearch = (RelativeLayout) dialog.findViewById(R.id.rlStateDialogSearch);
        rlLocationDialogSearch = (RelativeLayout) dialog.findViewById(R.id.rlLocationDialogSearch);

        tvApplyCityState = (TextView) dialog.findViewById(R.id.tvApplyCityState);
        tvApplyCityState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAdvanceSearchMain.setVisibility(View.VISIBLE);
                llCitiesDialogSearch.setVisibility(View.GONE);
                llCategoryDialogSearch.setVisibility(View.GONE);
                rlCitiesDialogSearch.setVisibility(View.GONE);
                rlStateDialogSearch.setVisibility(View.GONE);
                rlLocationDialogSearch.setVisibility(View.GONE);
                if (citySelected != null) {
                    tvCitySearchAdvance.setText(citySelected.getCity());
                }
            }
        });

        btnCancelCityState = (TextView) dialog.findViewById(R.id.btnCancelCityState);
        btnCancelCityState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAdvanceSearchMain.setVisibility(View.VISIBLE);
                llCitiesDialogSearch.setVisibility(View.GONE);
                llCategoryDialogSearch.setVisibility(View.GONE);
                rlCitiesDialogSearch.setVisibility(View.GONE);
                rlStateDialogSearch.setVisibility(View.GONE);
                rlLocationDialogSearch.setVisibility(View.GONE);
                if (citySelected != null) {
                    tvCitySearchAdvance.setText(citySelected.getCity());
                }
            }
        });
        if (isFromSplash) {
            edtSearchDialogAdvanceSearch.setVisibility(View.GONE);
        }
    }

    private void processShowScreenSelectCity() {
        llAdvanceSearchMain.setVisibility(View.GONE);
        llCitiesDialogSearch.setVisibility(View.VISIBLE);
        llCategoryDialogSearch.setVisibility(View.GONE);
        rlCitiesDialogSearch.setVisibility(View.GONE);
        rlStateDialogSearch.setVisibility(View.GONE);
        rlLocationDialogSearch.setVisibility(View.VISIBLE);

        if (citySelected != null) {
            tvCityDialogSearch.setText(citySelected.getCity());
            tvStateDialogSearch.setText(citySelected.getState());
        }
    }

    private void showScreenForDisplayCategory() {
        llAdvanceSearchMain.setVisibility(View.GONE);
        llCitiesDialogSearch.setVisibility(View.GONE);
        llCategoryDialogSearch.setVisibility(View.VISIBLE);
        rlCitiesDialogSearch.setVisibility(View.GONE);
        rlStateDialogSearch.setVisibility(View.GONE);
        rlLocationDialogSearch.setVisibility(View.GONE);
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
            for (int i = 0; i < jsonArray.length(); i++) {
                City city = gson.fromJson(jsonArray.get(i).toString(), City.class);
                if (city != null) {
                    cities.add(city);
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
                tvCitySearchAdvance.setText(cities.get(position).getCity());

                LatLng latLng = new LatLng(cities.get(position).getLatitude(), cities.get(position).getLongitude());
                citySelected = cities.get(position);
                Log.e("citiesAdapter", "position " + position);
                Log.e("citiesAdapter", "cities " + cities);
                llAdvanceSearchMain.setVisibility(View.VISIBLE);
                llCitiesDialogSearch.setVisibility(View.GONE);
                llCategoryDialogSearch.setVisibility(View.GONE);
                rlCitiesDialogSearch.setVisibility(View.GONE);
                rlStateDialogSearch.setVisibility(View.GONE);
                rlLocationDialogSearch.setVisibility(View.GONE);
            }
        });

    }

    public void processDataMenu() {
        for (CategoryNew category : categories) {
            mapCategory.put(category.getLegacy_id(), category);
            if (category != null && category.getLegacy_id() != null && category.getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                categoryAll = category;
                categoryAllId = category.getLegacy_id();
                categorySelectedId = categoryAllId;
            }
            if (category.getSort() > 0) {
                if (category != null && !category.getLegacy_id().equals(Constant.sosokanCategoryAll)
                        && category.getParentId() == null) {
                    categoriesChild.add(category);
                }

            }
            List<String> categoriesId;
            if (category.getParentId() != null) {
                categoriesId = mapCategoryChildren.get(category.getParentId());
                if (categoriesId == null) {
                    categoriesId = new ArrayList<>();
                }
                categoriesId.add(category.getLegacy_id());
                mapCategoryChildren.put(category.getParentId(), categoriesId);
            }
        }
        Collections.sort(categoriesChild, new CategoryApiSortComparator());
        categoriesChild.add(0, categoryAll);
        DataProviderCategoryApi.initData(mapCategory, mapCategoryChildren, categories);
        categorySelected = categoriesChild.get(0);
        listAdapter.setDataItems(categoriesChild);

    }

    private void initViewRecycleCategory(Dialog dialog, Activity activity) {
        mListView = (MultiLevelListView) dialog.findViewById(R.id.listViewCategorySelect);
        listAdapter = new ListCategoryApiAdapter(activity);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mapCategoryChildren = new HashMap<>();
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        prefManager = new PrefManager(activity);
        categories = prefManager.getListCategoriesApi();
        if (categories != null && categories.size() > 0) {
            processDataMenu();
        } else {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL);
            databaseReference.child(Constant.CATEGORIES).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    categories = new ArrayList<>();
                    categoriesChild = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        try {
                            CategoryNew category = postSnapshot.getValue(CategoryNew.class);
                            if (category != null && category.getLegacy_id() != null && !category.getLegacy_id().isEmpty()) {

                                categories.add(category);
                                mapCategory.put(category.getLegacy_id(), category);
                            }
                        } catch (Exception ex) {
                            Log.e(TAG, "GET CATEGORY ERROR: " + ex.toString());
                            Log.e(TAG, "GET CATEGORY postSnapshot: " + postSnapshot);
                        }
                    }
                    Gson gson = new Gson();
                    prefManager.setCategoriesFirebase(gson.toJson(categories));


                    processDataMenu();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });
        }


    }

    private OnItemCategoryClickListener mOnItemClickListener = new OnItemCategoryClickListener() {

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            CategoryNew category = ((CategoryNew) item);
            if (category != null) {
                categorySelected = category;
                categorySelectedId = category.getLegacy_id();
                Log.e("onItemClick cate", categorySelectedId);
                if (categorySelected.getChildren() != null && categorySelected.getChildren().length > 0) {
                    Log.e("onGroupItemClicked cate", "processWhenSelectedCategory");

                } else {
                    processWhenSelectedCategory();
                }
            }
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            CategoryNew category = ((CategoryNew) item);
            if (category != null) {
                categorySelectedId = category.getLegacy_id();
                categorySelected = category;
                Log.e("onGroupItemClicked cate", categorySelectedId);
                Log.e("onGroupItemClicked cate", String.valueOf(categorySelected.getChildren()));

                if (categorySelected.getChildren() != null && categorySelected.getChildren().length > 0) {
                    Log.e("onGroupItemClicked cate", "processWhenSelectedCategory");

                } else {
                    processWhenSelectedCategory();
                }
            }
        }
    };

    private void processWhenSelectedCategory() {
        llAdvanceSearchMain.setVisibility(View.VISIBLE);
        llCitiesDialogSearch.setVisibility(View.GONE);
        llCategoryDialogSearch.setVisibility(View.GONE);
        rlCitiesDialogSearch.setVisibility(View.GONE);
        rlStateDialogSearch.setVisibility(View.GONE);
        rlLocationDialogSearch.setVisibility(View.GONE);
        if (categorySelected != null) {
            String nameCategory;
            if (isChineseApp) {
                nameCategory = categorySelected.getNameChinese() == null ? categorySelected.getName() : categorySelected.getNameChinese();
            } else {
                nameCategory = categorySelected.getName();
            }
            tvCategorySearchAdvance.setText(nameCategory);
        }
    }

    //====== CITY
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

            //=============
            if (category != null) {
                this.categorySelectedId = category.getLegacy_id();
            }
            if (dialog != null) {
                //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.setCancelable(false);
                //dialog.setContentView(R.layout.dialog_search_advanced);
                if (activity != null) {
                    String languageToLoad = LocaleHelper.getLanguage(activity);
                    isChineseApp = languageToLoad.toString().equals("zh");
                }
                if (prefManager == null) prefManager = new PrefManager(activity);
                if (prefManager.getListCities() == null || prefManager.getListCities().size() <= 0) {
                    initValueCities(activity);
                } else {
                    cities = prefManager.getListCities();

                }
                ic_checked = activity.getResources().getIdentifier("ic_checked", "drawable", activity.getPackageName());
                ic_check = activity.getResources().getIdentifier("ic_check", "drawable", activity.getPackageName());
                // initView(activity, dialog);
                initView(activity, dialog, fragmentManager);
                setupRecycleViewCities(dialog);
                setupRecycleViewState(dialog, activity);
                initViewRecycleCategory(dialog, activity);
                getAddressFromLocation();

            }
            //============


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
                tvStateDialogSearch.setText(states.get(position));
                tvCityDialogSearch.setText(activity.getResources().getString(R.string.all_cities_lowercase));
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
                        tvCityDialogSearch.setText(cities.get(position).getCity());
                        tvStateDialogSearch.setText(cities.get(position).getState());
                        LatLng latLng = new LatLng(cities.get(position).getLatitude(), cities.get(position).getLongitude());
                        displayLocation(latLng, cities.get(position).getCity());
                        citySelected = cities.get(position);
                        Log.e("citiesAdapter", "position " + position);
                        Log.e("citiesAdapter", "cities " + cities);
                        rlCitiesDialogSearch.setVisibility(View.GONE);
                        rlStateDialogSearch.setVisibility(View.GONE);
                        rlLocationDialogSearch.setVisibility(View.VISIBLE);
                    }
                });
                if (citiesOfState != null && citiesOfState.size() > 0) {
                    citySelected = citiesOfState.get(0);
                } else {
                    citySelected = null;
                }
                rlCitiesDialogSearch.setVisibility(View.GONE);
                rlStateDialogSearch.setVisibility(View.GONE);
                rlLocationDialogSearch.setVisibility(View.VISIBLE);
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

            if (userSelection == null) {
                sendBroadcastUnselectSearchAdvance();
            }
        }

    }

    private void sendBroadcastUnselectSearchAdvance() {
        Log.e("onDestroyView", "unselected ");
        Intent intent = new Intent(MenuActivity.ACTION_UNSELECT_SEARCH_ADVANCE);
        activity.sendBroadcast(intent);
    }
}
