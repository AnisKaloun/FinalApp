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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class AdaptorDesir extends ArrayAdapter<String> {


    private Query mDatabase;
    private FirebaseAuth auth;

    public AdaptorDesir(Context context, ArrayList<String> myList) {
        super(context,0,myList);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

         auth=FirebaseAuth.getInstance();
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

                remove(getItem(position));

                mDatabase= FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid().toString()).child("ArticleDesire").orderByChild("titre")
                        .equalTo(item);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });




        return convertView;
    }


}
