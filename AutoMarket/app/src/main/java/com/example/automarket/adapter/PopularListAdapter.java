package com.example.automarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.automarket.CallService;
import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.DetailActivity;
import com.example.automarket.MainActivity;
import com.example.automarket.MessageService;
import com.example.automarket.Model.Annonce;
import com.example.automarket.Model.User;
import com.example.automarket.R;
import com.example.automarket.domain.PopularDomain;

import java.util.ArrayList;

public class PopularListAdapter extends RecyclerView.Adapter<PopularListAdapter.Viewholder> {


    ArrayList<PopularDomain> items;

    Context context;

    private DatabaseHandler db;


    public PopularListAdapter(ArrayList<PopularDomain> items, DatabaseHandler db,Context context) {
        this.items = items;
        this.db = db;
        this.context = context;
    }

    @NonNull
    @Override
    public PopularListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop_list, parent, false);
        final Viewholder holder = new Viewholder(inflate);

        // Trouver les vues à l'intérieur de l'élément de vue
        TextView appeler = holder.itemView.findViewById(R.id.appeler_popular);
        TextView message = holder.itemView.findViewById(R.id.message_popular);


        appeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accès à l'élément de données associé à cet élément de vue
                int position = holder.getAdapterPosition();
                PopularDomain item = items.get(position);
                Annonce annonce = item.getAnnonce();

                User userAnnonce = db.getUser(annonce.getUserId());

                String phoneNumber = userAnnonce.getPhone(); // Le numéro de téléphone du client
                Intent callServiceIntent = new Intent(context, CallService.class);
                callServiceIntent.putExtra("phone_number", phoneNumber);
                context.startService(callServiceIntent);

            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accès à l'élément de données associé à cet élément de vue
                int position = holder.getAdapterPosition();
                PopularDomain item = items.get(position);
                Annonce annonce = item.getAnnonce();

                User userAnnonce = db.getUser(annonce.getUserId());


                String phoneNumber = userAnnonce.getPhone(); // Le numéro de téléphone du client
                String messageText = "Bonjour, je suis intéressé par votre annonce pour le Vehicule "+annonce.getMarque()+" "+annonce.getModele()+" "+annonce.getAnnee()+"."; // Le message à envoyer
                Intent messageServiceIntent = new Intent(context, MessageService.class);
                messageServiceIntent.putExtra("phone_number", phoneNumber);
                messageServiceIntent.putExtra("message", messageText);
                context.startService(messageServiceIntent);
                }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull PopularListAdapter.Viewholder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.feeTxt.setText(String.valueOf(items.get(position).getPrice())+"€"); // Convert int to String
        holder.scoreTxt.setText(String.valueOf(items.get(position).getNbrVue()));
        PopularDomain item = items.get(position);
        Annonce annonce = item.getAnnonce();


        if (annonce.getPhotoUrl() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(annonce.getPhotoUrl()))
                    .placeholder(R.drawable.nocar)
                    //.transform(new GranularRoundedCorners(0,0,0,0))
                    .error(R.drawable.vehicule2)
                    .into(holder.pic);
        } else {
            holder.pic.setImageResource(R.drawable.nocar);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("objet",items.get(position).getAnnonce());
            db.addVue(items.get(position).getAnnonce());
            holder.itemView.getContext().startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends  RecyclerView.ViewHolder{
        TextView titleTxt,feeTxt,scoreTxt;
        ImageView pic;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            feeTxt = itemView.findViewById(R.id.feetxt);
            scoreTxt = itemView.findViewById(R.id.scorText);
            pic = itemView.findViewById(R.id.pic);

        }
    }
}
