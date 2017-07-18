package com.sosokan.android.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by AnhZin on 8/25/2016.
 */
public class PermissionGrantedHelper {
    Activity activity;
    public PermissionGrantedHelper(Activity activity) {
        this.activity = activity;
    }

    public void checkAndRequestPermissionForMap(){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PackageManager.PERMISSION_GRANTED);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PackageManager.PERMISSION_GRANTED);
        }
    }

    public boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return activity.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    public boolean checkAnPermissionCamera() {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkAnPermissionWriteStorage() {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    public boolean checkAnPermissionReadStorage() {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkAnPermissionRecordAudio() {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    public void checkAndRequestPermissionForCamera() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    PackageManager.PERMISSION_GRANTED);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PackageManager.PERMISSION_GRANTED);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PackageManager.PERMISSION_GRANTED);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PackageManager.PERMISSION_GRANTED);
        }
    }

    public boolean checkPermissionLocation()
    {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void checkAndRequestPermissionLocation()
    {
        if ( ActivityCompat.checkSelfPermission( activity, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PackageManager.PERMISSION_GRANTED);
        }
    }

    public void requestPermissionCall()
    {
        if ( ActivityCompat.checkSelfPermission( activity, android.Manifest.permission.CALL_PHONE )
                != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PackageManager.PERMISSION_GRANTED);
        }else   if ( ActivityCompat.checkSelfPermission( activity, android.Manifest.permission.READ_PHONE_STATE )
                != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PackageManager.PERMISSION_GRANTED);
        }
    }
    public boolean checkPermissionCall()
    {
        if ( ActivityCompat.checkSelfPermission( activity, android.Manifest.permission.CALL_PHONE )
                != PackageManager.PERMISSION_GRANTED ) {

            return false;
        }else  if ( ActivityCompat.checkSelfPermission( activity, android.Manifest.permission.READ_PHONE_STATE )
                != PackageManager.PERMISSION_GRANTED ) {
                    return false;
        }
        return true;
    }

    public boolean checkPermissionForMap(){

        if ((ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)||(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
         return false;
        }
        return true;
    }

}
