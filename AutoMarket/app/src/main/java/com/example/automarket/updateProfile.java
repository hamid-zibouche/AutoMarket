package com.example.automarket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.User;
import com.example.automarket.Utils.Utils;

public class updateProfile extends AppCompatActivity {

    EditText editUsername, editEmail, editTelephone;
    Button editButton;
    ImageView backButton;
    DatabaseHandler myDB;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        editUsername = findViewById(R.id.editusername);
        editEmail = findViewById(R.id.editemail);
        editTelephone = findViewById(R.id.edittelephone);
        editButton = findViewById(R.id.edit);
        backButton = findViewById(R.id.retour);

        TextView titrePage = findViewById(R.id.titrePage);
        titrePage.setText("Update Profile");
        TextView deconnecter = findViewById(R.id.deconnecter);
        deconnecter.setVisibility(View.GONE);

        myDB = new DatabaseHandler(this);

        // Récupérer l'ID de l'utilisateur connecté à partir des préférences partagées
        int userId = getUserIdFromSharedPreferences();
        // Récupérer les informations de l'utilisateur à partir de l'ID
        currentUser = myDB.getUser(userId);

        // Remplir les champs du formulaire avec les informations de l'utilisateur
        editUsername.setText(currentUser.getUsername());
        editEmail.setText(currentUser.getEmail());
        editTelephone.setText(currentUser.getPhone());

        // Définir le OnClickListener pour le bouton de modification
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("Voulez-vous vraiment modifier votre profil ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateUserProfile();
                    }
                });
            }
        });

        // Définir le OnClickListener pour le bouton de retour
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("Voulez-vous vraiment quitter sans sauvegarder les modifications ?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
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

    // Méthode pour récupérer l'ID de l'utilisateur connecté à partir des préférences partagées
    private int getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt(Utils.KEY_ID, 0);
    }

    // Méthode pour mettre à jour le profil de l'utilisateur
    private void updateUserProfile() {
        String newUsername = editUsername.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();
        String newTelephone = editTelephone.getText().toString().trim();

        if (newUsername.isEmpty() || newEmail.isEmpty() || newTelephone.isEmpty()) {
            Toast.makeText(updateProfile.this, "Tous les champs doivent être renseignés", Toast.LENGTH_SHORT).show();
        } else {
            // Mettre à jour les informations de l'utilisateur dans la base de données
            currentUser.setUsername(newUsername);
            currentUser.setEmail(newEmail);
            currentUser.setPhone(newTelephone);

            boolean isUpdated = myDB.updateUser(currentUser);

            if (isUpdated) {
                Toast.makeText(updateProfile.this, "Informations mises à jour avec succès", Toast.LENGTH_SHORT).show();
                // Mettre à jour les informations dans SharedPreferences si nécessaire
                updateSharedPreferences(currentUser);


                Intent intent = new Intent(updateProfile.this, Profile.class);
                startActivity(intent);
                finish(); 
            } else {
                Toast.makeText(updateProfile.this, "Erreur lors de la mise à jour des informations", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Méthode pour mettre à jour les informations de l'utilisateur dans SharedPreferences
    private void updateSharedPreferences(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Utils.KEY_USERNAME, user.getUsername());
        editor.putString(Utils.KEY_EMAIL, user.getEmail());
        editor.putString(Utils.KEY_PHONE, user.getPhone());
        editor.apply();
    }
}
