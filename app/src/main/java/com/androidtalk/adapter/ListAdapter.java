package com.androidtalk.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidtalk.R;

import java.util.List;


/**
 * @author axue
 * @date 2016/5/15 12:50
 */
public class ListAdapter extends BaseAdapter {

    //得到一个LayoutInfalter对象用来导入布局
    public LayoutInflater mInflater;
    public List<String> content;

    /**构造函数*/
    public ListAdapter(Context context,List<String> content) {
        this.mInflater = LayoutInflater.from(context);
        this.content=content;
    }

    @Override
    public int getCount() {
        return content.size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String item=content.get(position);
        //观察convertView随ListView滚动情况
        Log.v("MyListViewBase", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.msg_list_items,null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.text = (TextView) convertView.findViewById(R.id.tv_content);
            holder.viewBack= (View) convertView.findViewById(R.id.view_back);

            //绑定ViewHolder对象
            convertView.setTag(holder);
        }else{
            //取出ViewHolder对象
            holder = (ViewHolder)convertView.getTag();
        }
        //设置TextView显示的内容
        holder.text.setText(item);
        holder.viewBack.setBackgroundResource(R.drawable.msgbox_rec);
        return convertView;
    }
    public  class ViewHolder{
        public TextView text;
        public View viewBack;
    }

}
