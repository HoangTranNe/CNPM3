package com.example.dating_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ProgressBar spinner;
    private Button mLogin;
    private EditText mEmail, mPass;
    private TextView mForget;
    private boolean loginBtnClicked;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        spinner = findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);

        mLogin = findViewById(R.id.login);
        mEmail = findViewById(R.id.email);
        mPass = findViewById(R.id.pass);
        mForget = findViewById(R.id.forget);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnClicked = true;
                spinner.setVisibility(View.VISIBLE);
                final String email = mEmail.getText().toString();
                final String pass = mPass.getText().toString();

                if (isStringNull(email) || isStringNull(pass)) {
                    Toast.makeText(LoginActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Validate login with SQLite
                    if (validateLogin(email, pass)) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        return;
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
                spinner.setVisibility(View.GONE);
            }
        });

        mForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                Intent i = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean isStringNull(String email) {
        return email.equals("");
    }

    private boolean validateLogin(String email, String pass) {
        Cursor cursor = dbHelper.getUser(email);
        if (cursor.moveToFirst()) {
            String storedPass = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PASS));
            return pass.equals(storedPass);
        }
        return false;
    }
}
