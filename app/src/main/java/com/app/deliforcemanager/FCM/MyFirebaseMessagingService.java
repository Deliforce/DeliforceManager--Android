package com.app.deliforcemanager.FCM;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.deliforcemanager.Activities.NavigationActivity;
import com.app.deliforcemanager.Activities.NewTaskActivity;
import com.app.deliforcemanager.AppController.DeliForce;
import com.app.deliforcemanager.ModelClass.TaskNotificationModel;
import com.app.deliforcemanager.R;
import com.app.deliforcemanager.Services.MusicService;
import com.app.deliforcemanager.Services.PushNotificationService;
import com.app.deliforcemanager.Utils.DeliforceConstants;
import com.app.deliforcemanager.Utils.LoginPrefManager;
/*import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegisterUserClientService;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.notification.MobiComPushReceiver;*/
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import timber.log.Timber;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int NOTIFICATION_STATUS_TASK_CREATED = 1;
    private static final int NOTIFICATION_STATUS_DRIVER_OFFLINE = 7;
    private static final int NOTIFICATION_STATUS_DRIVER_IDLE = 8;
    private static final int NOTIFICATION_STATUS_AUTO_ALLOCATION = 4;
    private static final int NOTIFICATION_STATUS_AUTO_ALLOCATION_NEAR = 5;
    private static final int NOTIFICATION_STATUS_TASK_SUCCESS = 6;
    private static final int TASK_UPDATE = 2;
    private static final int TASK_CHANGE = 25;
    public static int notificationCancellableId = 78;
    LoginPrefManager loginPrefManager;
    ArrayList<TaskNotificationModel> taskNotificationModels;
    String notificationTitle = "";
    Vibrator vibrator;
    String driver_msg;
    NotificationManager notificationManager;
    String notificationTypeTaskStatus;
    NotificationCompat.BigTextStyle bigText;

    @Override
    public void onNewToken(String newToken) {
        if (newToken != null) {
            LoginPrefManager preference = new LoginPrefManager(DeliForce.getContext());
            preference.setDeviceToken(newToken);
        }

      /*  Applozic.getInstance(this).setDeviceRegistrationId(newToken);
        if (MobiComUserPreference.getInstance(this).isRegistered()) {
            try {
                RegistrationResponse registrationResponse = new RegisterUserClientService(this).updatePushNotificationId(newToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        loginPrefManager = new LoginPrefManager(getBaseContext());
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {



      //  Log.e("fcmData",new Gson().toJson(remoteMessage));

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (!remoteMessage.getData().isEmpty()) {
           /* if (MobiComPushReceiver.isMobiComPushNotification(remoteMessage.getData())) {
                MobiComPushReceiver.processMessageAsync(this, remoteMessage.getData());
            } else {*/



           // Log.e("fcmData",new Gson().toJson(remoteMessage));




            fetchNotification(remoteMessage);







            //   }
        }
    }

    private void fetchNotification(RemoteMessage remoteMessage) {
        taskNotificationModels = new ArrayList<>();

        bigText = new NotificationCompat.BigTextStyle();

        Type listType = new TypeToken<ArrayList<TaskNotificationModel>>() {
        }.getType();


        String data = remoteMessage.getData().get("data");

        taskNotificationModels = new Gson().fromJson(data, listType);
        if (taskNotificationModels != null && !taskNotificationModels.isEmpty()) {
            notificationTitle = taskNotificationModels.get(0).getTitle();
            String message = taskNotificationModels.get(0).getRequest();
            driver_msg = taskNotificationModels.get(0).getMessage();
            String expiry = taskNotificationModels.get(0).getExpiry();
            if (message != null) {
                stopService(new Intent(getBaseContext(), PushNotificationService.class));
                notificationManager.cancelAll();
                return;
            }
            if (expiry != null) {
                loginPrefManager.setStringValue("expirty_time", expiry);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    startService(new Intent(getBaseContext(), PushNotificationService.class));
                } else {
                    startForegroundService(new Intent(getBaseContext(), PushNotificationService.class));
                }
            }

            notificationTypeTaskStatus = taskNotificationModels.get(0).getStatus();
            int taskNotificationType = Integer.parseInt(notificationTypeTaskStatus);
            if (taskNotificationType == 25) {
                updateTaskList(notificationTitle);
            }
            if (taskNotificationType == NOTIFICATION_STATUS_TASK_CREATED ||
                    taskNotificationType == NOTIFICATION_STATUS_AUTO_ALLOCATION) {
                sendNotification(notificationTitle);
            } else if (taskNotificationType == NOTIFICATION_STATUS_AUTO_ALLOCATION_NEAR) {
                sendTaskUpdatedNotification(notificationTitle);
            } else if (taskNotificationType == TASK_UPDATE) {
                if (taskNotificationModels.get(0).getTaskStatus() != null && taskNotificationModels.get(0).getTaskStatus().equalsIgnoreCase(String.valueOf(NOTIFICATION_STATUS_TASK_SUCCESS))) {
                    sendTaskAdminSuccess(notificationTitle);
                } else {
                    sendTaskUpdatedNotification(notificationTitle);
                }
            } else {
                try {
                    if (driver_msg != null) {
                        String driver_status = taskNotificationModels.get(0).getDriverStatus();
                        if (driver_status.equalsIgnoreCase("4")) {
                            sendDriverBlocked(driver_msg);
                        } else {
                            sendTaskUpdatedNotification(driver_msg);
                        }
                    } else {
                        sendTaskUpdatedNotification(notificationTitle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Manually routing the driver to perform the jobs in order by admin: status 25
     **/
    private void updateTaskList(String messageBody) {
        Intent intent = new Intent(this, NewTaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        PendingIntent pendingIntent = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }


        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        boolean isRepeatRing = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_SETTINGS_REPEAT_RING);

        if (isRepeatRing) {
            //startService(new Intent(getBaseContext(), MusicService.class));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(new Intent(getBaseContext(), MusicService.class));
            } else {
                startForegroundService(new Intent(getBaseContext(), MusicService.class));
            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "update_task");
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setSmallIcon(R.drawable.notification_ic);
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_DEFAULT);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int vibrationStateLong = loginPrefManager.getIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG);
        long[] pattern = {50, 100, 100, 250, 150, 350};
        if (vibrationStateLong == 1) {
            notificationBuilder.setVibrate(pattern);
        } else {
            vibrator.vibrate(1000);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(getResources().getString(R.string.task_updated));
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.task_updated), "Task Updated", NotificationManager.IMPORTANCE_HIGH);
            // Creating an Audio Attribute
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.setSound(Uri.parse(""), audioAttributes);
            notificationManager.createNotificationChannel(channel);
            playAudio();
        } else {
            notificationBuilder.setSound(getNotificationUri());
        }

        notificationManager.notify(0, notificationBuilder.build());
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("updated").putExtra("start_order", "1"));
    }

    /**
     * New Task was created by Admin. so we are updating it to driver
     **/
    private void sendNotification(String messageBody) {
        String notifyTitle = messageBody;
        if (taskNotificationModels.size() == 2 && taskNotificationModels.get(0).getReferenceId().equalsIgnoreCase(taskNotificationModels.get(1).getReferenceId())) {
            if (taskNotificationModels.get(0).getJobAmount() != null &&
                    taskNotificationModels.get(1).getJobAmount() != null &&
                    !taskNotificationModels.get(0).getJobAmount().isEmpty() &&
                    !taskNotificationModels.get(1).getJobAmount().isEmpty()) {
                Double jobAmount = Double.parseDouble(taskNotificationModels.get(0).getJobAmount()) + Double.parseDouble(taskNotificationModels.get(1).getJobAmount());
                notifyTitle += " - " + jobAmount + " " + loginPrefManager.getCurrency();
            }
        } else {
            if (taskNotificationModels.get(0).getJobAmount() != null && !taskNotificationModels.get(0).getJobAmount().isEmpty()) {
                notifyTitle += " - " + taskNotificationModels.get(0).getJobAmount() + " " + loginPrefManager.getCurrency();
            }
        }


        String passing_data = new Gson().toJson(taskNotificationModels);
        loginPrefManager.setStringValue("notification_data",passing_data);


        Log.e("notification_send",new Gson().toJson(taskNotificationModels));



        Intent intent = new Intent(this, NewTaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("task", passing_data);


       /* intent.putExtra("cust_name", customerName);
        intent.putExtra("cust_addres", custAddress);
        intent.putExtra("task_date", taskdate);
        intent.putExtra("task_completion_time", taskStartTime);
        intent.putExtra("super_task_id", taskId);
        intent.putExtra("bussId", taskType);
        intent.putExtra("acknowledgementType", ackType);
        intent.putExtra("taskEndDate", taskEndDate);
        intent.putExtra("taskEndTime", taskEndTime);
        intent.putExtra("auto_status", auto_status);
        intent.putExtra("glympse_task_id", glympseID);
        boolean isTaskPickUP = Boolean.parseBoolean(isPickup);
        intent.putExtra("taskIsPickUp", isTaskPickUP);*/

        PendingIntent pendingIntent = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }


        String channelId = "NewTask";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setSmallIcon(R.drawable.notification_ic);
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notificationBuilder.setContentText(notifyTitle);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int vibrationStateLong = loginPrefManager.getIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG);

        if (vibrationStateLong == 1) {
            vibrator.vibrate(4000);
        } else {
            vibrator.vibrate(500);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "New Task", NotificationManager.IMPORTANCE_HIGH);
            // Creating an Audio Attribute
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.setSound(Uri.parse(""), audioAttributes);
            notificationManager.createNotificationChannel(channel);
            playAudio();
        } else {
            notificationBuilder.setSound(getNotificationUri());
        }


        boolean isRepeatRing = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_SETTINGS_REPEAT_RING);

        Notification notificaiton = notificationBuilder.build();

        if (isRepeatRing) {
            //startService(new Intent(getBaseContext(), MusicService.class));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(new Intent(getBaseContext(), MusicService.class));
            } else {
                //startForegroundService(new Intent(getBaseContext(), MusicService.class));
                notificaiton.flags |= Notification.FLAG_INSISTENT;
            }
        }
        int notificationId = 0;

        if (Integer.valueOf(notificationTypeTaskStatus) == NOTIFICATION_STATUS_AUTO_ALLOCATION ||
                Integer.valueOf(notificationTypeTaskStatus) == NOTIFICATION_STATUS_AUTO_ALLOCATION_NEAR
        ) {
            notificationId = notificationCancellableId;
        }

        notificationManager.notify(notificationId, notificaiton);

        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("updated").putExtra("start_order", "1"));
    }

    /**
     * Task was modified, deleted or updated by Admin. so we are notifying it to the driver
     **/
    private void sendTaskUpdatedNotification(String messageBody) {
        Intent intent = new Intent(this, NavigationActivity.class);

        PendingIntent pendingIntent = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int vibrationStateLong = loginPrefManager.getIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG);

        if (vibrationStateLong == 1) {
            vibrator.vibrate(4000);
        } else {
            vibrator.vibrate(500);
        }

        String channelId = getString(R.string.app_name);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setSmallIcon(R.drawable.notification_ic);
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel task Modification", NotificationManager.IMPORTANCE_HIGH);
            // Creating an Audio Attribute
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.setSound(Uri.parse(""), audioAttributes);
            notificationManager.createNotificationChannel(channel);
            playAudio();
        } else {
            notificationBuilder.setSound(getNotificationUri());
        }

        boolean isRepeatRing = loginPrefManager.getBooleanValue(DeliforceConstants.PREF_SETTINGS_REPEAT_RING);

        Notification notificaiton = notificationBuilder.build();

        if (isRepeatRing) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(new Intent(getBaseContext(), MusicService.class));
            } else {
                notificaiton.flags |= Notification.FLAG_INSISTENT;
            }
        }
        int notificationId = 0;

        if (Integer.valueOf(notificationTypeTaskStatus) == NOTIFICATION_STATUS_AUTO_ALLOCATION ||
                Integer.valueOf(notificationTypeTaskStatus) == NOTIFICATION_STATUS_AUTO_ALLOCATION_NEAR
        ) {
            notificationId = notificationCancellableId;
        }
        notificationManager.notify(notificationId, notificaiton);

