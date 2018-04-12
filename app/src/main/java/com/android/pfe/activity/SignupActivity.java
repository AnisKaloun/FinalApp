package com.android.pfe.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pfe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText mEmail,mPseudo,mMdp;
    private Button mEnregistrer;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);
        mEmail=(EditText)findViewById(R.id.Log_Email);
        mPseudo=(EditText)findViewById(R.id.Log_Pseudo);
        mMdp=(EditText)findViewById(R.id.Log_Mdp);
        mEnregistrer=(Button) findViewById(R.id.SignUp);

    mEnregistrer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             String email = mEmail.getText().toString().trim();
            String password = mMdp.getText().toString().trim();
             String pseudo =mPseudo.getText().toString().trim();

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

            //création compte
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Inscription réussie " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                              //  addUsers();
                                finish();

                            } else {
                                Toast.makeText(SignupActivity.this, "erreur d'inscription" + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();


                            }
                        }
                    });


        }

        private void addUsers() {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS_TABLE);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mPseudo.getText().toString().trim())
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            }
                        }
                    });
        }
    });

    }
}
