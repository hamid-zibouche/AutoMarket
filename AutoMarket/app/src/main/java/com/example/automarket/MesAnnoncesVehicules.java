package com.example.automarket;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.Annonce;
import com.example.automarket.Model.User;
import com.example.automarket.Utils.Utils;

import org.w3c.dom.Text;

import java.util.List;


public class MesAnnoncesVehicules extends Fragment {

    private DatabaseHandler db;
    private LinearLayout vh;
    private boolean isSearchMode = false;
    SharedPreferences sharedPreferences;
    public MesAnnoncesVehicules() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mes_annonces_vehicules, container, false);
        db = new DatabaseHandler(requireContext());
        vh = view.findViewById(R.id.vhh);

        initAllVehicules();
        return view;
    }

    private void initAllVehicules() {

        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int idUser = sharedPreferences.getInt(Utils.KEY_ID, 0);
        Log.d("idddd",String.valueOf(idUser));
        List<Annonce> annonces = db.getAllAnnoncesForUser(idUser);
        displayAnnonces(annonces);
    }

    private void displayAnnonces(List<Annonce> annonces) {
        vh.removeAllViews();

        for (Annonce annonce : annonces) {
            View itemView = getLayoutInflater().inflate(R.layout.list_mes_annonces_vehicules, vh, false);
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

        TextView supprimer = view.findViewById(R.id.supprimer);

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

        supprimer.setOnClickListener(v -> {
            showConfirmationDialog(annonce);
        });
    }

    private void showConfirmationDialog(Annonce annonce) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmation de suppression");
        builder.setMessage("Voulez-vous vraiment supprimer cette annonce ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Supprimer l'annonce ici
                deleteAnnonce(annonce);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ne rien faire, simplement fermer la boîte de dialogue
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteAnnonce(Annonce annonce) {
        // Supprimer l'annonce de la base de données
        db.deleteAnnonce(annonce.getId());

        // Rafraîchir la liste des annonces après la suppression
        initAllVehicules();

        Toast.makeText(requireContext(), "Annonce supprimée avec succès", Toast.LENGTH_SHORT).show();
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
