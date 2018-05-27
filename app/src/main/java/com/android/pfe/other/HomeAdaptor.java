package com.android.pfe.other;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pfe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by lety2018 on 20/04/2018.
 */

public class HomeAdaptor extends ArrayAdapter<Article>  {

    private static final String TAG ="Adaptor" ;
    public String recherche = "";
    ArrayList<Article> myList;
    private LayoutInflater mInflater;
    private onChecked listener;

    public HomeAdaptor(Context c, ArrayList<Article> list)
    {
        super(c,0,list);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Article item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_acceuil, parent, false);
        }
        final TextView txt = convertView.findViewById(R.id.titre);
        final TextView txt2 = convertView.findViewById(R.id.auteur);
        final TextView txt3 = convertView.findViewById(R.id.motcle);


        Button lire = convertView.findViewById(R.id.lirePDF);
        Button mPartager= convertView.findViewById(R.id.partagerPDF);
        Button mTelecharger=convertView.findViewById(R.id.telechargerPDF);


        RatingBar etoiles = convertView.findViewById(R.id.etoiles);


        // ************** elle est là la note !!

        double note = etoiles.getRating();

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
                myIntent.putExtra("link",item.getPdfUrl());
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
        mTelecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(item);
            }
        });




        return convertView;
    }
    public void setListener(onChecked listener) {
        this.listener = listener;
    }

    private void downloadFile(Article item) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.w("HomeAdaptor","le lien est"+item.getPdfUrl());
        StorageReference storageRef = storage.getReferenceFromUrl(item.getPdfUrl());


        File rootPath = new File(Environment.getExternalStorageDirectory(),"Article");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,item.getTitre()+".pdf");

        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(),"Fichier téléchargé", Toast.LENGTH_LONG).show();
                Log.e("firebase ","local file created" +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ","local file not created" +exception.toString());
            }
        });
    }
    public interface onChecked {
        void checkedListener(Article article);
    }

}