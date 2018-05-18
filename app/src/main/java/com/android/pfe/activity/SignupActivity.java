package com.android.pfe.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.android.pfe.other.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Keep
public class SignupActivity extends AppCompatActivity {
    private static final String TAG ="SignupActivity" ;
    private final int PICK_IMAGE_REQUEST = 71;
    public TextView mfilepath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private EditText mEmail,mPseudo,mMdp;
    private Button mEnregistrer;
    private FirebaseAuth auth;
    private int cpt=1;
    private ArrayList articleList;
    private FirebaseStorage Storage;
    private Uri filePath;
    private TextView mMdpVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final ArrayList<String> arrayList;
        final ArrayAdapter<String> adapter;
        final EditText txtinput ;

        articleList=new ArrayList<Article>();
        setContentView(R.layout.activity_signup);
        ListView listViewMotCle = findViewById(R.id.ListVMotcle);
        ListView listViewArticle=findViewById(R.id.ListVArticle);
        String [] item = {};
        arrayList = new ArrayList<>(Arrays.asList(item));
        adapter = new ArrayAdapter<String>(this,R.layout.list_motcle,R.id.txtitem,arrayList);
        listViewMotCle.setAdapter(adapter);
        txtinput= findViewById(R.id.txtinput);
        Button mImgAdd=findViewById(R.id.choisirPhoto);
        mfilepath=findViewById(R.id.photo);
        mImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


            }
        });

        Button btadd = findViewById(R.id.ajouterMcl);
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newitem = txtinput.getText().toString();
                if (!newitem.isEmpty())
                {
                arrayList.add(newitem);
                adapter.notifyDataSetChanged();
                txtinput.setText("");
                }
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
                }
            }
        });
        mEmail= findViewById(R.id.email);
        mPseudo= findViewById(R.id.pseudo);
        mMdp= findViewById(R.id.mdp);
        mMdpVer=findViewById(R.id.editText15);
        mEnregistrer= findViewById(R.id.enregCompte);

    mEnregistrer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String email = mEmail.getText().toString().trim();
            String password = mMdp.getText().toString().trim();
            String passver=mMdpVer.getText().toString().trim();
            String path=mfilepath.getText().toString().trim();
             final String pseudo =mPseudo.getText().toString().trim();

            if (TextUtils.isEmpty(pseudo)) {
                Toast.makeText(getApplicationContext(), "Entrer le pseudo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Entrer une adresse mail", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Entrer un mot de passe", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "mot de passe trop court, veuillez rentrer un minimum de 6 caractères", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.equals(passver))
            {
                Toast.makeText(getApplicationContext(), "Erreur mot de passe", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(getApplicationContext(), "Entrer une image", Toast.LENGTH_SHORT).show();
                return;
            }


            //créating account
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {

                                Toast.makeText(SignupActivity.this, "Inscription réussie " + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user!=null && pseudo!=null) {
                                    //changer les infos utilisateur
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(pseudo)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, " profile updated.");
                                                        startActivity(new Intent(SignupActivity.this, MenuActivity.class));
                                                        finish();
                                                    }
                                                }
                                            });
                                    String path ="images/";
                                    if(filePath!=null) {
                                        path+=UUID.randomUUID().toString();
                                        StorageReference ref = storageReference.child(path);

                                        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                                User uti = new User();
                                                Log.w("filepath",""+downloadUrl.toString());
                                                uti.addUser(user.getUid(), pseudo, email,downloadUrl.toString());
                                                uti.addMotcle(articleList, user.getUid());
                                                uti.recommand(user.getUid());

                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        Toast.makeText(SignupActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });



                                    }

                                }
                           //*****************************************************
                            } else {
                                Toast.makeText(SignupActivity.this, "erreur d'inscription" + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();


                            }
                        }
                    });


        }


    });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            String link;
            link=getImgPath(filePath);
            if(link!=null) {
                Log.w("Path", "" + link);
                mfilepath.setText(link.toString());
            }

        }
    }
   public String getImgPath(Uri uri) {
        String[] largeFileProjection = { MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA };
        String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
        Cursor myCursor = this.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                largeFileProjection, null, null, largeFileSort);
        String largeImagePath = "";
        try {
            myCursor.moveToFirst();
            largeImagePath = myCursor
                    .getString(myCursor
                            .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
        } finally {
            myCursor.close();
        }
        return largeImagePath;
    }


}
