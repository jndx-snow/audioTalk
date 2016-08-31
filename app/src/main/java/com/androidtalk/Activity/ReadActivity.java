package com.androidtalk.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidtalk.R;
import com.androidtalk.util.ApkInstaller;
import com.androidtalk.util.TextOperation;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class ReadActivity extends Activity {

    private static final String TAG = ReadActivity.class.getSimpleName();

    public Button btn_read_text, btn_read_File;
    private EditText et_input;
    public Intent intent;

    public Context mContext;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 语记安装助手类
    ApkInstaller mInstaller;
    private TextOperation textOperation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_activity);

        mContext = this;
        et_input = (EditText) findViewById(R.id.et_input);
        textOperation=new TextOperation();
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(ReadActivity.this, mTtsInitListener);
        mInstaller = new ApkInstaller(ReadActivity.this);
        //获取已选中的文件路径
        intent = getIntent();
        if (intent.getExtras() != null) {
            String filePath = intent.getStringExtra("filePath");
            et_input.setText(textOperation.ReadTxtFile(filePath));
        }

        btn_read_text = (Button) findViewById(R.id.btn_read_text);
        btn_read_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String text = et_input.getText().toString();
                // 设置参数
                setParam();
                if (text.length() > 0) {
                    int code = mTts.startSpeaking(text, mTtsListener);
                    if (code != ErrorCode.SUCCESS) {
                        if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                            //未安装则跳转到提示安装页面
                        Toast.makeText(mContext,"未安装"+code,Toast.LENGTH_SHORT).show();
                            mInstaller.install();
                        } else {
                            Toast.makeText(mContext, "语音合成失败,错误码: " + code, Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(mContext, "输入的文本为空，请输入内容！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //选择待读取文件
        btn_read_File = (Button) findViewById(R.id.btn_readFile);
        btn_read_File.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent file_intent = new Intent();
                file_intent.setClass(ReadActivity.this, SelectFileActivity.class);
                startActivity(file_intent);
            }
        });
    }

    /**
     * 初始化监听
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(mContext, "初始化失败,错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {}

        @Override
        public void onSpeakPaused() {}

        @Override
        public void onSpeakResumed() {}

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {}

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                et_input.setText("");
            } else if (error != null) {
                Toast.makeText(mContext, error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    /**
     * 设置播放参数
     */
    public void setParam() {
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
//        mTts.setParameter(SpeechConstant.EMOT, "0");
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, String.valueOf(50));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, String.valueOf(50));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, String.valueOf(100));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, String.valueOf(3));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/AndroidTalk.pcm");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
    }

}