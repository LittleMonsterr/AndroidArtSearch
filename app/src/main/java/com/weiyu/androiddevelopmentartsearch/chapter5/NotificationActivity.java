package com.weiyu.androiddevelopmentartsearch.chapter5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.weiyu.androiddevelopmentartsearch.MainActivity;
import com.weiyu.androiddevelopmentartsearch.R;

import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

       /* Notification notification = new Notification();
        notification.icon = R.drawable.k1;
        notification.tickerText = "hello world";
        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this,"chapter_5","this is notification",pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);*/

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //API level 11
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("chapter_5")
                .setContentText("this is notification")
                .setSmallIcon(R.drawable.k1)
                .setWhen(System.currentTimeMillis())
                .setTicker("hello world")
                .setAutoCancel(true);
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = builder.getNotification();
        manager.notify(1,notification);

        Notification.Builder builder1 = new Notification.Builder(this)
                .setSmallIcon(R.drawable.k1)
                .setTicker("hello world")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification);
        remoteViews.setTextViewText(R.id.tv_chapter5_notifmsg,"chapter_5");
        remoteViews.setImageViewResource(R.id.iv_chapter5_notificon,R.drawable.k2);
        PendingIntent openMainActivityPendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_chapter5_notifopenactivity,openMainActivityPendingIntent);
        Notification notification1 = builder1.getNotification();
        notification1.contentView = remoteViews;
        notification1.contentIntent =pendingIntent;
        manager.notify(2,notification1);
    }
}
