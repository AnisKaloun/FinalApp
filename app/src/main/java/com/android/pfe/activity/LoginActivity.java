package com.android.pfe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.pfe.R;

public class LoginActivity extends AppCompatActivity {
    private EditText MailAdress;
    private EditText Password;
    private TextView RegisterLink;
    private Button LogButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        MailAdress=(EditText) findViewById(R.id.MailAdress);
        Password=(EditText) findViewById(R.id.Password);
        LogButton=(Button) findViewById(R.id.LogButton);
        RegisterLink= (TextView) findViewById(R.id.Register);
        LogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(menu);
            }
        });
        RegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
    }
}
