package com.example.automarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.Annonce;
import com.example.automarket.Utils.Utils;

public class AddAnnonce extends AppCompatActivity {

    EditText etMarque, etModele, etBoite, etEnergie, etMoteur, etKilometrage, etCouleur, etAnnee, etPrix, etPhotoUrl, etCommentaire, etAdresse;
    Button btnAddAnnonce;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_annonce);

        dbHandler = new DatabaseHandler(this);

        etMarque = findViewById(R.id.etMarque);
        etModele = findViewById(R.id.etModele);
        etBoite = findViewById(R.id.etBoite);
        etEnergie = findViewById(R.id.etEnergie);
        etMoteur = findViewById(R.id.etMoteur);
        etKilometrage = findViewById(R.id.etKilometrage);
        etCouleur = findViewById(R.id.etCouleur);
        etAnnee = findViewById(R.id.etAnnee);
        etPrix = findViewById(R.id.etPrix);
        etPhotoUrl = findViewById(R.id.etPhotoUrl);
        etCommentaire = findViewById(R.id.etCommentaire);
        etAdresse = findViewById(R.id.etAdresse);

        btnAddAnnonce = findViewById(R.id.btnAddAnnonce);

        btnAddAnnonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnnonce();
            }
        });

    }

    private void addAnnonce() {
        String marque = etMarque.getText().toString().trim();
        String modele = etModele.getText().toString().trim();
        String boite = etBoite.getText().toString().trim();
        String energie = etEnergie.getText().toString().trim();
        String moteur = etMoteur.getText().toString().trim();
        int kilometrage = Integer.parseInt(etKilometrage.getText().toString().trim());
        String couleur = etCouleur.getText().toString().trim();
        int annee = Integer.parseInt(etAnnee.getText().toString().trim());
        double prix = Double.parseDouble(etPrix.getText().toString().trim());
        String photoUrl = etPhotoUrl.getText().toString().trim();
        String commentaire = etCommentaire.getText().toString().trim();
        String adresse = etAdresse.getText().toString().trim();

        if (!marque.isEmpty() && !modele.isEmpty() && !boite.isEmpty() && !energie.isEmpty() && !moteur.isEmpty()
                && !couleur.isEmpty() && !photoUrl.isEmpty() && !commentaire.isEmpty() && !adresse.isEmpty()  ){

            Annonce annonce = new Annonce(marque, modele, boite, energie, moteur, kilometrage,
                    couleur, annee, prix, photoUrl, commentaire, adresse);

            dbHandler.addAnnonce(annonce, Utils.DEFAULT_USER_ID); // DEFAULT_USER_ID or the current user ID

            Toast.makeText(this, "Annonce ajoutée avec succès", Toast.LENGTH_SHORT).show();
            finish(); // Close activity after adding annonce
        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }
}