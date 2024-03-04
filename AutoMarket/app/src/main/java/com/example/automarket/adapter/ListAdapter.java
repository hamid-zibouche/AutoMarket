package com.example.automarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        TextView title = listItem.findViewById(R.id.titleTxt);
        title.setText(currentDomain.getTitle());

        TextView price = listItem.findViewById(R.id.feetxt);
        price.setText(String.valueOf(currentDomain.getPrice()) + "€");

        TextView scoreTxt = listItem.findViewById(R.id.scorText);
        scoreTxt.setText(String.valueOf(currentDomain.getScore()));



        // Ajoutez d'autres TextViews pour les autres informations

        return listItem;
    }
}

