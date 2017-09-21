package iso.piotrowski.marek.nyndro.RemainderService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.compat.BuildConfig;
import android.util.Log;

public class RemainderReceiver extends BroadcastReceiver {
    private static final String TAG = "RemainderReceiver";

    public RemainderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) Log.d(TAG, "[service] onReceive: restart service");
        startService(context);
    }

    private void startService(Context context) {
        Intent intent = new Intent(context, RemainderService.class);
        context.startService(intent);
    }


}
