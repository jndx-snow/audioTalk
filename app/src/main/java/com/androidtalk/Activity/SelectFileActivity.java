	package com.androidtalk.Activity;

	import android.app.Activity;
	import android.content.Context;
	import android.content.Intent;
	import android.os.Bundle;
	import android.os.Environment;
	import android.util.Log;
	import android.view.View;
	import android.widget.AdapterView;
	import android.widget.AdapterView.OnItemClickListener;
	import android.widget.ArrayAdapter;
	import android.widget.ListView;
	import android.widget.TextView;
	import android.widget.Toast;

	import com.androidtalk.R;

	import java.io.File;
	import java.util.ArrayList;

	public class SelectFileActivity extends Activity{

		public static final String TAG = SelectFileActivity.class.getSimpleName();

		private String currPath;
		public String selectedPath;

		public ListView listView;
		public TextView textView;

		private ArrayList<String > arrayList = new ArrayList<String>();
		public ArrayAdapter arrayAdapter;

		private Context mContext;
		private File file;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.select_file);
			mContext = this;

			listView = (ListView) this.findViewById(R.id.List_files);
	        textView = (TextView) this.findViewById(R.id.tv_title);

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// 获得SD卡路径
				File sdPath = Environment.getExternalStorageDirectory();
				String path=sdPath.getPath()+"/AndroidTalk";
				 file=new File(path);
				if(!file.exists()) {
					file.mkdir();
				}
				// 读取该目录下的.txt文件
				File[] files = file.listFiles();
				getFileName(files);
			}

			arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
			listView.setAdapter(arrayAdapter);

			listView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id)
				{
					// TODO Auto-generated method stub
					String selectItem = parent.getItemAtPosition(position).toString();

					Log.v(TAG,"当前选中的文件路径："+currPath+";"+selectItem);
					//	已选中的文件路径
					selectedPath=currPath+"/"+selectItem;
					//显示并播放
					Intent intent=new Intent();
					intent.setClass(SelectFileActivity.this,ReadActivity.class);
					intent.putExtra("filePath",selectedPath);
					startActivity(intent);
				}
			});
	    }

		/**
		 * 获取文件/AndroidTalk路径下的所有.txt文件，并添加至列表
		 * @param files
         */
		private void getFileName(File[] files) {
			// 先判断目录是否为空，否则会报空指针
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						getFileName(file.listFiles());
					} else {
						String fileName = file.getName();
						if (fileName.endsWith(".txt")) {
							arrayList.add(fileName);
						}
					}
				}
//				Log.v(TAG,"file.getPath():"+file.getPath());
				currPath=file.getPath();
			}else{
				Toast.makeText(mContext, "该目录下无.txt文件!", Toast.LENGTH_LONG).show();
			}
		}

	}
