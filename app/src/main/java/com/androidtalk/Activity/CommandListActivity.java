package com.androidtalk.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtalk.entity.Command;
import com.androidtalk.R;
import com.androidtalk.util.CommandItemAdapter;
import com.androidtalk.util.DataHelper;

import java.util.ArrayList;

public class CommandListActivity extends Activity implements View.OnClickListener {

    public String TAG = CommandListActivity.class.getSimpleName();
    public ListView lv_command;
    public TextView tv_title;
    public ImageButton btn_add;

    private CommandItemAdapter adapter;
    private ArrayList<Command> arrayList = new ArrayList<Command>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cmd_list);

        tv_title = (TextView) findViewById(R.id.tv_title_command);
        btn_add = (ImageButton) findViewById(R.id.btn_add_command);
        btn_add.setOnClickListener(this);

        lv_command = (ListView) findViewById(R.id.lv_comand_list);
        try {
            adapter = new CommandItemAdapter(CommandListActivity.this, R.layout.cmd_list_item, arrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lv_command.setAdapter(adapter);
        lv_command.setItemsCanFocus(true);
        lv_command.invalidate();
        getArrayList();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_add_command:
                Intent start_intent = new Intent();
                start_intent.setClass(CommandListActivity.this, CommandAddActivity.class);
                startActivityForResult(start_intent, 1);
                break;
            case R.id.lv_comand_list:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (arrayList != null) {
                    arrayList.removeAll(arrayList);
                }
                getArrayList();
                break;
        }
    }

    private void getArrayList() {
        Cursor cur = DataHelper.getCommandList(this);
        cur.moveToFirst();
        Command item;
        if (arrayList != null) {
            arrayList.removeAll(arrayList);
        }
        while (!cur.isAfterLast()) {
            item = new Command(cur.getString(cur.getColumnIndex("id")),
                    cur.getString(cur.getColumnIndex("cmd_name")),
                    cur.getString(cur.getColumnIndex("cmd_category")),
                    cur.getString(cur.getColumnIndex("relation")));

            arrayList.add(item);
            cur.moveToNext();
            Log.v(TAG, "命令列表：" + item.toString());
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        arrayList.clear();
    }
}
