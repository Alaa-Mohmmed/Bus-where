package services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import com.aou.buswhere.R;
import com.aou.buswhere.SplashScreen;
import com.google.firebase.database.DataSnapshot;

import java.util.Timer;
import java.util.TimerTask;

import model.Bus;
import model.GetDataInterface;
import tracking.GPSTracker;
import util.HelperMethods;

public class NotificationService extends Service implements GetDataInterface {

    String lastUpdateTime;
    double speed = 0;
    double maxSpeed = -1;
    int time = 10000;
    Bus currentBus = new Bus();

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HelperMethods.getDataServiceBusLocation(NotificationService.this, "Bus", HelperMethods.currentStudent.getBusName());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Handler handler = new Handler();
        final Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            Toast.makeText(NotificationService.this, "distance = 000", Toast.LENGTH_SHORT).show();

//                            if (HelperMethods.notificationBoolean) {
                            if (HelperMethods.currentStudent.getBusName() != null) {

                                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                                if (gpsTracker.canGetLocation() && gpsTracker.getLongitude() != 0) {

                                    double distance = HelperMethods.calculateDistance(currentBus.getCurrentLatitude(), currentBus.getCurrentLongitude(), gpsTracker.getLatitude(), gpsTracker.getLongitude());
                                    Toast.makeText(NotificationService.this, "distance = " + distance, Toast.LENGTH_SHORT).show();

                                    // 3 KM
                                    if (distance < 3) {
                                        createNotification("The bus is very near to you");
//                                        createNotification("Distance between " + currentBus.getName() + " and you is " + distance + " M");

                                    }


                                } else {
//                                        Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                                }
                            }
//                            } else {
//                                stopSelf();
//                                timer.cancel();
//                            }


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, time);

        return START_STICKY;
    }

    @Override
    public void updateUI(DataSnapshot data) {
        currentBus = data.getValue(Bus.class);


    }

    public void createNotification(String msg) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, SplashScreen.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle(msg)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(soundUri).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
//        noti.flags |= Notification.FLAG_NO_CLEAR;

//        noti.defaults |= Notification
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//         Vibrate for 500 milliseconds
        v.vibrate(500);
        notificationManager.notify(0, noti);

    }

}

