package com.nlscan.android.usbserialdemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Alan
 * @Company nlscan
 * @date 2018/7/4 21:24
 * @Description:
 */

public class MainActivity extends AppCompatActivity {

    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private UsbService usbService;
    private TextView d1,d2,d3,d4,d5;
    private EditText editText2;
    private static final String TAG = "SerialDemo";
    //    private EditText editText;
    private MyHandler mHandler;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new MyHandler(this);
        d1 =  findViewById(R.id.d1);
        d2 =  findViewById(R.id.d2);
        d3 =  findViewById(R.id.d3);
        d4 =  findViewById(R.id.d4);
        d5 =  findViewById(R.id.d5);
        d1.setMovementMethod(ScrollingMovementMethod.getInstance());
        d2.setMovementMethod(ScrollingMovementMethod.getInstance());
        d3.setMovementMethod(ScrollingMovementMethod.getInstance());
        d4.setMovementMethod(ScrollingMovementMethod.getInstance());
        d5.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText2 = findViewById(R.id.editText2);
        Exhibition.registDemoDataReceiver(this);
        SharedPreferencesUtil.getInstance(getApplicationContext());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText2.getWindowToken(),0);
        editText2.requestFocus();


        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    if(!TextUtils.isEmpty(charSequence.toString())&&String.valueOf(charSequence.charAt(i)).equals("\n")){
                        String[] strArr = editText2.getText().toString().split("\n");
                        itemCount(strArr);
                    }
                } catch (Exception e) {
//                    String[] strArr = editText2.getText().toString().split("\n");
//                    countDisplay.setText(itemCount(strArr));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        Button sendButton = (Button) findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.setText("");
                d1.setText("");
                d2.setText("");
                d3.setText("");
                d4.setText("");
                d5.setText("");

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Exhibition.unregistDemoDataReceiver(this);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }


    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    Log.d(TAG,"scan data:"+data);
                    mActivity.get().editText2.append(data);
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    private void itemCount(String[] array) {

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            if (TextUtils.isEmpty(array[i])){
                continue;
            }
            Integer integer = map.get(array[i]);
            map.put(array[i], integer == null ? 1 : integer + 1);
            Log.d(TAG,"array[i]"+array[i]+"size:"+array.length+" i:"+i+"KEY:"+array[i]+"value:"+integer);
        }
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        StringBuilder sb5 = new StringBuilder();
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Map.Entry<String, Integer> entry : set) {
            System.out.println(entry.getKey() + "---" + entry.getValue());
            Exhibition.displayShow(entry.getKey().toString(),entry.getValue().toString(),sb1,sb2,sb3,sb4,sb5);

        }
        d1.setText(sb1.toString());
        d2.setText(sb2.toString());
        d3.setText(sb3.toString());
        d4.setText(sb4.toString());
        d5.setText(sb5.toString());
    }
}