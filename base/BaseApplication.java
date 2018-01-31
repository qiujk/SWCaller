package com.sn.swcaller.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 全局变量使用
 */

public class BaseApplication extends Application {
    //常量
    private static final String PARAM_SERVER_IP = "serverip";
    private static final String PARAM_SERVER_PORT = "serverport";
    private static final String PARAM_USER_NAME = "username";
    //对象
    private static BaseApplication instance;

    private SharedPreferences globalSp;
    private String mServerip;
    private String mServerport;
    private String mUsername;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        globalSp = PreferenceManager.getDefaultSharedPreferences(this);
        mServerip = globalSp.getString(PARAM_SERVER_IP,"192.168.69.43");//第二个为默认值
        mServerport = globalSp.getString(PARAM_SERVER_PORT,"8090");
        mUsername = globalSp.getString(PARAM_USER_NAME,"");
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    public String getServiceip() {
        return mServerip;
    }

    public void setServiceip(String serverip) {
        if (mServerip.equals(serverip))
            return;

        mServerip = serverip;
        globalSp.edit().putString(PARAM_SERVER_IP, serverip).apply();
    }

    public String getServiceport() {
        return mServerport;
    }

    public void setServiceport(String serverport) {
        if (mServerport.equals(serverport))
            return;

        mServerport = serverport;
        globalSp.edit().putString(PARAM_SERVER_PORT, serverport).apply();
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        if (mUsername.equals(username))
            return;

        mUsername = username;
        globalSp.edit().putString(PARAM_USER_NAME, username).apply();
    }
}
