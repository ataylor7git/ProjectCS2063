package ca.unb.mobiledev.projectcs2063;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private final static String TAG = "Alarm Receiver";

    private int notificationId = 0;
    private int notificationId2 = 1;
    private static final String WATERCHANNELID = "WaterServiceNotif" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive method called");

        Intent mainIntent = new Intent(context, WaterFragment.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"channel_id")
                .setSmallIcon(R.drawable.water)
                .setContentTitle("Water Reminder")
                .setContentText("It's time to drink water")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
        createNotificationChannel(context);

    }

    public static void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Water Reminder";
            String description = "It's time to drink water";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

        }
    }

    public static void createWaterNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Congratulations!";
            String description = "You have reached your daily water goal!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(WATERCHANNELID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

        }
    }

}
