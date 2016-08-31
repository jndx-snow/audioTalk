package com.androidtalk.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidtalk.R;
import com.androidtalk.entity.Command;
import com.androidtalk.util.DataHelper;
import com.androidtalk.util.JsonParser;
import com.androidtalk.util.ListAdapter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author axue
 * @date 2016/5/14 18:38
 */
public class IdentifyActivity extends Activity implements OnClickListener {
    private static final String TAG = IdentifyActivity.class.getSimpleName();

    private RelativeLayout rl_bottom;
    private RelativeLayout speak_bottom;
    private ListView listView;
    private ListAdapter mAdapter;

    private ImageButton voiceButton;
    private ImageButton imageButton;
    private ImageButton btnSend;
    private EditText inputMessage;
    // 语音听写对象
    public SpeechRecognizer mIat;

    private Context mContext;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private List<String> listContent = new LinkedList<String>();
    int ret = 0; // 函数调用返回值
    public static String speakContent = "";
    public Toast mToast;

    public   String  identify="";
    private Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idetify_activity);

        myIntent=getIntent();
        mContext = this;
        identify=myIntent.getStringExtra("identify");
        Log.v(TAG,"identify=="+identify);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
//        mToast=new Toast(mContext);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        speak_bottom = (RelativeLayout) findViewById(R.id.speak_bottom);

        voiceButton=(ImageButton)findViewById(R.id.voice_input);
        voiceButton.setOnClickListener(this);

        imageButton = (ImageButton) findViewById(R.id.speak_input);
        imageButton.setOnClickListener(this);

        btnSend = (ImageButton) findViewById(R.id.btn_msg_send);
        btnSend.setOnClickListener(this);

        inputMessage = (EditText) findViewById(R.id.et_message);

        listView = (ListView) findViewById(R.id.list);
        //得到一个ListAdapter对象
        mAdapter = new ListAdapter(IdentifyActivity.this, listContent);
        //为ListView绑定Adapter
        listView.setAdapter(mAdapter);
        // 初始化识别对象
        mIat = SpeechRecognizer.createRecognizer(IdentifyActivity.this, mInitListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.speak_input:
                rl_bottom.setVisibility(View.GONE);
                speak_bottom.setVisibility(View.VISIBLE);
                showTip("点击按钮，松开后开始说话!");
                break;
            case R.id.voice_input:
                setParam();
                // 不显示听写对话框
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    showTip("听写失败,错误码：" + String.valueOf(ret));
                } else {
                    showTip(getString(R.string.text_begin));
                }
                break;
            case R.id.btn_msg_send:
                if(inputMessage.getText().length()>0){
                    new Thread(new Runnable() {
                        public void run() {
                            Log.v(TAG, "speakContent=" + speakContent);
                            Message message = new Message();
                            message.what = 12;
                            handler.sendMessage(message);
                        }
                    }).start();
                }else{
                    showTip("输入内容为空，请输入!");
                    inputMessage.setFocusable(true);
                }
                break;
        }
    }

    /**
     * 设置参数
     */
    public void setParam() {
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            showTip("结束说话");}

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            //处理结果
            printResult(results);
            if (isLast) {
                // TODO 最后的结果
                new Thread(new Runnable() {
                    public void run() {
                        Log.v(TAG, "speakContent=" + speakContent);
                        Message message = new Message();
                        message.what = 11;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        }

        @Override
        public void onVolumeChanged(int volume) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}
    };
    /**
     * 听写UI监听器
     */
    RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        public void onEnd(SpeechError error) {
            listContent.add(inputMessage.getText().toString());
            Log.i(TAG, "识别完成:" + inputMessage.getText().toString());
        }

        @Override
        public void onError(SpeechError arg0) {}
    };

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(mContext, "初始化失败,错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 处理识别的消息，并显示
     *
     * @param results
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        speakContent = resultBuffer.toString();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    listContent.add(speakContent);
                    Log.v(TAG, "输入的语音为==" + speakContent);
                    mAdapter.notifyDataSetChanged();
                    rl_bottom.setVisibility(View.VISIBLE);
                    speak_bottom.setVisibility(View.GONE);
                    Log.v(TAG,"是否为：："+identify);
                    if (identify!=null){
                        if(speakContent.length()>0) {
                          String[] ss=speakContent.split("[。]");
                            Log.v(TAG,"ss=="+ss.length+ss[0]);

                            Command cmd = DataHelper.getCommand(ss[0], IdentifyActivity.this);

                            Log.v(TAG, "cmd==" + cmd.getRelation());
                            if (cmd.getRelation()!= null) {
                                startAPP(cmd.getRelation());
                            }else{
                                showTip("未设置打开该应用的命令行!");
                            }
                        }
                    }else{
                        break;
                    }
                    break;
                case 12:
                    String txt=inputMessage.getText().toString();
                    listContent.add(inputMessage.getText().toString());
                    Log.v(TAG, "文字输入的文本为==" + inputMessage.getText().toString());
                    mAdapter.notifyDataSetChanged();
                    inputMessage.setText("");
                    if (identify!=null){
                        if(txt.length()>0) {
                            String[] ss=txt.split("[。]");
                            Log.v(TAG,"ss=="+ss.length+ss[0]);

                            Command cmd = DataHelper.getCommand(ss[0], IdentifyActivity.this);

                            Log.v(TAG, "cmd==" + cmd.getRelation());
                            if (cmd.getRelation()!= null) {
                                startAPP(cmd.getRelation());
                            }else{
                                showTip("未设置打开该应用的命令行!");
                            }
                        }
                    }
                    break;
            }
        }
    };

    public void startAPP(String appPackageName){
        try{
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * toast消息提示
     * @param str
     */
    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        listContent.clear();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
