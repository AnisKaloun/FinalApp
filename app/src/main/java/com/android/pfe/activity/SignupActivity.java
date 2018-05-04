package com.android.pfe.activity;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.pfe.R;
import com.android.pfe.other.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Keep
public class SignupActivity extends AppCompatActivity {
    private static final String TAG ="SignupActivity" ;
    private EditText mEmail,mPseudo,mMdp;
    private Button mEnregistrer;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();

        final ArrayList<String> arrayList;
        final ArrayAdapter<String> adapter;
        final EditText txtinput ;
        setContentView(R.layout.activity_signup);
        ListView listView = findViewById(R.id.ListV);
        String [] item = {};
        arrayList = new ArrayList<>(Arrays.asList(item));
        adapter = new ArrayAdapter<String>(this,R.layout.list_motcle,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);
        txtinput= findViewById(R.id.txtinput);
        Button btadd = findViewById(R.id.btadd);
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newitem = txtinput.getText().toString();
                arrayList.add(newitem);
                adapter.notifyDataSetChanged();
                txtinput.setText("");
            }
        });

        mEmail= findViewById(R.id.email);
        mPseudo= findViewById(R.id.pseudo);
        mMdp= findViewById(R.id.mdp);
        mEnregistrer= findViewById(R.id.enregCompte);

    mEnregistrer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String email = mEmail.getText().toString().trim();
            String password = mMdp.getText().toString().trim();
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


            //créating account
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {

                                Toast.makeText(SignupActivity.this, "Inscription réussie " + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                                    User uti = new User();
                                    uti.addUser(user.getUid(), pseudo, email);
                                    if(arrayList.isEmpty()==false) {
                                        final HashMap<String, Integer> map = new HashMap<>();
                                        for (int i = 0; i < arrayList.size(); i++) {
                                            map.put(arrayList.get(i),1);
                                        }
                                        uti.addMotcle(map, user.getUid());
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
}
