package com.sosokan.android.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AnhZin on 10/16/2016.
 */

public class FireBaseUtils {
    private static DatabaseReference refTemp, refTempId;
    private static DatabaseReference ref, mDatabaseUser;
    public static String generateId() {
        ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL);
        refTemp = ref.child("temp");
        refTempId = refTemp.push();
        Map<String, String> post = new HashMap<String, String>();
        post.put("author", "gracehop");
        refTempId.setValue(post);
        String generationId = refTempId.getKey();
        return generationId;
    }
}
