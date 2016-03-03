package nkey.com.smartcoffeemaker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nkey.com.smartcoffeemaker.service.ServerStatusService;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TRACKING = 1;
    private static long LOCATION_TRACKER_TIME_BETWEEN_UPDATES = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startReadFromServer(getBaseContext());
        finish();
    }

    public static void startReadFromServer(Context context) {
        Intent serviceIntent = new Intent(context, ServerStatusService.class);
        PendingIntent locationIntent = PendingIntent.getService(context, REQUEST_TRACKING, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), LOCATION_TRACKER_TIME_BETWEEN_UPDATES, locationIntent);
    }
}
