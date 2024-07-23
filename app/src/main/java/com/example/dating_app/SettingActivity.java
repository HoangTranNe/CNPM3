/*
package com.example.dating_app;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

public class SettingActivity extends AppCompatActivity {


    private EditText mNameField, mPhoneField;
    private ProgressBar spinner;
    private Button mConfirm;
    private ImageButton mBack;
    private ImageView mProfileImage;
    private EditText mBudget;
    private Spinner need, give;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userId, name, phone, profileImageUrl, userSex, userBudget, userNeed, userGive;
    private int needIndex, giveIndex;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        spinner = (ProgressBar) findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);
        mNameField = (EditText) findViewById(R.id.name);
        mPhoneField = (EditText) findViewById(R.id.phone);
        mProfileImage = (ImageView) findViewById(R.id.profileImage);
        mBack = findViewById(R.id.settingsBack);
        mConfirm = (Button) findViewById(R.id.confirm);
        mBudget = (EditText) findViewById(R.id.budget_settings);
        need = (Spinner) findViewById(R.id.spinner_need_settings);
        give = (Spinner) findViewById(R.id.spinner_give_setting);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth != null && mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        } else {
            finish();
        }

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.services, android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        give.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_give = ArrayAdapter.createFromResource(
                this, R.array.services, android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        give.setAdapter(adapter_give);

        //getUserInfo();

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    Toast.makeText(SettingActivity.this, "Please allow access to continue!", Toast.LENGTH_SHORT).show();
                    requestPermissions();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                }
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 100);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(this, "Please allow access to continue!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ContactUs) {
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Contact Us")
                    .setMessage("Contact us: codingbunch@gmail.com")
                    .setNegativeButton("Dismiss", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (item.getItemId() == R.id.logout) {
            spinner.setVisibility(View.VISIBLE);
            mAuth.signOut();
            Toast.makeText(this, "Log Out successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingsActivity.this, Choose_Login_And_Reg.class);
            startActivity(intent);
            finish();
            spinner.setVisibility(View.GONE);
        }
        else if(item.getItemId() == R.id.deleteAccount){
            new AlertDialog.Builder(SettingActivity.this)
                    .setTitle("Are you sure")
                    .setMessage("Deleting your account will result in completely removing your account from the system")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                    spinner.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        deleteUserAccount(userId);
                                        Toast.makeText(SettingActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SettingActivity.this, Login_Regis.class);
                                        startActivity(intent);
                                        finish();
                                        spinner.setVisibility(View.GONE);
                                        return;
                                    }
                                    else{
                                        Toast.makeText(SettingActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
        }


    }}


*/
