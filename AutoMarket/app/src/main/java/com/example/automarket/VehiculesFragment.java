package com.example.automarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.Annonce;
import com.example.automarket.R;

import java.util.List;

public class VehiculesFragment extends Fragment {

    private DatabaseHandler db;

    public VehiculesFragment() {
        // Required empty public constructor
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicules, container, false);

        db = new DatabaseHandler(getActivity());

        List<Annonce> annonces = db.getAllAnnonces();

        LinearLayout vh = view.findViewById(R.id.vh);


        // Le reste de votre logique ici...
        for (Annonce annonce : annonces) {
            View itemView = getLayoutInflater().inflate(R.layout.list_vehicules, vh, false);

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
            TextView nbrvue = itemView.findViewById(R.id.vueText);

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
            nbrvue.setText(String.valueOf(annonce.getNbrVue()));

            Log.d("image", annonce.getPhotoUrl());
            // Charger l'image à partir de l'URI en utilisant Glide
            if (annonce.getPhotoUrl() != null) {
                Glide.with(requireContext())
                        .load(Uri.parse(annonce.getPhotoUrl()))
                        .placeholder(R.drawable.nocar) // Image de placeholder
                        .error(R.drawable.nocar) // Image à afficher en cas d'erreur
                        .into(photo);
            } else {
                // Si l'URI est null, vous pouvez afficher une image par défaut
                photo.setImageResource(R.drawable.nocar);
            }

            //methode d'appelle
            LinearLayout appeler = itemView.findViewById(R.id.appeler);
            appeler.setOnClickListener(v -> Toast.makeText(requireContext(), "APPELER", Toast.LENGTH_SHORT).show());

            //methode de message
            LinearLayout message = itemView.findViewById(R.id.message);
            message.setOnClickListener(v -> Toast.makeText(requireContext(), "MESSAGE", Toast.LENGTH_SHORT).show());

            // Ajouter un OnClickListener à chaque itemView
            itemView.setOnClickListener(v -> {
                // Lorsque l'utilisateur clique, ouvrir la DetailView avec les données de l'élément
                Intent intent = new Intent(requireContext(), DetailActivity.class);
                intent.putExtra("title", annonce.getMarque());
                intent.putExtra("price", annonce.getPrix());

                db.addVue(annonce);
                intent.putExtra("objet", annonce);

                startActivity(intent);

            });

            // Ajoutez chaque itemView au LinearLayout
            vh.addView(itemView);
        }


        return view;
    }

    private void initAllVehicules(LinearLayout vh) {
        db = new DatabaseHandler(requireContext());

        List<Annonce> annonces = db.getAllAnnonces();


    }
}