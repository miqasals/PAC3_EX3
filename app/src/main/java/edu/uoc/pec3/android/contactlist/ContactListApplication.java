package edu.uoc.pec3.android.contactlist;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by mgarcia on 24/03/2016.
 */
public class ContactListApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // The Firebase library must be initialized once with an Android context
        Firebase.setAndroidContext(getApplicationContext());
        // Enabling disk persistence allows our app to also keep all of its state even after an app restart
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        /*
         * FIREBASE API: setPersistenceEnabled(true)
         * By default the Firebase client will keep data in memory while your application is running, but
         * not when it is restarted. By setting this value to true, the data will be persisted to on-device
         * (disk) storage and will thus be available again when the app is restarted (even when there is
         * no network connectivity at that time). Note that this method must be called before creating your
         * first Firebase reference and only needs to be called once per application.
         */
    }
}
