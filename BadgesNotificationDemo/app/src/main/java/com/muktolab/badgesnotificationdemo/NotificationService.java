package com.muktolab.badgesnotificationdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationService extends Service {
    private int notificationID = 111;
    private NotificationManager notificationManager;
    private int badgesCount = 0;
    private Timer timer;
    private Handler handler = new Handler();

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("NOTIFICATION_TAG","============ Notification onCreate Level =====================");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("NOTIFICATION_TAG","============ Notification onStartCommand Level =====================");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("NOTIFICATION_TAG","============ Notification Inside handler Level =====================");
                        showNotificationWithBadges();
                    }
                });
            }
        },0,10000);
        return START_STICKY;
    }

    private void showNotificationWithBadges() {
        try {
            Log.d("NOTIFICATION_TAG","============ Notification start showNotificationWithBadges Level =====================");
            ++badgesCount;
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Demo Notification")
                    .setContentText("Demo notification test with badges.");
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(new Intent(this, HomeActivity.class));
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);
            ShortcutBadger.applyCount(this,badgesCount);
            notificationManager.notify(notificationID,notification.build());
            Log.d("NOTIFICATION_TAG","============ Notification send Level =====================");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("NOTIFICATION_TAG","============ Notification onTaskRemove Level =====================");
        startService(new Intent(this, this.getClass()));
        Log.d("NOTIFICATION_TAG","============ Notification end start service Level =====================");
        super.onTaskRemoved(rootIntent);
        Log.d("NOTIFICATION_TAG","============ Notification end onTaskRemove Level =====================");
    }
}
