package com.android.pfe.other;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.pfe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class DialogAdaptor extends ArrayAdapter<User> {
    private LayoutInflater mInflater;
    List<User> myList;
    private FirebaseAuth auth,admin;
    private DatabaseReference mDatabase;
  //  private onClicked listener;

    public DialogAdaptor(FragmentActivity activity, List<User> myList) {
        super(activity,0,myList);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final User item = (User) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_partage, parent, false);
        }
       auth=FirebaseAuth.getInstance();


        TextView username = (TextView)convertView.findViewById(R.id.text_view_contact_username);
        TextView email=(TextView) convertView.findViewById(R.id.contact_email);
        username.setText(item.username);
        email.setText(item.email);
       /*convertView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (listener!=null)
               {
                   Log.w("Adaptor","i am clicking on item");
                   listener.checkedListener(item);
               }


           }
       });*/

        return convertView;
    }
/*
    public void setListener(onClicked listener) {
        this.listener = listener;
    }
    public interface onClicked {
        void checkedListener(User user);
    }
*/




}
