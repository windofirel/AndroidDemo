package priv.wind.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class MyService extends Service {
    private IBinder mBinder;

    private static final String TAG = "wind_demo";

    public MyService() {
        mBinder = new LocalBinder();
        Log.i(TAG, "MyService: new a MyService");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: service");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "onStart: service");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: service");
        String name = intent.getStringExtra("Name");
        Log.i(TAG, "His name is " + name);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: service");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: service");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: service");
        return super.onUnbind(intent);
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().toString();
    }

    public class LocalBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }
}
