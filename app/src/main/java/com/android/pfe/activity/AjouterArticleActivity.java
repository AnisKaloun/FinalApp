package com.android.pfe.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pfe.R;
import com.android.pfe.other.Article;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class AjouterArticleActivity extends AppCompatActivity {


    private static final int PICK_PDF_REQUEST =70 ;
    Button addPDF ;
    TextView lien ;
    private FirebaseAuth auth;
    private Uri filePath;
    private TextView path;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_article);
         setTitle("Ajouter Article");
        auth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final ArrayList<String> arrayList;
        final ArrayAdapter<String> adapter;
        final EditText id_motcle ;
        final EditText id_titre ;

        id_titre = findViewById(R.id.id_titre);
        id_motcle = findViewById(R.id.id_motcle);
        Button ajouter = findViewById(R.id.ajouter);
        ListView listView = findViewById(R.id.ListV);
        path=findViewById(R.id.lien);

        //********************* ici le nom de l'auteur *************
        String [] item = {};
        arrayList = new ArrayList<>(Arrays.asList(item));
        adapter = new ArrayAdapter<String>(this,R.layout.list_motcle,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!id_motcle.getText().toString().isEmpty()) {
                    String newitem = id_motcle.getText().toString();
                    arrayList.add(newitem);
                    adapter.notifyDataSetChanged();
                    id_motcle.setText("");
                }
            }
        });


        addPDF= findViewById(R.id.addPDF);
        lien= findViewById(R.id.lien);

        addPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
            }
        });

        Button enregister = findViewById(R.id.id_enregistrer);

        enregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("AddArticle","je suis dans le bouton enregistrer");
                String emplacement ="Documents/";
                if(filePath!=null) {
                    Log.w("AddArticle","i am in the if");
                    emplacement += UUID.randomUUID().toString();
                    Log.w("AddArticle","the link"+emplacement);
                    StorageReference ref = storageReference.child(emplacement);
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.w("AddArticle","i add the file");

                            Log.w("filepath",""+downloadUrl.toString());
                            Article article = new Article();
                            String titrePDF = id_titre.getText().toString();
                            String Userid = auth.getCurrentUser().getUid().toString();
                            String author = auth.getCurrentUser().getDisplayName().toString();
                            String mot = "";
                            if (arrayList.isEmpty() == false) {

                                for (int i = 0; i < arrayList.size(); i++) {
                                    mot += arrayList.get(i) + " ";
                                }

                            }

                            Log.w("AjouterArticle","i'm here");
                            article.addArticle(Userid, author, titrePDF, mot,downloadUrl.toString());
                            finish();

                        }
                    })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot snapshot) {
                                    Toast.makeText(AjouterArticleActivity.this, "en cours "+snapshot.getBytesTransferred(), Toast.LENGTH_SHORT).show();
                                    Log.w("AjouterArticle","taille envoyé"+snapshot.getBytesTransferred());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(AjouterArticleActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                }
                else
                {

                    Toast.makeText(AjouterArticleActivity.this, "erreur d'ajout d'article", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            String mpath = filePath.getPath().toString();
            path.setText(mpath);

        }
    }

}
