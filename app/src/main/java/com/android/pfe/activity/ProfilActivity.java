package com.android.pfe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.pfe.R;
import com.android.pfe.other.AdaptorDesir;
import com.android.pfe.other.AdaptorDesirMotcle;
import com.android.pfe.other.Article;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfilActivity extends AppCompatActivity{
    private ArrayList<String> arrayList1;
    private ArrayList<String> arrayList2;

    private TextView txtemail,txtpseudo ;

    private Button modifPseudo,modifEmail,modifMdp,MajListe,enregModif;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ListView listViewMotCle,listViewArticle;
    private ArrayList<String> article;
    private AdaptorDesir adapter2;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            article.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article arti = snapshot.getValue(Article.class);
                    if(arti!=null) {
                        article.add(arti.getTitre());
                    }
                }

                adapter2.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private AdaptorDesirMotcle adapter1;
    private ArrayList<String> motcle;
    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            motcle.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article arti = snapshot.getValue(Article.class);
                    if(arti!=null) {

                        String mot=arti.getMot_cle();
                        String[] splited = mot.split("\\s+");
                        for(int i=0;i<splited.length;i++)
                        {
                            Log.w("Motcle"," "+splited[i]);
                            motcle.add(splited[i]);
                        }

                    }
                }

                adapter1.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private Query mDatabase2;
    private String CurrentTitle;
    private Query mdata;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        auth = FirebaseAuth.getInstance();

       setTitle("Profil");

         CurrentTitle=new String();
        //**********ici on vas utiliser les bouttons du profil ^pour les modification****
        modifPseudo = findViewById(R.id.modifPseudo);
        modifEmail = findViewById(R.id.modifEmail);
        modifMdp = findViewById(R.id.modifMdp);
        MajListe = findViewById(R.id.MajListe);
        txtemail=findViewById(R.id.txtEmail);
        txtpseudo=findViewById(R.id.txtPseudo);
        enregModif = findViewById(R.id.enregModif);
        listViewMotCle = findViewById(R.id.ListVMotcle);
        listViewArticle = findViewById(R.id.ListVArticle);
        txtemail.setText(auth.getCurrentUser().getEmail());
        txtpseudo.setText(auth.getCurrentUser().getDisplayName());
        mDatabase= FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid().toString())
                .child("ArticleDesire");
        mDatabase.addValueEventListener(valueEventListener);

         motcle = new ArrayList<>();
        article = new ArrayList<>();
        adapter1 = new AdaptorDesirMotcle(getApplicationContext(),motcle);
        listViewMotCle.setAdapter(adapter1);

        adapter2 = new AdaptorDesir(getApplicationContext(),article);
        listViewArticle.setAdapter(adapter2);


        adapter1.setListener(new AdaptorDesirMotcle.onChecked() {
            @Override
            public void checkedListener(final String article) {
                  if(CurrentTitle!=null)
                  {
                      mdata= FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid().toString())
                              .child("ArticleDesire").orderByChild("titre").equalTo(CurrentTitle);
                      mdata.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {

                              if (dataSnapshot.exists()) {
                                  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                      Article arti = snapshot.getValue(Article.class);
                                      if(arti!=null) {
                                          String mot=arti.getMot_cle();
                                          mot.replace(article+" ","");
                                          Log.w("ReplaceMot","mot remplacé"+mot);
                                          arti.setMot_cle(mot);
                                         snapshot.getRef().setValue(arti);
                                      }

                                  }


                              }
                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {

                          }
                      });

                  }

            }
        });


        listViewArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String titre= (String)parent.getItemAtPosition(position);
                CurrentTitle=titre;
                motcle.clear();
                mDatabase2= FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid().toString())
                        .child("ArticleDesire").orderByChild("titre").equalTo(titre);
                mDatabase2.addValueEventListener(valueEventListener2);
            }
        });









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






    }
}


