
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

        db = new DatabaseHandler(this);

        List<Annonce> annonces = db.getAllAnnonces();


        for (Annonce annonce:annonces) {
            Log.d("annonce",annonce.getBoite());
        }

        if(!annonces.isEmpty()) {
            for (Annonce annonce : annonces) {
                // Création d'une nouvelle instance de View en utilisant item_layout.xml
                View itemView = getLayoutInflater().inflate(R.layout.list_vehicules, null);

                // Trouver les TextViews dans l'itemView
                TextView titleTextView = itemView.findViewById(R.id.titleTxt);
                TextView priceTextView = itemView.findViewById(R.id.feetxt);
                ImageView photo = itemView.findViewById(R.id.pic);

                // Définir le titre et le prix à partir de l'objet PopularDomain
                titleTextView.setText(annonce.getMarque());
                priceTextView.setText(String.valueOf(annonce.getPrix()) + "€");
                int drawableResourceId = getResources().getIdentifier(annonce.getPhotoUrl(), "drawable", getPackageName());

                // Mettre l'image dans l'ImageView
                photo.setImageResource(drawableResourceId);

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

                LinearLayout vh = findViewById(R.id.vh);
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
                Intent intent = new Intent(MainActivity.this,Profile.class);
                startActivity(intent);
            }
        });

    }
}