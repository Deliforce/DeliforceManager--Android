package com.app.deliforcemanager.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Utils.LoginPrefManager;

import java.io.IOException;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {

    LoginPrefManager loginPrefManager;

    MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mediaPlayer.start();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        loginPrefManager = new LoginPrefManager(getBaseContext());

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri ringtoneSelected;
        String ringtonePath;
        try {
            ringtonePath = loginPrefManager.getStringValue("ringtoneUri");
            if (ringtonePath.equals("") || ringtonePath.isEmpty()) {
                ringtoneSelected = defaultSoundUri;
            } else
                ringtoneSelected = Uri.parse(ringtonePath);
        } catch (Exception e) {
            e.printStackTrace();
            ringtoneSelected = defaultSoundUri;
        }

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(getBaseContext().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSound(ringtoneSelected, AudioManager.STREAM_NOTIFICATION)
                    .setContentText("").build();

            notification.flags |= Notification.FLAG_INSISTENT;
            startForeground(300, notification);
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, ringtoneSelected);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mediaPlayer.setLooping(true);
                //mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        stopSelf();
        super.onDestroy();
    }
}
