package com.example.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login_Regis extends AppCompatActivity {
    private ProgressBar spinner;
    private Button mLogin,mRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_regis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mLogin =(Button) findViewById(R.id.login);
        mRes  =(Button) findViewById(R.id.register);
        spinner=findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Login_Regis.this, LoginActivity.class);
                startActivity(i);
                finish();
                spinner.setVisibility(View.VISIBLE);
            }
        });

        mRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Login_Regis.this, RegisterActivity.class);
                startActivity(i);
                finish();
                spinner.setVisibility(View.VISIBLE);
            }
        });
    }
}