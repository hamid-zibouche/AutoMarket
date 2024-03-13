package com.example.automarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.automarket.LoginActivity;
import com.example.automarket.R;
import com.example.automarket.Utils.Utils;

public class Profile extends AppCompatActivity {

    Button logoutButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialiser les vues
        logoutButton = findViewById(R.id.logoutButton);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Configurer l'écouteur de clic pour le bouton de déconnexion
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Effacer les informations de l'utilisateur des préférences partagées
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(Utils.KEY_ID);
                editor.remove(Utils.KEY_USERNAME);
                editor.remove(Utils.KEY_EMAIL);
                editor.apply();

                Toast.makeText(Profile.this, "Vous êtes déconnecté", Toast.LENGTH_SHORT).show();

                // Rediriger l'utilisateur vers l'écran de connexion
                Intent intent = new Intent(Profile.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optionnel : fermer l'activité de profil après la déconnexion
            }
        });
    }
}
