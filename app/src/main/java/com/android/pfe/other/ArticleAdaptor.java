package com.android.pfe.other;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.pfe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class ArticleAdaptor extends ArrayAdapter<Article> {
    List<Article> myList;
    private LayoutInflater mInflater;
    private Query mDatabase;
    private onChecked listener;


    public ArticleAdaptor(FragmentActivity activity, List<Article> myList) {
        super(activity,0,myList);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Article item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_article, parent, false);
        }
        final TextView txt = convertView.findViewById(R.id.titre);
        final TextView txt2 = convertView.findViewById(R.id.auteur);
        final TextView txt3 = convertView.findViewById(R.id.motcle);
        Button lire = convertView.findViewById(R.id.lirePDF);

        final String rec_titre = item.getTitre();
        final String rec_auteur = item.getAuteur();

        txt.setText(item.getTitre());
        txt2.setText(item.getAuteur());
        txt3.setText(item.getMot_cle());
        Log.w("mot clé recup",""+item.getMot_cle());
        lire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), LecturePDF.class);
                myIntent.putExtra("titre",rec_titre+".pdf");
                myIntent.putExtra("auteur",rec_auteur);

                v.getContext().startActivity(myIntent);

            }
        });

        Button partage=convertView.findViewById(R.id.partagerPDF);
        partage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) //call interface
                    listener.checkedListener(item);
            }
        });


        Button Supp= convertView.findViewById(R.id.SupprimerPDF);
        Supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase= FirebaseDatabase.getInstance().getReference("Article").orderByChild("articleId")
                        .equalTo(item.getArticleId());
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

    public void setListener(onChecked listener) {
        this.listener = listener;
    }
    public interface onChecked {
        void checkedListener(Article article);
    }
}
