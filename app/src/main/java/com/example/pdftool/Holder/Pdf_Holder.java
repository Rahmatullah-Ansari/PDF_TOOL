package com.example.pdftool.Holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdftool.R;

public class Pdf_Holder extends RecyclerView.ViewHolder {
    public TextView pdf_name,pdf_size;
    public ImageView sdcard;
    public CardView container;
    public ImageButton option_item;
    public Pdf_Holder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
        super(itemView);
        pdf_name=itemView.findViewById(R.id.pdf_name);
        sdcard=itemView.findViewById(R.id.sdcard);
        container=itemView.findViewById(R.id.cardView);
        pdf_size=itemView.findViewById(R.id.size);
        option_item=itemView.findViewById(R.id.option_item);
    }
}
