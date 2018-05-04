package com.android.pfe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.android.pfe.other.User;
import com.google.firebase.auth.FirebaseAuth;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class AjouterArticleActivity extends AppCompatActivity {


    Button addPDF ;
    TextView lien ;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_article);
         setTitle("Ajouter Article");
        auth=FirebaseAuth.getInstance();
        final ArrayList<String> arrayList;
        final ArrayAdapter<String> adapter;
        final EditText id_motcle ;
        final EditText id_titre ;

        id_titre = findViewById(R.id.id_titre);
        id_motcle = findViewById(R.id.id_motcle);
        Button ajouter = findViewById(R.id.ajouter);
        ListView listView = findViewById(R.id.ListV);

        //********************* ici le nom de l'auteur *************
        String [] item = {};
        arrayList = new ArrayList<>(Arrays.asList(item));
        adapter = new ArrayAdapter<String>(this,R.layout.list_motcle,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newitem = id_motcle.getText().toString();
                arrayList.add(newitem);
                adapter.notifyDataSetChanged();
                id_motcle.setText("");
            }
        });

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }

        addPDF= findViewById(R.id.addPDF);
        lien= findViewById(R.id.lien);

        addPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(AjouterArticleActivity.this)
                        .withRequestCode(1000)
                        .withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
            }
        });

        Button enregister = findViewById(R.id.id_enregistrer);
        enregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = new Article();
                 String titrePDF = id_titre.getText().toString();
               String Userid=auth.getCurrentUser().getUid().toString();
               String author=auth.getCurrentUser().getDisplayName().toString();
                final HashMap<String, Integer> map = new HashMap<>();
                if(arrayList.isEmpty()==false) {

                    for (int i = 0; i < arrayList.size(); i++) {
                        map.put(arrayList.get(i),1);
                    }

                }
                User uti=new User();

               article.addArticle(Userid,author,titrePDF,map);
                uti.addMotcle(map,Userid);
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            lien.setText(filePath);
        }
    }
}
