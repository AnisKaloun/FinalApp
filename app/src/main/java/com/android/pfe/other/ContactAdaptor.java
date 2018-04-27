package com.android.pfe.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.pfe.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class ContactAdaptor extends ArrayAdapter<User> {
    private LayoutInflater mInflater;
    List<User> myList;


    public ContactAdaptor(FragmentActivity activity, List<User> myList) {
        super(activity,0,myList);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        User item = (User) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_contact, parent, false);
        }
        TextView username = (TextView)convertView.findViewById(R.id.text_view_contact_username);
        TextView email=(TextView) convertView.findViewById(R.id.contact_email);
        username.setText(item.username);
        email.setText(item.email);


        return convertView;
    }


}