//        if (remoteMessage != null) {
        String task_status = taskNotificationModels.get(0).getTaskStatus();
        Timber.e("--%s", task_status);
        if (task_status.contains("4") || task_status.contains("6") || task_status.contains("7") || task_status.contains("8") || task_status.contains("9")) {
            if (task_status.contains("4")) {
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("task_start").putExtra("start_task", "3"));
            }
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("service_status").putExtra("server_profile", "102"));
        } else {
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("updated").putExtra("start_order", "3"));
        }
        /*} else {
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("updated").putExtra("start_order", "3"));
        }*/
    }

    /**
     * Driver was blocked by admin for some reason and we notify this to driver and logging him out for the duty
     **/
    private void sendDriverBlocked(String messageBody) {
        Intent intent = new Intent(this, NavigationActivity.class);
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        PendingIntent pendingIntent = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int vibrationStateLong = loginPrefManager.getIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG);

        if (vibrationStateLong == 1) {
            vibrator.vibrate(4000);
        } else {
            vibrator.vibrate(500);
        }


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "driver_blocked");
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setSmallIcon(R.drawable.notification_ic);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.driver_blocked),
                    "Driver Blocked", NotificationManager.IMPORTANCE_HIGH);
            notificationBuilder.setChannelId(getResources().getString(R.string.driver_blocked));
            // Creating an Audio Attribute
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.setSound(Uri.parse(""), audioAttributes);
            notificationManager.createNotificationChannel(channel);
            playAudio();
        } else {
            notificationBuilder.setSound(getNotificationUri());
        }

        notificationManager.notify(0, notificationBuilder.build());

        loginPrefManager.setDriverBlock("4");

        if (!messageBody.equalsIgnoreCase("")) {
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("changeByAdmin").putExtra("profile", "3"));

        }
    }

    /**
     * Task was completed or succeeded by Admin. so we are updating it to driver
     **/
    private void sendTaskAdminSuccess(String messageBody) {
        Intent intent = new Intent(this, NavigationActivity.class);


        PendingIntent pendingIntent = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int vibrationStateLong = loginPrefManager.getIntValue(DeliforceConstants.PREF_SETTINGS_VIBRATE_LONG);

        if (vibrationStateLong == 1) {
            vibrator.vibrate(4000);
        } else {
            vibrator.vibrate(500);
        }
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "admin_task");
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setSmallIcon(R.drawable.notification_ic);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(getResources().getString(R.string.admin_task));
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.admin_task),
                    "Admin Task details", NotificationManager.IMPORTANCE_HIGH);
            // Creating an Audio Attribute
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            channel.setSound(Uri.parse(""), audioAttributes);
            notificationManager.createNotificationChannel(channel);
            playAudio();
        } else {
            notificationBuilder.setSound(getNotificationUri());
        }

        notificationManager.notify(0, notificationBuilder.build());


        if (taskNotificationModels.get(0).getAuto() != null &&
                taskNotificationModels.get(0).getAuto().equals("0")) {
            Timber.e("---");
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("service_status").putExtra("server_profile", "102"));
        } else {
            Timber.e("-------");
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent("receiver").putExtra("new_task", "3"));
        }
    }

    void playAudio() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), getNotificationUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    Uri getNotificationUri() {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri ringtoneSelected = defaultSoundUri;
        String ringtonePath = "";
        try {
            ringtonePath = loginPrefManager.getStringValue("ringtoneUri");
            if (!ringtonePath.isEmpty())
                ringtoneSelected = Uri.parse(ringtonePath);
        } catch (Exception e) {
            e.printStackTrace();
            ringtoneSelected = defaultSoundUri;
        }
        return ringtoneSelected;
    }
}

