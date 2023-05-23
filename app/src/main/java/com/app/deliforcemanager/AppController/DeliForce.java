package com.app.deliforcemanager.AppController;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.app.deliforcemanager.R;
/*import com.applozic.mobicomkit.Applozic;*/
import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class DeliForce extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private static Context applicationContext = null;
    private WeakReference<Activity> mActivity = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        //if (BuildConfig.DEBUG)
        Timber.plant(new Timber.DebugTree());

        applicationContext = getApplicationContext();
        MapsInitializer.initialize(this);
        registerActivityLifecycleCallbacks(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
      /*  Applozic.init(getApplicationContext(), getString(R.string.appLogicId));*/

        MultiDex.install(this);

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setApplicationId(getString(R.string.google_app_id)) // Required for Analytics.
                .setProjectId(getString(R.string.project_id)) // Required for Firebase Installations.
                .setApiKey(getString(R.string.google_api_key)) // Required for Auth.
                .build();
        FirebaseApp.initializeApp(this, firebaseOptions,"Deliforce");
    }

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivity.clear();
    }

}