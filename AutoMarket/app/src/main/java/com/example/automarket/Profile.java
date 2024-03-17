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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
                        Intent intent = new Intent(Profile.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Optionnel : fermer l'activité de profil après la déconnexion
                    }
                });
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, updateProfile.class);
                startActivity(intent);
            }
        });
    }

    // Méthode pour afficher une boîte de dialogue de confirmation
    private void showConfirmationDialog(String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Oui", listener)
                .setNegativeButton("Non", null)
                .show();
    }


}
