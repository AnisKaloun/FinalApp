package com.android.pfe.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.pfe.R;

import java.util.List;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class ArticleAdaptor extends ArrayAdapter<Article> {
    private LayoutInflater mInflater;
    List<Article> myList;


    public ArticleAdaptor(FragmentActivity activity, List<Article> myList) {
        super(activity,0,myList);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Article item = (Article) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_article, parent, false);
        }
        final TextView txt = (TextView)convertView.findViewById(R.id.titre);
        final TextView txt2 = (TextView)convertView.findViewById(R.id.auteur);
        final TextView txt3 = (TextView)convertView.findViewById(R.id.motcle);
        Button lire = (Button)convertView.findViewById(R.id.lirePDF);

        final String rec_titre = item.getTitre();
        final String rec_auteur = item.getAuteur();
        txt.setText(item.getTitre());
        txt2.setText(item.getAuteur());
        txt3.setText(item.getMot_cle());
        lire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), LecturePDF.class);
                myIntent.putExtra("titre",rec_titre+".pdf");
                myIntent.putExtra("auteur",rec_auteur);

                v.getContext().startActivity(myIntent);

            }
        });

        Button Supp=(Button)convertView.findViewById(R.id.SupprimerPDF);
        Supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_share);
                ListView lv = (ListView ) dialog.findViewById(R.id.Shareframe);
                dialog.setCancelable(true);
                dialog.setTitle("Ami");
                dialog.show();
            }
        });




        return convertView;
    }


}
