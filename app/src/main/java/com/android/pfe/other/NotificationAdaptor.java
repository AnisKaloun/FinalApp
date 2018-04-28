package com.android.pfe.other;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.pfe.R;

import java.util.List;

/**
 * Created by SADA INFO on 27/04/2018.
 */

public class NotificationAdaptor extends ArrayAdapter<Message> {

    public NotificationAdaptor(FragmentActivity activity,List<Message> List) {
        super(activity,0,List);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Message item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_notification, parent, false);
        }
        TextView message = convertView.findViewById(R.id.ListMessage);
        message.setText(""+item.getUserUsername()+" vous a recommandé "+item.getArticleId());
        return convertView;
    }
}
