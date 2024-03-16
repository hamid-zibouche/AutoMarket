package com.example.automarket;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import com.example.automarket.Model.User;
import com.example.automarket.R;

import java.util.List;

public class VehiculesFragment extends Fragment {

    private DatabaseHandler db;
    private LinearLayout vh;
    private boolean isSearchMode = false;

    public VehiculesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicules, container, false);
        db = new DatabaseHandler(requireContext());
        vh = view.findViewById(R.id.vh);
        initAllVehicules();
        return view;
    }

    private void initAllVehicules() {
        List<Annonce> annonces = db.getAllAnnonces();
        displayAnnonces(annonces);
    }

    private void displayAnnonces(List<Annonce> annonces) {
        vh.removeAllViews();

        TextView countText = new TextView(requireContext());
        countText.setText("Nombre de résultats : " + annonces.size());
        countText.setBackgroundResource(R.drawable.resultat_background); // Utiliser le drawable personnalisé
        countText.setPadding(16, 8, 16, 8); // Padding pour l'esthétique

        countText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        vh.addView(countText);
        for (Annonce annonce : annonces) {
            View itemView = getLayoutInflater().inflate(R.layout.list_vehicules, vh, false);
            bindAnnonceToView(itemView, annonce);
            vh.addView(itemView);
        }
    }

    private void bindAnnonceToView(View view, Annonce annonce) {
        TextView marque = view.findViewById(R.id.marque);
        TextView model = view.findViewById(R.id.model);
        TextView annee = view.findViewById(R.id.annee);
        TextView priceTextView = view.findViewById(R.id.feetxt);
        ImageView photo = view.findViewById(R.id.pic);
        TextView kelo = view.findViewById(R.id.kelo);
        TextView carburant = view.findViewById(R.id.carburant);
        TextView moteur = view.findViewById(R.id.moteur);
        TextView boite = view.findViewById(R.id.boite);
        TextView adresse = view.findViewById(R.id.adresse);
        TextView time = view.findViewById(R.id.time);
        TextView nbrvue = view.findViewById(R.id.vueText);

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

        try {
            int userId = annonce.getUserId();
            User userAnnonce = db.getUser(userId);

            LinearLayout appeler = view.findViewById(R.id.appeler);
            appeler.setOnClickListener(v -> {
                String phoneNumber = userAnnonce.getPhone();
                Intent callServiceIntent = new Intent(requireContext(), CallService.class);
                callServiceIntent.putExtra("phone_number", phoneNumber);
                requireContext().startService(callServiceIntent);
            });

            LinearLayout message = view.findViewById(R.id.message);
            message.setOnClickListener(v -> {
                String phoneNumber = userAnnonce.getPhone();
                String messageText = "Bonjour, je suis intéressé par votre annonce pour le Vehicule " + annonce.getMarque() + " " + annonce.getModele() + " " + annonce.getAnnee() + ".";
                Intent messageServiceIntent = new Intent(requireContext(), MessageService.class);
                messageServiceIntent.putExtra("phone_number", phoneNumber);
                messageServiceIntent.putExtra("message", messageText);
                requireContext().startService(messageServiceIntent);
            });

        } catch (Exception e) {
            Log.e(TAG, "Error getting user from database", e);
        }

        if (annonce.getPhotoUrl() != null) {
            Glide.with(requireContext())
                    .load(Uri.parse(annonce.getPhotoUrl()))
                    .placeholder(R.drawable.nocar)
                    .error(R.drawable.nocar)
                    .into(photo);
        } else {
            photo.setImageResource(R.drawable.nocar);
        }

        view.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra("title", annonce.getMarque());
            intent.putExtra("price", annonce.getPrix());
            db.addVue(annonce);
            intent.putExtra("objet", annonce);
            startActivity(intent);
        });
    }

    public void searchAnnonces(String rechercheText) {
        List<Annonce> annonces = db.rechercheAnnonces(rechercheText);
        if (annonces.isEmpty()) {
            Toast.makeText(requireContext(), "Aucun résultat trouvé pour : " + rechercheText, Toast.LENGTH_SHORT).show();
        } else {
            displayAnnonces(annonces);
        }
    }

    public void exitSearchMode() {
        isSearchMode = false;
        initAllVehicules();
    }

    public boolean isSearchMode() {
        return isSearchMode;
    }
}
