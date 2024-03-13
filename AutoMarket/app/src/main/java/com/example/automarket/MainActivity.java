package com.example.automarket;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.Annonce;
import com.example.automarket.adapter.PopularListAdapter;
import com.example.automarket.domain.PopularDomain;
import com.example.automarket.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterPopular;
    private RecyclerView recyclerViewPopular1;
    private DatabaseHandler db;




    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);



        Button changerLangue = findViewById(R.id.btnChanerlangue);


        changerLangue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLangueDialog();
            }
        });

        checkStoragePermission();
        initRecyclerView();
        initAllVehicules();
        favoris();
        annonce();
        profile();
    }

    private void showChangeLangueDialog() {
        final String[] listItems = {"Francais", "Anglais"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("choisie une langue ...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
        if (i == 0){
            setLocale("fr");
            recreate();
        }

       else  if (i == 1){
            setLocale("en");
            recreate();
        }

         dialogInterface.dismiss();

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang ) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("parameters", MODE_PRIVATE).edit();
        editor.putString("Ma lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("parameters", Activity.MODE_PRIVATE);
        String langue =prefs.getString("Ma lang", "");
        setLocale(langue);

    }


    // Méthode pour vérifier si l'utilisateur est déjà connecté
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.contains(Utils.KEY_ID);
    }



    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("peugout 406", "", "vehicule", 15, 15, 500, 500));
        items.add(new PopularDomain("Mercedes", "", "vehicule2", 15, 15, 500, 500));
        items.add(new PopularDomain("BMW", "", "vehicule3", 15, 15, 500, 500));
        items.add(new PopularDomain("peugout 406", "", "vehicule", 15, 15, 500, 500));
        items.add(new PopularDomain("Mercedes", "", "vehicule2", 15, 15, 500, 500));
        items.add(new PopularDomain("BMW", "", "vehicule3", 15, 15, 500, 500));
        items.add(new PopularDomain("peugout 406", "", "vehicule", 15, 15, 500, 500));
        items.add(new PopularDomain("Mercedes", "", "vehicule2", 15, 15, 500, 500));
        items.add(new PopularDomain("BMW", "", "vehicule3", 15, 15, 500, 500));

        recyclerViewPopular1 = findViewById(R.id.view1);
        recyclerViewPopular1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterPopular = new PopularListAdapter(items);
        recyclerViewPopular1.setAdapter(adapterPopular);
    }

    @Override
    protected void onResume() {
        super.onResume();


        initAllVehicules(); // Actualiser la liste des annonces lors de la reprise de MainActivity
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Permission already granted
            initAllVehicules();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your code
                initAllVehicules();
            } else {
                // Permission denied, show a message or handle it gracefully
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                // Optionnel : Vous pouvez redemander la permission ici ou fermer l'application
                // requestStoragePermission();
            }
        }
    }

    public void initAllVehicules() {

        db = new DatabaseHandler(this);

        List<Annonce> annonces = db.getAllAnnonces();

        LinearLayout vh = findViewById(R.id.vh);
        vh.removeAllViews(); // Supprimer toutes les vues précédentes avant d'ajouter les nouvelles annonces

        for (Annonce annonce : annonces) {
            View itemView = getLayoutInflater().inflate(R.layout.list_vehicules, null);

            TextView marque = itemView.findViewById(R.id.marque);
            TextView model = itemView.findViewById(R.id.model);
            TextView annee = itemView.findViewById(R.id.annee);
            TextView priceTextView = itemView.findViewById(R.id.feetxt);
            ImageView photo = itemView.findViewById(R.id.pic);

            TextView kelo = itemView.findViewById(R.id.kelo);
            TextView carburant = itemView.findViewById(R.id.carburant);
            TextView moteur = itemView.findViewById(R.id.moteur);
            TextView boite = itemView.findViewById(R.id.boite);
            TextView adresse = itemView.findViewById(R.id.adresse);
            TextView time = itemView.findViewById(R.id.time);

            marque.setText(annonce.getMarque());
            model.setText(annonce.getModele());
            annee.setText(String.valueOf(annonce.getAnnee()));
            priceTextView.setText(String.valueOf(annonce.getPrix()) + "€");
            kelo.setText(String.valueOf(annonce.getKilometrage()) + "KM");
            carburant.setText(annonce.getEnergie());
            moteur.setText(annonce.getMoteur());
            boite.setText(annonce.getBoite());
            adresse.setText(annonce.getAdresse());
            time.setText(annonce.getDateCreation());

            Log.d("image", annonce.getPhotoUrl());
            // Charger l'image à partir de l'URI en utilisant Glide
            if (annonce.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(Uri.parse(annonce.getPhotoUrl()))
                        .placeholder(R.drawable.nocar) // Image de placeholder
                        .error(R.drawable.nocar) // Image à afficher en cas d'erreur
                        .into(photo);
            } else {
                // Si l'URI est null, vous pouvez afficher une image par défaut
                photo.setImageResource(R.drawable.nocar);
            }

            //methode d'appelle
            TextView appeler = itemView.findViewById(R.id.appeler);
            appeler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "APPELER", Toast.LENGTH_SHORT).show();
                }
            });

            //methode de message
            TextView message = itemView.findViewById(R.id.message);
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "MESSAGE", Toast.LENGTH_SHORT).show();
                }
            });

            // Ajouter un OnClickListener à chaque itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lorsque l'utilisateur clique, ouvrir la DetailView avec les données de l'élément
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("title", annonce.getMarque());
                    intent.putExtra("price", annonce.getPrix());
                    intent.putExtra("objet", annonce);
                    // Ajoutez d'autres données si nécessaire
                    startActivity(intent);
                }
            });

            vh.addView(itemView);
        }
    }

    public void favoris() {
        LinearLayout favoris = findViewById(R.id.favoris);

        favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isLoggedIn()) {
                    Intent intent = new Intent(MainActivity.this, Favoris.class);
                    startActivity(intent);
                } else {
                    // Rediriger vers l'écran de connexion
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }


            }
        });
    }

    public void annonce() {
        LinearLayout annonce = findViewById(R.id.annonce);

        annonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (isLoggedIn()) {
                    Intent intent = new Intent(MainActivity.this, AddAnnonce.class);
                    startActivity(intent);
                } else {
                    // Rediriger vers l'écran de connexion
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void profile() {
        LinearLayout profile = findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifier si l'utilisateur est connecté
                if (isLoggedIn()) {
                    // Ouvrir la page de profil
                    Intent intent = new Intent(MainActivity.this, Profile.class);
                    startActivity(intent);
                } else {
                    // Rediriger vers l'écran de connexion
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });


    }



}
