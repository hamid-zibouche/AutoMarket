package com.example.automarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.automarket.Model.Annonce;
import com.example.automarket.domain.PopularDomain;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        if (intent != null) {
            Annonce objet = (Annonce) intent.getSerializableExtra("objet");
            String title = intent.getStringExtra("title");
            int price = intent.getIntExtra("price", 0);
            int score = intent.getIntExtra("score", 0);

            // Afficher les données dans les TextViews de votre layout

            TextView titre = findViewById(R.id.textView9);
            TextView prixTitre = findViewById(R.id.textView17);

            TextView prix = findViewById(R.id.prix);
            TextView marque = findViewById(R.id.marque);
            TextView modele = findViewById(R.id.modele);
            TextView boite = findViewById(R.id.boite);
            TextView energie = findViewById(R.id.energie);
            TextView moteur = findViewById(R.id.moteur);
            TextView kilometrage = findViewById(R.id.kilometrage);
            TextView couleur = findViewById(R.id.couleur);
            TextView annee = findViewById(R.id.annee);
            TextView adresse = findViewById(R.id.adresse);
            TextView date = findViewById(R.id.date);
            TextView description = findViewById(R.id.description);



            titre.setText(objet.getMarque() + " "+objet.getModele()+" "+String.valueOf(objet.getAnnee()));
            prix.setText(String.valueOf(objet.getPrix()) + " €");
            prixTitre.setText(String.valueOf(objet.getPrix()) + " €");
            date.setText(objet.getDateCreation());
            marque.setText(objet.getMarque());
            modele.setText(objet.getModele());
            boite.setText(objet.getBoite());
            energie.setText(objet.getEnergie());
            moteur.setText(objet.getMoteur());
            kilometrage.setText(String.valueOf(objet.getKilometrage())+" KM");
            couleur.setText(objet.getCouleur());
            annee.setText(String.valueOf(objet.getAnnee()));
            adresse.setText(objet.getAdresse());
            description.setText(objet.getCommentaire());

        }
    }
}