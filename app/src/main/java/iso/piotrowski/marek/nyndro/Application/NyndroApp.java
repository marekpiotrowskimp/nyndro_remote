package iso.piotrowski.marek.nyndro.Application;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

/**
 * Created by marek.piotrowski on 12/08/2017.
 */

public class NyndroApp extends com.activeandroid.app.Application {

    private static NyndroApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ActiveAndroid.initialize(getApplicationContext());
    }

    public static Context getContect() {
        return instance;
    }


}
