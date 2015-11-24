package com.android.md.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 首选项管理
 * Created by mingdasen on 2015/11/23.
 */
public class SharePreferenceUtil {
    private SharedPreferences mSharePreferences;
    private static SharedPreferences.Editor editor;
    public SharePreferenceUtil(Context context,String name){
        mSharePreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        editor = mSharePreferences.edit();
    }

    private String SHARED_KEY_NOTIFY = "share_key_notify";
    private String SHARED_KEY_VOICE = "shared_key_sound";
    private String SHARED_KEY_VIBRATE = "shared_key_vibrate";

    //是否允许推送通知
    public boolean isAllowPushNotify(){
        return mSharePreferences.getBoolean(SHARED_KEY_NOTIFY,true);
    }

    public void setPushNotifyEnable(boolean isChecked){
        editor.putBoolean(SHARED_KEY_NOTIFY,isChecked);
        editor.commit();
    }

    //允许声音
    public boolean isAllowVoice(){
        return mSharePreferences.getBoolean(SHARED_KEY_VOICE,true);
    }

    public void setAllowVoiceEnble(boolean isChecked){
        editor.putBoolean(SHARED_KEY_VOICE,isChecked);
        editor.commit();
    }

    //允许震动
    public boolean isAllowVibrate(){
        return mSharePreferences.getBoolean(SHARED_KEY_VIBRATE,true);
    }

    public void setAllowVibrateEnable(boolean isChecked){
        editor.putBoolean(SHARED_KEY_VIBRATE,isChecked);
        editor.commit();
    }
}
