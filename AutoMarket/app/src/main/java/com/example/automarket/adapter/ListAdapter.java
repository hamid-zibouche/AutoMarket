package com.example.automarket.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.automarket.Model.Annonce;
import com.example.automarket.R;
import com.example.automarket.domain.PopularDomain;

import java.util.List;

public class ListAdapter extends ArrayAdapter<PopularDomain> {

    private Context mContext;
    private List<PopularDomain> mItems;

    public ListAdapter(Context context, List<PopularDomain> items) {
        super(context, 0, items);
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.viewholder_pop_list, parent, false);
        }

        PopularDomain currentDomain = mItems.get(position);
        Annonce annonce = currentDomain.getAnnonce();

        TextView title = listItem.findViewById(R.id.titleTxt);
        title.setText(currentDomain.getTitle());

        TextView price = listItem.findViewById(R.id.feetxt);
        price.setText(String.valueOf(currentDomain.getPrice()) + "€");

        TextView scoreTxt = listItem.findViewById(R.id.scorText);
        scoreTxt.setText(String.valueOf(currentDomain.getNbrVue()));

        ImageView photo = listItem.findViewById(R.id.pic);

        if (annonce.getPhotoUrl() != null) {
            Glide.with(mContext)
                    .load(Uri.parse(annonce.getPhotoUrl()))
                    .placeholder(R.drawable.nocar)
                    .error(R.drawable.nocar)
                    .into(photo);
        } else {
            photo.setImageResource(R.drawable.nocar);
        }



        // Ajoutez d'autres TextViews pour les autres informations

        return listItem;
    }
}

