package com.example.automarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.User;

public class registerActivity2 extends AppCompatActivity {

    Button signup;
    EditText username, email, password, confirmpassword, telephone;
    TextView textView;
    ImageButton imageButton;

    DatabaseHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        // Initialiser les vues à l'aide de findViewById
        signup = findViewById(R.id.signup);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        telephone = findViewById(R.id.telephone);
        textView = findViewById(R.id.loginRedirectText);
        imageButton = findViewById(R.id.imageButton2);


        myDB = new DatabaseHandler(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String confirmpasswordText = confirmpassword.getText().toString();
                String telephoneText = telephone.getText().toString();

                if (usernameText.equals("") || emailText.equals("") || passwordText.equals("") || confirmpasswordText.equals("") || telephoneText.equals("")) {
                    Toast.makeText(registerActivity2.this, "Tous les champs doivent être renseignés", Toast.LENGTH_SHORT).show();
                } else {
                    if (passwordText.equals(confirmpasswordText)) {
                        Boolean checkUserUsername = myDB.checkUsername(usernameText);
                        if (!checkUserUsername) {
                            // Ajouter l'utilisateur à la base de données
                            Boolean insert = myDB.addUser(new User(usernameText, emailText, passwordText, telephoneText));
                            if (insert) {
                                Toast.makeText(registerActivity2.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(registerActivity2.this, "Inscription non réussie", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(registerActivity2.this, "Username  existe déjà", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(registerActivity2.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
