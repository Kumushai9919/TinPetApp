package com.example.tinpetdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<Pets> items;

    public CardStackAdapter(List<Pets> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, age, type;
        String petId;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            age = itemView.findViewById(R.id.item_age);
            type = itemView.findViewById(R.id.item_city);
        }

        void setData(Pets data) {
            image.setImageBitmap(data.getPetImage());
//            Picasso.get()
//                    .load(data.getPetImage())
//                    .fit()
//                    .centerCrop()
//                    .into(image);
            name.setText(data.getPetName());
            age.setText(data.getPetAge());
            type.setText(data.getPetType());
            petId = data.petId;
        }
    }

    public List<Pets> getItems() {
        return items;
    }

    public void setItems(List<Pets> items) {
        this.items = items;
    }

}
