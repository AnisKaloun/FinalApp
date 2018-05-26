package com.android.pfe.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.pfe.R;

import java.util.ArrayList;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class AdaptorDesir extends ArrayAdapter<String> {

    private HomeAdaptor.onChecked listener;

    public AdaptorDesir(Context context, ArrayList<String> myList) {
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

                remove(getItem(position));

                //on supp les item de la bdd

            }
        });

        txtArticleDesir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // techniquement c'est ici qu on doit affichier les mot cle mais j ai pas su le faire ?? genre on fait la recursivité wela ...??



            }
        });



        return convertView;
    }




}
