package com.androidtalk.app;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
/**
 * @author axue
 * @date 2016/5/14 10:21
 */
public class AndroidTalkApp  extends Application {

    @Override
    public void onCreate() {
//        SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC+
        SpeechUtility.createUtility(AndroidTalkApp.this, SpeechConstant.APPID +"56838841");
//        +","+","+SpeechConstant.FORCE_LOGIN + "=true"
        super.onCreate();
    }

}

