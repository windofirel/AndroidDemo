package priv.wind.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "wind_demo";

    private ServiceConnection mServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "onServiceConnected: connect");
                MyService.LocalBinder localBinder = ((MyService.LocalBinder) service);
                MyService myService = localBinder.getService();
                Log.i(TAG, "onServiceConnected: " + myService.getDate());
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "onServiceDisconnected: disconnect");
            }
        };
        Button btnBind = ((Button) findViewById(R.id.btn_start));
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                Log.i(TAG, "onClick: onbind service");
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        Button btnUnbind = ((Button) findViewById(R.id.btn_stop));
        btnUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: unbind service");
                unbindService(mServiceConnection);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}
