package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sosokan.android.R;
import com.sosokan.android.control.multi.level.menu.ItemInfo;
import com.sosokan.android.control.multi.level.menu.ListCategoryApiAdapter;
import com.sosokan.android.control.multi.level.menu.MultiLevelListView;
import com.sosokan.android.control.multi.level.menu.OnItemCategoryClickListener;
import com.sosokan.android.models.Category;
import com.sosokan.android.utils.comparator.CategorySortComparator;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AnhZin on 11/18/2016.
 */

public class SelectCategoryActivity extends Activity implements View.OnClickListener {
    private MultiLevelListView mListView;
    //private DatabaseReference mDatabaseCategory;
    private List<Category> categories, categoriesChild;
    public String categoryAllId = "All";
    public Category categoryAll = null;
    public static Map<String, Category> mapCategory;
    public static Map<String, List<String>> mapCategoryChildren;
    ListCategoryApiAdapter listAdapter;
    public String  categorySelectedId;
    Category categorySelected;
    LinearLayout llCategory;

    ImageButton ibBackSelectCategory;
    boolean isChineseApp;
    private DatabaseReference databaseReference, mDatabaseUser;

    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_select_category);
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        mapCategoryChildren = new HashMap<>();
        categorySelectedId = Constant.sosokanCategoryAll;

        String languageToLoad = LocaleHelper.getLanguage(SelectCategoryActivity.this);
        isChineseApp = languageToLoad.toString().equals("zh");

        initView();
        initViewRecycleCategory();
    }
    private void initView() {
        ibBackSelectCategory = (ImageButton) findViewById(R.id.ibBackSelectCategory);
        ibBackSelectCategory.setOnClickListener(this);


    }
    public void processDataMenu() {
        for (Category category : categories) {
            mapCategory.put(category.getId(), category);
            if (category != null && category.getId() != null && category.getId().equals(Constant.sosokanCategoryAll)) {
                categoryAll = category;
                categoryAllId = category.getId();
                categorySelectedId = categoryAllId;
            }
            if (category.getSort() > 0) {
                if (category != null && !category.getId().equals(Constant.sosokanCategoryAll)
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
                categoriesId.add(category.getId());
                mapCategoryChildren.put(category.getParentId(), categoriesId);
            }
        }
        Collections.sort(categoriesChild, new CategorySortComparator());
        categoriesChild.add(0, categoryAll);
        //DataProviderCategory.initData(mapCategory, mapCategoryChildren, categories);

        categorySelected = categoriesChild.get(0);
        listAdapter.setDataItems(categoriesChild);

    }

    private void initViewRecycleCategory() {
        mListView = (MultiLevelListView) findViewById(R.id.listViewCategorySelect);
        listAdapter = new ListCategoryApiAdapter(SelectCategoryActivity.this);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mapCategoryChildren = new HashMap<>();
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        prefManager = new PrefManager(this);
        categories = prefManager.getListCategoriesFirebase();
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
                            Category category = postSnapshot.getValue(Category.class);
                            if (category != null && category.getId() != null && !category.getId().isEmpty()) {

                                categories.add(category);
                                mapCategory.put(category.getId(), category);
                            }
                        } catch (Exception ex) {
                            Log.e("SELECT CATEGORY", "GET CATEGORY ERROR: " + ex.toString());
                            Log.e("SELECT CATEGORY", "GET CATEGORY postSnapshot: " + postSnapshot);
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
            Category category = ((Category) item);
            if(category!= null)
            {
                categorySelectedId = category.getId();
                categorySelected = category;
            }
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            Category category = ((Category) item);
            if(category!= null)
            {
                categorySelectedId = category.getId();
                categorySelected = category;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ibBackSelectCategory:
                if(categorySelected!=null)
                {
                  String nameCategory;
                    if (isChineseApp) {
                        nameCategory = categorySelected.getNameChinese()  == null? categorySelected.getName(): categorySelected.getNameChinese();
                    } else {
                        nameCategory = categorySelected.getName();
                    }
                    Intent    intent = new Intent(this, NewAdvertiseActivity.class);
                    intent.putExtra("categorySelectedId",categorySelectedId);
                    intent.putExtra("nameCategory",nameCategory);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                    //finish();
                }


                break;
        }
    }


}
