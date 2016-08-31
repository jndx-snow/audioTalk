package com.androidtalk.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.androidtalk.R;
import com.androidtalk.entity.Command;
import com.androidtalk.util.DataHelper;

import java.util.ArrayList;

public class CommandAddActivity extends Activity implements OnClickListener {
    private String TAG = CommandAddActivity.class.getSimpleName();
    //获取文本
    private EditText et_command, et_name, et_relation;
    //按钮
    private ImageButton btn_save, btn_cancel;
    static ArrayList<String> res = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    private ListView listView;

    public String cmd_name, cmd_category, cmd_relation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_command_activity);

        btn_save = (ImageButton) findViewById(R.id.btn_add_command);
        btn_save.setOnClickListener(this);

        btn_cancel = (ImageButton) findViewById(R.id.btn_cancel_command);
        btn_cancel.setOnClickListener(this);

        et_command = (EditText) findViewById(R.id.et_cmd_name);
        et_name = (EditText) findViewById(R.id.et_name);
        et_name.setOnClickListener(this);
        et_relation = (EditText) findViewById(R.id.et_relation);

        res = DataHelper.getInstalledApps(this);
        listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, res);

        listView = (ListView) findViewById(R.id.lv_app_Name);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btn_save) {
            cmd_name = et_command.getText().toString();
            cmd_category = et_name.getText().toString();
            cmd_relation = et_relation.getText().toString();
//			Log.v(TAG,"cmd_name=="+cmd_name);
            if ((cmd_name.equals("")) || (cmd_category.equals("")) || (cmd_relation.equals(""))) {
                Toast.makeText(this, "内容不能为空，请输入!", Toast.LENGTH_SHORT).show();
            } else {
                Command cmd = DataHelper.getCommand(cmd_category, CommandAddActivity.this);

//                Log.v(TAG, "cmd==" + cmd.getCmdCategory());

                if (cmd.getCmdCategory() == null) {
                    DataHelper.addCommand(CommandAddActivity.this, cmd_name, cmd_category, cmd_relation);
                    Intent intent = new Intent();
                    this.setResult(RESULT_OK, intent);
                    finish();
                } else {
                    et_name.setText("");
                    et_relation.setText("");
                    Toast.makeText(this, "该映射已存在，不能重新添加!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (v == btn_cancel) {
            finish();
        } else if (v == et_name) {
            // TODO Auto-generated method stub
            listView.setAdapter(listAdapter);
            listView.setItemsCanFocus(true);
            listView.setScrollContainer(true);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.v(TAG, "选中的值为：" + res.get(i));
                    et_name.setText(res.get(i));
                    et_relation.setText(DataHelper.getPackageName(res.get(i)));
                }

            });
        }
    }
}