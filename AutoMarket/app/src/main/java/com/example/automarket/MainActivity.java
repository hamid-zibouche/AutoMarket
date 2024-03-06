
package com.example.automarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.Annonce;
import com.example.automarket.adapter.ListAdapter;
import com.example.automarket.adapter.PopularListAdapter;
import com.example.automarket.domain.PopularDomain;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapterPopular;
    private RecyclerView recyclerViewPopular1,recyclerViewPopular2;
    private DatabaseHandler db;
    private ListView list_allV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initRecyclerView();
        initAllVehicules();
        appeler();
        annonce();
        favoris();
        profile();
    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("peugout 406","","vehicule",15,15,500,500));
        items.add(new PopularDomain("Mercedes","","vehicule2",15,15,500,500));
        items.add(new PopularDomain("BMW","","vehicule3",15,15,500,500));
        items.add(new PopularDomain("peugout 406","","vehicule",15,15,500,500));
        items.add(new PopularDomain("Mercedes","","vehicule2",15,15,500,500));
        items.add(new PopularDomain("BMW","","vehicule3",15,15,500,500));
        items.add(new PopularDomain("peugout 406","","vehicule",15,15,500,500));
        items.add(new PopularDomain("Mercedes","","vehicule2",15,15,500,500));
        items.add(new PopularDomain("BMW","","vehicule3",15,15,500,500));
        recyclerViewPopular1 = findViewById(R.id.view1);
        recyclerViewPopular1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapterPopular=new PopularListAdapter(items);
        recyclerViewPopular1.setAdapter(adapterPopular);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initAllVehicules(); // Actualiser la liste des annonces lors de la reprise de MainActivity
    }
    public void initAllVehicules(){
        db = new DatabaseHandler(this);

        List<Annonce> annonces = db.getAllAnnonces();

        LinearLayout vh = findViewById(R.id.vh);
        vh.removeAllViews(); // Supprimer toutes les vues précédentes avant d'ajouter les nouvelles annonces

        for (Annonce annonce:annonces) {
            Log.d("annonce",annonce.getBoite());
        }

        if(!annonces.isEmpty()) {
            for (Annonce annonce : annonces) {
                // Création d'une nouvelle instance de View en utilisant item_layout.xml
                View itemView = getLayoutInflater().inflate(R.layout.list_vehicules, null);

                // Trouver les TextViews dans l'itemView
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

                // Définir le titre et le prix à partir de l'objet PopularDomain
                int drawableResourceId = getResources().getIdentifier(annonce.getPhotoUrl(), "drawable", getPackageName());
                // Mettre l'image dans l'ImageView
                photo.setImageResource(drawableResourceId);
                marque.setText(annonce.getMarque());
                model.setText(annonce.getModele());
                annee.setText(String.valueOf(annonce.getAnnee()));
                priceTextView.setText(String.valueOf(annonce.getPrix()) + "€");

                kelo.setText(String.valueOf(annonce.getKilometrage())+"KM");
                carburant.setText(annonce.getEnergie());
                moteur.setText(annonce.getMoteur());
                boite.setText(annonce.getBoite());
                adresse.setText(annonce.getAdresse());
                time.setText(annonce.getDateCreation());

                //methode d'appelle
                TextView appeler = itemView.findViewById(R.id.appeler);
                appeler.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText( MainActivity.this, "APPELER", Toast.LENGTH_SHORT).show();

                    }
                });

                //methode de message
                TextView message = itemView.findViewById(R.id.message);
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText( MainActivity.this, "MESSAGE", Toast.LENGTH_SHORT).show();
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
                        intent.putExtra("objet",annonce);
                        // Ajoutez d'autres données si nécessaire
                        startActivity(intent);
                    }
                });


                vh.addView(itemView);
            }
        }
    }

    public void favoris(){
        LinearLayout favoris = findViewById(R.id.favoris);

        favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Favoris.class);
                startActivity(intent);
            }
        });

    }
    public void annonce(){
        LinearLayout annonce = findViewById(R.id.annonce);

        annonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddAnnonce.class);
                startActivity(intent);
            }
        });

    }
    public void profile(){
        LinearLayout profile = findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,registerActivity2.class);
                startActivity(intent);
            }
        });

    }

    public void appeler(){

    }
}