package com.android.pfe.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.pfe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

import java.util.ArrayList;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class AdaptorDesirMotcle extends ArrayAdapter<String> {


    private Query mDatabase;
    private FirebaseAuth auth;
    private onChecked listener;

    public AdaptorDesirMotcle(Context context, ArrayList<String> myList) {
        super(context,0,myList);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final String item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_article_desir, parent, false);
        }

        final TextView txtArticleDesir ;
        txtArticleDesir = convertView.findViewById(R.id.txtArticleDesir);
        txtArticleDesir.setText(item);

        Button supp =  convertView.findViewById(R.id.buttonSupp);
        supp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (listener != null) //call interface
                    listener.checkedListener(item);
                remove(getItem(position));

            }
        });



        return convertView;
    }

    public void setListener(onChecked listener) {
        this.listener = listener;
    }

    public interface onChecked {
        void checkedListener(String article);
    }


}
