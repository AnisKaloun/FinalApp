package com.android.pfe.other;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.pfe.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by lety2018 on 05/04/2018.
 */

public class MAJ_mot_cle extends Activity {
    private int cpt=1;
    private ArrayList articleList;
    private ArrayAdapter<String> adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modif_mot_cle);

        final ArrayList<String> arrayList;

        final EditText txtinput ;
        articleList=new ArrayList<Article>();
        ListView listViewMotCle = findViewById(R.id.ListVMotcle);
        ListView listViewArticle=findViewById(R.id.ListVArticle);
        String [] item = {};
        arrayList = new ArrayList<>(Arrays.asList(item));
        adapter = new ArrayAdapter<String>(this,R.layout.list_motcle,R.id.txtitem,arrayList);


        listViewMotCle.setAdapter(adapter);
        txtinput= findViewById(R.id.txtinput);
        Button btadd = findViewById(R.id.ajouterMcl);
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newitem = txtinput.getText().toString();
                if (newitem.isEmpty() == false) {
                    arrayList.add(newitem);
                    adapter.notifyDataSetChanged();
                    txtinput.setText("");
                }
                else Toast.makeText(getApplicationContext(), "Entrer un mot clé", Toast.LENGTH_SHORT).show();
            }
        });

        String [] itemArticle = {};
        final ArrayList<String> arrayListArticle = new ArrayList<>(Arrays.asList(itemArticle));
        final ArrayAdapter<String> adapterArticle = new ArrayAdapter<String>(this, R.layout.list_motcle, R.id.txtitem, arrayListArticle);
        listViewArticle.setAdapter(adapterArticle);
        Button btaddArticle=findViewById(R.id.btaddArticle);

        btaddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Entrer des mot clé d'abord", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String txt="Article"+ cpt+"";
                    arrayListArticle.add(txt);
                    adapterArticle.notifyDataSetChanged();
                    cpt++;
                    String mot="";
                    if(arrayList.isEmpty()==false) {

                        for (int i = 0; i < arrayList.size(); i++) {
                            mot+=arrayList.get(i)+" ";
                        }

                    }
                    Article article=new Article(mot,txt);
                    articleList.add(article);
                    arrayList.clear();
                    adapter.notifyDataSetChanged();


                }
            }
        });


    }
}

