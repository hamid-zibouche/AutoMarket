package com.example.automarket;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.automarket.Utils.Utils;

import com.example.automarket.Controller.DatabaseHandler;

public class LoginActivity extends AppCompatActivity {

    EditText  email, password;
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


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        imageButton = findViewById(R.id.imageButton);
        textView =findViewById(R.id.creercompte);
        button = findViewById(R.id.connection);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(email.equals("") || password.equals(""))
                    Toast.makeText(LoginActivity.this, "all field are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkCredentials = myDB.checkEmailPassword(emailText, passwordText);

                    if(checkCredentials == true){
                        Toast.makeText(LoginActivity.this, "connexion reussie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        int userID = 123;

                        // Enregistrer les données dans SharedPreferences après une connexion réussie
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putInt(Utils.KEY_ID, userID);
                        editor.putString(Utils.KEY_EMAIL, emailText);
                        editor.putString(Utils.KEY_PASSWORD, passwordText);
                        editor.apply();
                    }else {
                        Toast.makeText(LoginActivity.this, "Identifiants invalides", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        // Récupérer les données enregistrées dans SharedPreferences

        String savedEmail = sharedPreferences.getString(Utils.KEY_EMAIL, "");
        String savedPassword = sharedPreferences.getString(Utils.KEY_PASSWORD, "");
        int savedUserID = sharedPreferences.getInt(Utils.KEY_USER_ID, -1); // Par défaut, -1 si l'ID n'est pas trouvé
        if (!savedEmail.isEmpty() && !savedPassword.isEmpty() && savedUserID != -1) {
            // Si des données sont enregistrées, vous pouvez les utiliser selon vos besoins.
            // Par exemple, vous pouvez pré-remplir les champs username, email et password dans votre formulaire de connexion.

            email.setText(savedEmail);
            password.setText(savedPassword);
        }



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