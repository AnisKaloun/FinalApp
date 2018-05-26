package com.android.pfe.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pfe.R;
import com.android.pfe.activity.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by lety2018 on 05/04/2018.
 */

public class Modif_mot_de_passe extends Activity {
    private static final String TAG ="Modif_mot de passe" ;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText mAncienUser;
    private EditText mNouveauUser;
    private Button mModifier;
    private FirebaseUser userFire;
    private EditText mComfirmemdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_acceuil);
        setTitle("Modifier Mot De Passe");
        userFire = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.modif_pseudo);
        mAncienUser=findViewById(R.id.editText4);
        mNouveauUser=findViewById(R.id.editText5);
        mComfirmemdp=findViewById(R.id.editText6);
        mModifier=findViewById(R.id.button7);
        mAuth=FirebaseAuth.getInstance();
        mModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            if((mNouveauUser.getText().toString()).equals(mComfirmemdp.getText().toString()))
                            {
                                userFire.updatePassword(mNouveauUser.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User password updated.");
                                                }
                                            }
                                        });

                                Intent insc = new Intent(Modif_mot_de_passe.this,MenuActivity.class);
                                startActivity(insc);
                            }
                            else
                            {
                                Toast.makeText(Modif_mot_de_passe.this, "mot de passe incorrect", Toast.LENGTH_SHORT).show();
                            }

                    }


        });


    }
}



