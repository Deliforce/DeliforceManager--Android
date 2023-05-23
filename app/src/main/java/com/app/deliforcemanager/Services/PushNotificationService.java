package com.app.deliforcemanager.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import static com.app.deliforcemanager.FCM.MyFirebaseMessagingService.notificationCancellableId;

public class PushNotificationService extends Service {

    NotificationManager notificationManager;
    private CountDownTimer notification_timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(getBaseContext().getString(R.string.app_name))
                    .setContentText("").build();

            startForeground(1, notification);
        }

        //ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Activity.ACTIVITY_SERVICE);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        LoginPrefManager loginPrefManager = new LoginPrefManager(getBaseContext());

        long expirty_time = Long.parseLong(loginPrefManager.getStringValue("expirty_time"));

        expirty_time = expirty_time * 1000;

        notification_timer = new CountDownTimer(expirty_time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("TAG ", "Countdown seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                notificationManager.cancel(notificationCancellableId);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("receiver").putExtra("send_data", "1"));
                stopSelf();
                stopService(new Intent(getBaseContext(), PushNotificationService.class));
            }
        };

        notification_timer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        notification_timer.cancel();
        stopSelf();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); //true will remove notification
        }

    }
}
