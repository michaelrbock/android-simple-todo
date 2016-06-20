package com.michaelrbock.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by michaelbock on 6/19/16.
 */
public class TodoModelAdapter extends ArrayAdapter<TodoModel> {

    // Private lookup cache.
    private static class ViewHolder {
        TextView tvText;
        TextView tvDate;
        TextView tvPriority;
    }

    public TodoModelAdapter(Context context, ArrayList<TodoModel> todoModels) {
        super(context, 0, todoModels);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoModel todoModel = (TodoModel) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view.
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvText.setText(todoModel.text);
        Date date = new Date(todoModel.date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        viewHolder.tvDate.setText(sdf.format(date));
        viewHolder.tvPriority.setText(todoModel.priority);

        return convertView;
    }
}
