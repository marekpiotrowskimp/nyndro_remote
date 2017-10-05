package iso.piotrowski.marek.nyndro.RemainderService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.compat.BuildConfig;
import android.util.Log;

import iso.piotrowski.marek.nyndro.tools.Utility;

/**
 * Created by marek.piotrowski on 21/09/2017.
 */

public class RebootReceiver extends BroadcastReceiver {
    private static final String TAG = "RebootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Utility.setAlarm(context, 60000);
        if (BuildConfig.DEBUG) Log.d(TAG, "setAlarm: [service] ALARM");
    }
}
