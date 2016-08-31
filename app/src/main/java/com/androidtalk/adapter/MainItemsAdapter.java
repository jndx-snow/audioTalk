package com.androidtalk.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtalk.R;

import java.util.List;
import java.util.Map;

/**
 * @author axue
 * @date 2016/5/17 13:39
 */
public class MainItemsAdapter extends BaseAdapter {

    //得到一个LayoutInfalter对象用来导入布局
    public LayoutInflater mInflater;
    public List<Map> content;
    /**构造函数*/
    public MainItemsAdapter(Context context, List<Map> content) {
        this.mInflater = LayoutInflater.from(context);
        this.content=content;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int i) {
        return content.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        Map map=content.get(i);

        if (view == null) {
            view = mInflater.inflate(R.layout.main_list_item,null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.image = (ImageView) view.findViewById(R.id.btn_image);
            holder.title= (TextView) view.findViewById(R.id.tv_title);
            holder.arrow = (ImageView) view.findViewById(R.id.btn_arrow);

            //绑定ViewHolder对象
            view.setTag(holder);
        }else{
            //取出ViewHolder对象
            holder = (ViewHolder)view.getTag();
        }

        //设置TextView显示的内容
        holder.image.setImageResource((Integer) map.get("image"));
        holder.title.setText((String)map.get("title"));
        holder.arrow.setImageResource((Integer) map.get("arrow"));
        return view;
    }

    public class ViewHolder{
        public ImageView image;
        public TextView title;
        public ImageView arrow;
    }
}
