package com.example.automarket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.Annonce;
import com.example.automarket.adapter.PopularListAdapter;
import com.example.automarket.domain.PopularDomain;
import com.example.automarket.Utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterPopular;
    private RecyclerView recyclerViewPopular1;
    private DatabaseHandler db = new DatabaseHandler(this);


    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        afficher();

        checkStoragePermission();
        initRecyclerView();

        favoris();
        annonce();
        profile();

        replaceFragment(new VehiculesFragment());
        recherche();
    }

    @Override
    protected void onResume() {
        super.onResume();
        afficher();
        initRecyclerView();
        replaceFragment(new VehiculesFragment()); // Actualiser la liste des annonces lors de la reprise de MainActivity
    }

    private void afficher(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if(sharedPreferences.contains(Utils.KEY_USERNAME)) {
            TextView username = findViewById(R.id.usernameMain);
            username.setText(sharedPreferences.getString(Utils.KEY_USERNAME, "GUEST"));
        }else{
            TextView username = findViewById(R.id.usernameMain);
            username.setText("Guest");
        }
    }


    private void recherche() {
        EditText rechercheEditText = findViewById(R.id.recherche);
        rechercheEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // L'utilisateur a appuyé sur "Done" ou "Search"
                    String rechercheText = rechercheEditText.getText().toString().trim();

                    // Vérifiez si le champ de recherche est vide
                    if (!rechercheText.isEmpty()) {
                        if (getSupportFragmentManager() != null) {
                            VehiculesFragment vehiculesFragment = (VehiculesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if (vehiculesFragment != null) {
                                vehiculesFragment.searchAnnonces(rechercheText);
                            }
                        }
                    } else {
                        // Le champ de recherche est vide, vous pouvez afficher un message à l'utilisateur ou rafraîchir la liste complète
                        if (getSupportFragmentManager() != null) {
                            VehiculesFragment vehiculesFragment = (VehiculesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if (vehiculesFragment != null) {
                                vehiculesFragment.exitSearchMode();
                            }
                        }
                    }

                    // Masquer le clavier après la recherche
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rechercheEditText.getWindowToken(), 0);

                    return true; // Indique que l'événement a été traité
                }
                return false;
            }
        });
    }






    // Méthode pour vérifier si l'utilisateur est déjà connecté
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.contains(Utils.KEY_ID);
    }



    private void initRecyclerView() {
        try {
            List<Annonce> popularAnnonces = db.getPopularAnnonces();

            if (popularAnnonces.isEmpty()) {
                // Si la liste est vide, afficher un message à l'utilisateur
                // Vous pouvez utiliser un TextView ou un Toast pour afficher le message
                // Par exemple, avec un Toast :
                Toast.makeText(this, "Aucune annonce populaire à afficher", Toast.LENGTH_SHORT).show();
                return; // Sortir de la méthode car il n'y a rien à afficher
            }

            // Créer une liste d'items pour l'adaptateur
            ArrayList<PopularDomain> items = new ArrayList<>();
            for (int i = 0; i < Math.min(popularAnnonces.size(), 5); i++) {
                Annonce annonce = popularAnnonces.get(i);
                PopularDomain popularDomain = new PopularDomain(
                        annonce,
                        annonce.getMarque()+ " " + annonce.getModele()+ " " +annonce.getAnnee(),
                        "vehicule",
                        annonce.getNbrVue(),
                        annonce.getPrix()
                );
                items.add(popularDomain);
            }

            recyclerViewPopular1 = findViewById(R.id.view1);
            recyclerViewPopular1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            adapterPopular = new PopularListAdapter(items,db,MainActivity.this);
            recyclerViewPopular1.setAdapter(adapterPopular);
        } catch (Exception e) {
            e.printStackTrace();
            // Afficher un message d'erreur ou effectuer toute autre action nécessaire en cas d'exception
            Toast.makeText(this, "Une erreur s'est produite lors du chargement des annonces populaires.", Toast.LENGTH_SHORT).show();
        }
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
            replaceFragment(new VehiculesFragment());
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your code
                replaceFragment(new VehiculesFragment());
            } else {
                // Permission denied, show a message or handle it gracefully
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                // Optionnel : Vous pouvez redemander la permission ici ou fermer l'application
                // requestStoragePermission();
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
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
