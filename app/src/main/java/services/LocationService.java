package services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import model.Bus;
import model.GetDataInterface;
import model.Violation;
import tracking.GPSTracker;
import util.HelperMethods;

public class LocationService extends Service implements GetDataInterface {

    double lastLongitude = 0, lastLatitude = 0;
    String lastUpdateTime;
    double speed = 0;
    double maxSpeed = 0;
    int time = 10000;
    Bus currentBus = new Bus();

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HelperMethods.getDataService(LocationService.this, "Bus", HelperMethods.currentSupervisor.getBusName());

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
                            if (HelperMethods.isStarted) {
                                if (HelperMethods.currentSupervisor.getBusName() != null) {

                                    GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                                    if (gpsTracker.canGetLocation() && gpsTracker.getLongitude() != 0) {
                                        double distanceForSpeed = 0, distanceForViolation = 0;
                                        if (lastLatitude != 0) {
                                            distanceForSpeed = HelperMethods.calculateDistance(lastLatitude, lastLongitude, gpsTracker.getLatitude(), gpsTracker.getLongitude());
                                            distanceForViolation = HelperMethods.calculateDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), currentBus.getCenterLatitude(), currentBus.getCenterLongitude());

                                        }


                                        lastUpdateTime = Calendar.getInstance().getTime().toString();
                                        lastLatitude = gpsTracker.getLatitude();
                                        lastLongitude = gpsTracker.getLongitude();

                                        speed = distanceForSpeed / time;

                                        // 10 KM
                                        if (distanceForViolation > 10000 || speed > maxSpeed) {
                                            Violation currentViolation = new Violation();


                                            currentViolation.setBusName(currentBus.getName());
                                            currentViolation.setLatitude(gpsTracker.getLatitude());
                                            currentViolation.setLongitude(gpsTracker.getLongitude());
                                            currentViolation.setSpeed(speed);

                                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                            Date date = new Date();
                                            System.out.println(dateFormat.format(date));
                                            currentViolation.setTime(dateFormat.format(date));


                                            HelperMethods.pushInFireBaseService("Violations", currentViolation);


                                        }

                                        HelperMethods.pushInFireBaseService("Bus", HelperMethods.currentSupervisor.getBusName(), "currentLatitude", gpsTracker.getLatitude());
                                        HelperMethods.pushInFireBaseService("Bus", HelperMethods.currentSupervisor.getBusName(), "currentLongitude", gpsTracker.getLongitude());


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                stopSelf();
                                timer.cancel();
                            }


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
}
