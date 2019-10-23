package com.forest.tinkerfix;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.forest.library.FixDexUtils;

public class BaseApplication extends MultiDexApplication {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        FixDexUtils.loadFixedDex(this);
    }
}
