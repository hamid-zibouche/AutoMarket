package com.example.automarket;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automarket.Utils.Utils;
import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.User;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button button;
    ImageButton imageButton;
    TextView textView;
    SharedPreferences sharedPreferences;
    DatabaseHandler myDB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation de DatabaseHandler
        myDB = new DatabaseHandler(this);

        // Initialisation de SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Récupération des vues
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        imageButton = findViewById(R.id.imageButton);
        textView = findViewById(R.id.creercompte);
        button = findViewById(R.id.connection);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

                if (usernameText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    User user = myDB.checkUsernamePassword(usernameText, passwordText);
                    if (user != null) {
                        Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        int userID = user.getId();


                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(Utils.KEY_ID, userID);
                        editor.putString(Utils.KEY_USERNAME, user.getUsername());
                        editor.putString(Utils.KEY_EMAIL, user.getEmail());
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Identifiants invalides", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registerActivity2.class);
                startActivity(intent);
            }
        });
    }
}
