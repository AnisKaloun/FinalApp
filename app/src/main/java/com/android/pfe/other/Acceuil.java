package com.android.pfe.other;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.android.pfe.R;

public class Acceuil extends Activity {

    private ListView listACTU;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_acceuil);

        //Récupération de la listview créée dans le fichier main.xml
        listACTU = (ListView) findViewById(R.id.ListAcceil);

        //Création de la ArrayList qui nous permettra de remplir la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        //Création d'une HashMap pour insérer les informations du premier item de notre listView
        map = new HashMap<String, String>();

        //on insère un élément titre que l'on récupérera dans le textView titre créé dans le fichier affichageitem.xml
        map.put("titre", "Titre: Pdf");

        //on insère un élément description que l'on récupérera dans le textView description créé dans le fichier affichageitem.xml
        map.put("auteur", "Auteur:");

        //********************************************
        map.put("motcle", "Mots clé:");


        //on insère la référence à l'image (converti en String car normalement c'est un int) que l'on récupérera dans l'imageView créé dans le fichier affichageitem.xml
        map.put("img", String.valueOf(R.drawable.pdf));

        //enfin on ajoute cette hashMap dans la arrayList
        listItem.add(map);

        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView


        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.list_acceuil,
                new String[]{"img", "titre", "auteur", "motcle"}, new int[]{R.id.img, R.id.titre, R.id.auteur, R.id.motcle});

        //On attribue à notre listView l'adapter que l'on vient de créer
        listACTU.setAdapter(mSchedule);

        //Enfin on met un écouteur d'évènement sur notre listView
        listACTU.setOnItemClickListener(new OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //on récupère la HashMap contenant les infos de notre item (titre, description, img)
                HashMap<String, String> map = (HashMap<String, String>) listACTU.getItemAtPosition(position);
                //on créé une boite de dialogue
                AlertDialog.Builder adb = new AlertDialog.Builder(Acceuil.this);
                //on attribue un titre à notre boite de dialogue
                adb.setTitle("Sélection Item");
                //on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
                adb.setMessage("Votre choix : " + map.get("titre"));
                //on indique que l'on veut le bouton ok à notre boite de dialogue
                adb.setPositiveButton("Ok", null);
                //on affiche la boite de dialogue
                adb.show();
            }
        });

    }
}
