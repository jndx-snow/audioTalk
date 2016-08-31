package com.androidtalk.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidtalk.R;
import com.androidtalk.util.MainItemsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    //上下文
    private Context mContext;
    //获取list列表
    public ListView lv_title;
    //获取适配器
    public MainItemsAdapter myAdapter;

    public ArrayList<Map> titleList = new ArrayList<Map>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mContext = this;

        init();
        lv_title = (ListView) findViewById(R.id.lv_title_list);
        myAdapter = new MainItemsAdapter(MainActivity.this, titleList);
        lv_title.setAdapter(myAdapter);

        lv_title.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                Toast.makeText(mContext, "选择==" + titleList.get(i).get("title"), Toast.LENGTH_SHORT).show();
                if (titleList.get(i).get("title").equals("命令映射")) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, CommandListActivity.class);
                    startActivity(intent);
                } else if (titleList.get(i).get("title").equals("语音播报")) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ReadActivity.class);
                    startActivity(intent);
                } else if (titleList.get(i).get("title").equals("语音识别")) {
                    //语音识别
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, IdentifyActivity.class);
                    startActivity(intent);
                } else if (titleList.get(i).get("title").equals("智能搜索")) {
                    //智能搜索
                    Intent intent = new Intent();
                    intent.putExtra("identify","1");
                    intent.setClass(MainActivity.this, IdentifyActivity.class);
                    startActivity(intent);
                } else if (titleList.get(i).get("title").equals("关于应用")) {
                    //帮助
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, HelpActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void init() {
        int[] imageId = new int[]{R.drawable.setting, R.drawable.read,
                R.drawable.communicate, R.drawable.search, R.drawable.help};
        String[] titles = new String[]{"命令映射", "语音播报", "语音识别", "智能搜索", "关于应用"};
        int[] imageArrow = new int[]{R.drawable.arrow};

        for (int i = 0; i < imageId.length; i++) {
            Map map = new HashMap();
            map.put("image", imageId[i]);
            map.put("title", titles[i]);
            map.put("arrow", imageArrow[0]);
            titleList.add(map);
        }
    }

}