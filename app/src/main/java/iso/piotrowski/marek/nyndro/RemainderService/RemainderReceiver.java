package iso.piotrowski.marek.nyndro.RemainderService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RemainderReceiver extends BroadcastReceiver {
    public RemainderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, RemainderService.class));
    }
}
