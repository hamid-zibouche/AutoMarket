package com.example.automarket;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.Annonce;
import com.example.automarket.Model.User;
import com.example.automarket.domain.PopularDomain;

public class DetailActivity extends AppCompatActivity {

    private DatabaseHandler db = new DatabaseHandler(this);
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

            TextView usernameAnnonce = findViewById(R.id.nomprenom);
            TextView adresseUser = findViewById(R.id.adresseUser);
            TextView phoneUser = findViewById(R.id.numeroTel);
            ImageView retour = findViewById(R.id.retour);
            TextView titrePage = findViewById(R.id.titrePage);

            //fonction de retour
            retourFinish(retour);
            titrePage.setText("Details");
            TextView deconnecter = findViewById(R.id.deconnecter);
            deconnecter.setVisibility(View.GONE);

            Button detail_appeler = findViewById(R.id.datail_appeler);

            int userId = objet.getUserId();
            User userAnnonce = db.getUser(userId);

            detail_appeler.setOnClickListener(v -> {
                String phoneNumber = userAnnonce.getPhone(); // Le numéro de téléphone du client
                Intent callServiceIntent = new Intent(this, CallService.class);
                callServiceIntent.putExtra("phone_number", phoneNumber);
                this.startService(callServiceIntent);
            });

            Button detail_message = findViewById(R.id.datail_message);
            detail_message.setOnClickListener(v -> {
                String phoneNumber = userAnnonce.getPhone(); // Le numéro de téléphone du client
                String messageText = "Bonjour, je suis intéressé par votre annonce pour le Vehicule "+objet.getMarque()+" "+objet.getModele()+" "+objet.getAnnee()+"."; // Le message à envoyer
                Intent messageServiceIntent = new Intent(this, MessageService.class);
                messageServiceIntent.putExtra("phone_number", phoneNumber);
                messageServiceIntent.putExtra("message", messageText);
                this.startService(messageServiceIntent);
            });

            Log.d("console",String.valueOf(objet.getUserId()));
            try {

                if (userAnnonce != null) {
                    usernameAnnonce.setText(String.valueOf(userAnnonce.getUsername()));
                    phoneUser.setText(String.valueOf(userAnnonce.getPhone()));
                } else {
                    // L'utilisateur n'a pas été trouvé, afficher un message ou gérer la situation
                    Log.e(TAG, "User not found in database");
                    // Par exemple, afficher un message à l'utilisateur
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // Gérer l'exception, par exemple en affichant un message dans les logs
                Log.e(TAG, "Error getting user from database", e);
                // Vous pouvez aussi afficher un message à l'utilisateur si nécessaire
            }


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

            ImageView photo = findViewById(R.id.imageView4);

            if (objet.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(Uri.parse(objet.getPhotoUrl()))
                        .placeholder(R.drawable.nocar)
                        .error(R.drawable.nocar)
                        .into(photo);
            } else {
                photo.setImageResource(R.drawable.nocar);
            }

        }
    }

    public void retourFinish(ImageView retour){
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}