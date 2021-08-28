package com.example.pdftool.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdftool.Activity.show_image;
import com.example.pdftool.Holder.New_pdf_holder;
import com.example.pdftool.R;
import java.util.ArrayList;
public class New_pdf_adapter extends RecyclerView.Adapter<New_pdf_adapter.ViewHolder> {
    private final Context context;
    private final ArrayList<New_pdf_holder> arrayList;
    public New_pdf_adapter(Context context, ArrayList<New_pdf_holder> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public New_pdf_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.new_pdf_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull New_pdf_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.page_index.setText(String.valueOf(position+1));
        holder.page_size.setText(arrayList.get(position).getSize());
        holder.new_page.setImageURI(arrayList.get(position).getPath());
        holder.itemView.setOnClickListener(view -> context.startActivity(new Intent(context, show_image.class).putExtra("path",arrayList.get(position).getPath()).putExtra("name",arrayList.get(position).getName())));
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView new_page;
        TextView page_index,page_size;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            new_page=itemView.findViewById(R.id.new_pdf_page);
            page_index=itemView.findViewById(R.id.page_no);
            page_size=itemView.findViewById(R.id.time);
        }
    }
}
