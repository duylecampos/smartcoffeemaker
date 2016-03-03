package nkey.com.smartcoffeemaker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import nkey.com.smartcoffeemaker.MainActivity;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.startReadFromServer(context);
    }
}
