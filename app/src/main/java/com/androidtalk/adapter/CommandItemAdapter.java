package com.androidtalk.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidtalk.entity.Command;
import com.androidtalk.R;

import java.util.List;

public class CommandItemAdapter extends ArrayAdapter<Command> {
    int resource;

    public CommandItemAdapter(Context _context, int _resource, List<Command> _items) {
        super(_context, _resource, _items);
        resource = _resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout todoView;
        Command item = getItem(position);
        String cmdName = item.getCmdName();
        String cmdCategory = item.getCmdCategory();
        String cmdRelation = item.getRelation();
        if (convertView == null) {
            todoView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, todoView, true);
        } else {
            todoView = (LinearLayout) convertView;
        }
        TextView nameView = (TextView) todoView.findViewById(R.id.tv_my_command);
        TextView categoryView = (TextView) todoView.findViewById(R.id.tv_my_name);
        TextView relationView = (TextView) todoView.findViewById(R.id.tv_my_relation);
        nameView.setText(cmdName);
        nameView.setTextColor(Color.BLACK);
        categoryView.setText(cmdCategory);
        categoryView.setTextColor(Color.BLACK);
        relationView.setText(cmdRelation);
        relationView.setTextColor(Color.BLACK);
        return todoView;
    }
}
