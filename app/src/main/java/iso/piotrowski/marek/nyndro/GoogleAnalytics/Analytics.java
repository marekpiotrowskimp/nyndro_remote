package iso.piotrowski.marek.nyndro.GoogleAnalytics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by marek.piotrowski on 16/09/2017.
 */

public class Analytics {
    private static FirebaseAnalytics firebaseAnalytics = null;

    public enum TypeOfEvent{
        Fragment, AddPractice, AddHistory, AddRemainder
    }

    public static void startAnalytics(AppCompatActivity activity) {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        }
    }

    public static void logEvent(String id, String name, String contentType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void logPracticeEvent(String type, String name, String description) {
        Bundle params = new Bundle();
        params.putString("name", name);
        params.putString("description", description);
        firebaseAnalytics.logEvent(type, params);
    }

    public static void logErrorEvent(String type, String name, String description) {
        Bundle params = new Bundle();
        params.putString("name", name);
        params.putString("description", description);
        firebaseAnalytics.logEvent(type, params);
    }
}
