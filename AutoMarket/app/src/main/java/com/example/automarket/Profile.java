package com.example.automarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automarket.LoginActivity;
import com.example.automarket.R;
import com.example.automarket.Utils.Utils;

import java.util.Locale;

public class Profile extends AppCompatActivity {

    Button logoutButton, updateProfile;
    SharedPreferences sharedPreferences;
    TextView usernameTextView, emailTextView, phoneTextView, usernametext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialiser les vues
        logoutButton = findViewById(R.id.deconnecter);
        updateProfile = findViewById(R.id.editProfile);
        usernameTextView = findViewById(R.id.usernames);
        emailTextView = findViewById(R.id.textView85);
        phoneTextView = findViewById(R.id.textView44);
        usernametext = findViewById(R.id.textView82);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Récupérer les informations de l'utilisateur depuis SharedPreferences
        String username = sharedPreferences.getString(Utils.KEY_USERNAME, "");
        String email = sharedPreferences.getString(Utils.KEY_EMAIL, "");
        String phone = sharedPreferences.getString(Utils.KEY_PHONE, "");

        // Afficher les informations de l'utilisateur dans les champs correspondants
        usernameTextView.setText(username);
        usernametext.setText(username);
        emailTextView.setText(email);
        phoneTextView.setText(phone);

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

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, updateProfile.class);
                startActivity(intent);
            }
        });

        // Ajouter un écouteur de clic pour le bouton de langue "FR"
        Button btnFr = findViewById(R.id.fr);
        btnFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("fr");
                recreate();
            }
        });

        // Ajouter un écouteur de clic pour le bouton de langue "EN"
        Button btnEn = findViewById(R.id.EN);
        btnEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                recreate();
            }
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Enregistrer la langue sélectionnée dans les préférences partagées
        SharedPreferences.Editor editor = getSharedPreferences("parameters", MODE_PRIVATE).edit();
        editor.putString("MaLangue", lang);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Charger la langue enregistrée lors du démarrage de l'activité
        loadLocale();
    }

    private void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("parameters", MODE_PRIVATE);
        String langue = prefs.getString("MaLangue", "");
        setLocale(langue);
    }
}
