package ca.unb.mobiledev.projectcs2063;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;

import ca.unb.mobiledev.projectcs2063.entity.Item;
import ca.unb.mobiledev.projectcs2063.repository.ItemRepository;

public class StepService extends Service implements SensorEventListener {
    private StepDetector detector = StepsFragment.getDetector();
    private final double STEPTHRESH = 12.9;
    private final static String TAG = "INFO Steps Detector";
    private final String CHANNELID = "StepServiceNotif";
    private int date = 0;

    private static ItemRepository itemRepository;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_STICKY;
    }

    @Override
    public void onCreate()
    {
        if(detector == null)
            detector = new StepDetector();
        SensorManager sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sSensor= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean success = sensorManager.registerListener(this, sSensor, SensorManager.SENSOR_DELAY_NORMAL);
        date = MainActivity.getDate();

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNELID)
                .setContentTitle("Step Detector")
                .setContentText("Counting steps in background")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notif = builder.setOngoing(true)
                .setSmallIcon(R.drawable.walk)
                .build();
        startForeground(101, notif);


        super.onCreate();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int newDate = MainActivity.getDate();
            if(date != newDate)
            {
                if(itemRepository != null)
                {
                    itemRepository.updateStepItem(detector.getStepCount(), date);
                }
                detector.setStepCount(0);
            }
            double accelX = event.values[0];
            double accelY = event.values[1];
            double accelZ = event.values[2];
            double atot = Math.sqrt(accelX*accelX + accelY*accelY + accelZ*accelZ);
            boolean success = detector.detect(atot, STEPTHRESH);
            if(success) {
                Log.d(TAG,detector.getStepCount()+"");
                Intent sendLevel = new Intent();
                sendLevel.setAction("GET_STEPS");
                sendBroadcast(sendLevel);

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static void setRepository(ItemRepository ir)
    {
        itemRepository = ir;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Step Detector";
            String description = "detecting steps in background";
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel(CHANNELID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
