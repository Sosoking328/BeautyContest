package com.sosokan.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sosokan.android.deps.DaggerDeps;
import com.sosokan.android.deps.Deps;
import com.sosokan.android.networking.NetworkModule;

import java.io.File;

/**
 * Created by macintosh on 3/16/17.
 */

public class BaseApp extends AppCompatActivity {
    Deps deps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();

    }

    public Deps getDeps() {
        return deps;
    }
}

