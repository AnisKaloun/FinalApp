package com.android.pfe.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.pfe.R;

/**
 * Created by lety2018 on 05/04/2018.
 */

public class Modif_mot_de_passe extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_acceuil);
        setTitle("Modifier Mot De Passe");

       /* Button button7;
        button7 = (Button)findViewById(R.id.button5);

        button7.setOnClickListener(new View.OnClickListener() {

                                       public void onClick (View v){

                                           Intent ins = new Intent(com.android.pfe.other.Modif_mot_de_passe.this,Inscription.class);
                                           startActivity(ins);
                                       }
                                   }
        );*/

    }
}























        /* final ArrayList<String> arrayList;
         final ArrayAdapter<String> adapter;
         final EditText txtinput ;


        ListView listView = (ListView)findViewById(R.id.ListV);
        String [] item = {};
        arrayList = new ArrayList<>(Arrays.asList(item));
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.name,arrayList);
        listView.setAdapter(adapter);
        txtinput=(EditText)findViewById(R.id.txtinput);
        Button btadd = (Button)findViewById(R.id.btadd);
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newitem = txtinput.getText().toString();
                arrayList.add(newitem);
                adapter.notifyDataSetChanged();
            }
        });*/


