package com.nlscan.android.usbserialdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alan
 * @Company nlscan
 * @date 2018/7/1 19:55
 * @Description:
 */
public class Exhibition {
    private static final String TAG = "Exhibition_TAG";
    private static final String SET_DEMO_DATA = "SET_DEMO_DATA";
    private static final String EXTRA_DEMO_DATA = "EXTRA_DEMO_DATA";
    private static final String ACTION_SET_DEMO_DATA = "nlscan.action.ACTION_SET_DEMO_DATA";
    private static Map demoDataMap = new HashMap<String,DemoData>();

    /**
     * receiver for intent from nebconfig
     *@author alan
     *@time 2018/4/23 22:23
     */
    private static BroadcastReceiver demoDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "setDemoDataReceiver action：" + action);
            if (ACTION_SET_DEMO_DATA.equals(action)) {
                Bundle cfgData = intent.getBundleExtra(SET_DEMO_DATA);
                Log.d(TAG, "DemoData：" + cfgData.toString());
                String demoData = cfgData.getString(SET_DEMO_DATA, "");
                if (!TextUtils.isEmpty(demoData)) {
                    SharedPreferencesUtil.writeStrPrefNode(EXTRA_DEMO_DATA, demoData);
                    getDemoDataMap(demoDataMap);
                }
            }
        }
    };

    private static void getDemoDataMap(Map demoDataMap){
        String demoDataStr = SharedPreferencesUtil.readStrPrefNode(EXTRA_DEMO_DATA);
        if(!TextUtils.isEmpty(demoDataStr)){
            String[] demoDataArr = demoDataStr.split("\n");
            for(int i = 0;i<demoDataArr.length;i++){
                if(!TextUtils.isEmpty(demoDataArr[i])){
                    String[] demoDataObjArr = demoDataArr[i].split("-");
                    DemoData demoDataObj = new DemoData(demoDataObjArr[0],demoDataObjArr[1],demoDataObjArr[2]);
                    demoDataMap.put(demoDataObjArr[1],demoDataObj);
                }
            }
        }
    }

    public static void registDemoDataReceiver(Context context) {
        IntentFilter intFilter = new IntentFilter(ACTION_SET_DEMO_DATA);
        context.registerReceiver(demoDataReceiver, intFilter);
        Log.d(TAG, "registerReceiver：registtDemoDataReceiver");
    }

    public static void unregistDemoDataReceiver(Context context) {
        try {
            context.unregisterReceiver(demoDataReceiver);
            Log.d(TAG, "unregisterReceiver：registtDemoDataReceiver");
        } catch (Exception e) {
            Log.d(TAG, "UnregisterReceiver Exception(registtDemoDataReceiver)");
            e.printStackTrace();
        }
    }


    public static String addSpaceForStr(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
//                sb.append("0").append(str);
                 sb.append(str).append(" ");
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }


        /**
         * stub method for tokyo show
         *
         * @author alan
         * @time 2018/4/23 22:22
         */
    public static void displayShow(String code, String count, StringBuilder d1, StringBuilder d2, StringBuilder d3, StringBuilder d4, StringBuilder d5) {
        if (code == null) {
            return;
        } else {
            StringBuilder sb = new StringBuilder();
            if(demoDataMap.isEmpty()){
                getDemoDataMap(demoDataMap);
            }
            if(demoDataMap.containsKey(code)){
                DemoData demoDataObj = (DemoData)demoDataMap.get(code);
                d1.append(demoDataObj.getName()+"\n");
                d2.append(demoDataObj.getPrice()+"\n");
//                sb.append(addSpaceForStr(demoDataObj.getName(),91));
//                sb.append(addSpaceForStr(demoDataObj.getPrice(),81));

            } else {
                d1.append("Unknown"+"\n");
                d2.append("Unknown"+"\n");
//                sb.append(addSpaceForStr("Unknown",81));
//                sb.append(addSpaceForStr("Unknown",76));
            }
            d3.append(code+"\n");
//            sb.append(addSpaceForStr(code,13));
//            sb.append("                                                 ");
//            String tempVal = count;
//            int tempValInt = tempVal.length();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date day = new Date();
//            if (tempValInt == 1){
//                tempVal = "0"+tempVal;
//            }
            d4.append(count+"\n");
//            sb.append(addSpaceForStr(tempVal,84));
            d5.append(df.format(day)+"\n");
//            sb.append(df.format(day));
//            sb.append("\r\n");
//            return sb.toString();
        }
    }
}
