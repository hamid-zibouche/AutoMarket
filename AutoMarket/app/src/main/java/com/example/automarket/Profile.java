package com.example.automarket;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import com.example.automarket.LoginActivity;
import com.example.automarket.R;
import com.example.automarket.Utils.Utils;

public class Profile extends AppCompatActivity {

    Button logoutButton, updateProfile;
    SharedPreferences sharedPreferences;
    TextView usernameTextView, emailTextView, phoneTextView, usernametext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        TextView titrePage = findViewById(R.id.titrePage);
        titrePage.setText(R.string.profile);

        // Configurer l'écouteur de clic pour le bouton de langue "FR"
        Button btnFr = findViewById(R.id.fr);
        btnFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("fr");
                recreate();
            }
        });

        // Configurer l'écouteur de clic pour le bouton de langue "EN"
        Button btnEn = findViewById(R.id.EN);
        btnEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                recreate();
            }
        });

        // Configurer l'écouteur de clic pour le bouton de déconnexion/update
        deconnecter();
        updateProfile();
        retour();
    }

    // Méthode pour afficher une boîte de dialogue de confirmation
    private void showConfirmationDialog(String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Oui", listener)
                .setNegativeButton("Non", null)
                .show();
    }

    private void retour(){
        ImageView retour = findViewById(R.id.retour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deconnecter(){
        TextView deconnecter = findViewById(R.id.deconnecter);
        deconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("Voulez-vous vraiment vous déconnecter ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Effacer les informations de l'utilisateur des préférences partagées
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(Utils.KEY_ID);
                        editor.remove(Utils.KEY_USERNAME);
                        editor.remove(Utils.KEY_EMAIL);
                        editor.apply();

                        Toast.makeText(Profile.this, "Vous êtes déconnecté", Toast.LENGTH_SHORT).show();

                        // Rediriger l'utilisateur vers l'écran de connexion
                        Intent intent = new Intent(Profile.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optionnel : fermer l'activité de profil après la déconnexion
                    }
                });
            }
        });

        logoutButton = findViewById(R.id.deconnecter2);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("Voulez-vous vraiment vous déconnecter ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Effacer les informations de l'utilisateur des préférences partagées
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(Utils.KEY_ID);
                        editor.remove(Utils.KEY_USERNAME);
                        editor.remove(Utils.KEY_EMAIL);
                        editor.apply();

                        Toast.makeText(Profile.this, "Vous êtes déconnecté", Toast.LENGTH_SHORT).show();

                        // Rediriger l'utilisateur vers l'écran de connexion
                        Intent intent = new Intent(Profile.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optionnel : fermer l'activité de profil après la déconnexion
                    }
                });
            }
        });
    }

    private void updateProfile(){
        updateProfile = findViewById(R.id.editProfile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, updateProfile.class);
                startActivity(intent);
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
