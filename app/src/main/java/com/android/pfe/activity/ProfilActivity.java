package com.android.pfe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;


import com.android.pfe.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfilActivity extends AppCompatActivity implements OnClickListener {
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
   // private EditText txtinput ;

    private Button modifPseudo,modifEmail,modifMdp,MajListe,enregModif;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);


      //  setTitle("Profil");


        //**********ici on vas utiliser les bouttons du profil ^pour les modification****
        modifPseudo = (Button)findViewById(R.id.modifPseudo);
        modifEmail = (Button)findViewById(R.id.modifEmail);
        modifMdp = (Button)findViewById(R.id.modifMdp);
        MajListe = (Button)findViewById(R.id.MajListe);
        enregModif = (Button)findViewById(R.id.enregModif);

        modifPseudo.setOnClickListener(new OnClickListener() {

            public void onClick (View v){

                Intent mod_psd = new Intent(ProfilActivity.this,com.android.pfe.other.Modif_pseudo.class);
                startActivity(mod_psd);
            }
        }
        );

        modifEmail.setOnClickListener(new OnClickListener() {

           public void onClick (View v){

               Intent mod_email = new Intent(ProfilActivity.this,com.android.pfe.other.Modif_email.class);
               startActivity(mod_email);
           }
        }
        );
        modifMdp.setOnClickListener(new OnClickListener() {

           public void onClick (View v){

               Intent mod_mdp = new Intent(ProfilActivity.this,com.android.pfe.other.Modif_mot_de_passe.class);
               startActivity(mod_mdp);
           }
        }
        );
        MajListe.setOnClickListener(new OnClickListener() {

           public void onClick (View v){

               Intent maj_mc = new Intent(ProfilActivity.this,com.android.pfe.other.MAJ_mot_cle.class);
               startActivity(maj_mc);
           }
        }
        );
        enregModif.setOnClickListener(new OnClickListener() {

            public void onClick (View v){

                Intent insc = new Intent(ProfilActivity.this,MenuActivity.class);
                startActivity(insc);
            }
        }
        );


       /* enregModif.setOnClickListener(new View.OnClickListener() {

           public void onClick (View v){

               Intent mod_email = new Intent(com.android.pfe.activity.ProfilActivity.this,com.android.pfe.other.Modif_email.class);
           }
        }
        );*/




        // *******gerer la liste view ****
        ListView listView = (ListView)findViewById(R.id.ListV);
        String [] item = {};
        arrayList = new ArrayList<>(Arrays.asList(item));
        adapter = new ArrayAdapter<String>(this,R.layout.list_motcle,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);
        /*txtinput=(EditText)findViewById(R.id.txtinput);
        Button btadd = (Button)findViewById(R.id.btadd);
        btadd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String newitem = txtinput.getText().toString();
                arrayList.add(newitem);
                adapter.notifyDataSetChanged();
            }
        });*/



    }

    @Override
    public void onClick(View v) {

    }
}


