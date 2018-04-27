package com.android.pfe.other;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.pfe.R;
import com.android.pfe.fragment.FragmentDialog;
import com.android.pfe.fragment.HomeFragment;


import java.util.ArrayList;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class HomeAdaptor extends ArrayAdapter<Article>  {

    private static final String TAG ="Adaptor" ;
    public String recherche = "";
    private LayoutInflater mInflater;
    ArrayList<Article> myList;
    private onChecked listener;

    public HomeAdaptor(Context c, ArrayList<Article> list)
    {
        super(c,0,list);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Article item = (Article) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_acceuil, parent, false);
        }
        final TextView txt = (TextView)convertView.findViewById(R.id.titre);
        final TextView txt2 = (TextView)convertView.findViewById(R.id.auteur);
        final TextView txt3 = (TextView)convertView.findViewById(R.id.motcle);
        Button lire = (Button)convertView.findViewById(R.id.lirePDF);
        Button mPartager=(Button)convertView.findViewById(R.id.partagerPDF);

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
        mPartager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) //call interface
                    listener.checkedListener(item);

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