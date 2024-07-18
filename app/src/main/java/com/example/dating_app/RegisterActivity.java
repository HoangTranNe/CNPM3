package com.example.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegister;
    private ProgressBar spinner;
    private EditText mEmail, mPass, mName;
    private CheckBox checkBox;

    private DBHelper dbHelper;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        dbHelper = new DBHelper(this);

        // Initialize views
        spinner = findViewById(R.id.pBar);
        mEmail = findViewById(R.id.email);
        mPass = findViewById(R.id.pass);
        mName = findViewById(R.id.name);
        checkBox = findViewById(R.id.checkbox1);

        // Set up Terms & Conditions TextView with hyperlink
        TextView textView = findViewById(R.id.tv2);
        textView.setText(Html.fromHtml("I have read and agree to the " +
                "<a href = 'https://www.blogger.com/blog/post/edit/570335079672114278/2606069925882363493'> Terms & Conditions</a>"));
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        spinner.setVisibility(View.GONE);

        // Set up register button click listener
        mRegister = findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.GONE);

                final String email = mEmail.getText().toString();
                final String pass = mPass.getText().toString();
                final String name = mName.getText().toString();
                final boolean tnc = checkBox.isChecked();

                if (checkInputs(email, name, pass, tnc)) {
                    // Check if the email is already registered in SQLite
                    if (dbHelper.checkUserExists(email)) {
                        Toast.makeText(RegisterActivity.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.VISIBLE);
                    } else {
                        // Add user to SQLite
                        long userId = dbHelper.addUser(email, name, pass);
                        if (userId != -1) {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            clearInputs();
                            startActivity(new Intent(RegisterActivity.this, Login_Regis.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.VISIBLE);
                    }
                } else {
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean checkInputs(String email, String name, String pass, boolean tnc) {
        if (email.isEmpty() || name.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.matches(emailPattern)) {
            Toast.makeText(this, "Invalid email address, enter a valid email id and click on confirm", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!tnc) {
            Toast.makeText(this, "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearInputs() {
        mEmail.setText("");
        mName.setText("");
        mPass.setText("");
    }
}
