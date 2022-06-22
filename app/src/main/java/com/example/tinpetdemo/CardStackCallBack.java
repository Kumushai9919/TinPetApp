package com.example.tinpetdemo;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class CardStackCallBack extends DiffUtil.Callback {

    private List<Pets> old, baru;

    public CardStackCallBack(List<Pets> old, List<Pets> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }



    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getPetImage() == baru.get(newItemPosition).getPetImage();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}
