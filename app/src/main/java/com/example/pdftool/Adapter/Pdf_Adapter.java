package com.example.pdftool.Adapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pdftool.Activity.OnPdfSelect;
import com.example.pdftool.Holder.Pdf_Holder;
import com.example.pdftool.R;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class Pdf_Adapter extends RecyclerView.Adapter<Pdf_Holder> implements Filterable{
    private final Context context;
    public List<File> pdfs;
    private final OnPdfSelect select;
    List<File> searched;
    private TextView Convert,Delete,Share,Info;
    public Pdf_Adapter(Context context, List<File> pdfs,OnPdfSelect select) {
        this.context = context;
        this.pdfs = pdfs;
        this.select=select;
        this.searched=new ArrayList<>(pdfs);
    }
    @NonNull
    @NotNull
    @Override
    public Pdf_Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new Pdf_Holder(LayoutInflater.from(context).inflate(R.layout.pdf_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull Pdf_Holder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog3= builder.create();
        String path=pdfs.get(position).getAbsolutePath();
        String word="sdcard1";
        if (path.toLowerCase().contains(word.toLowerCase())){
            holder.sdcard.setImageResource(R.drawable.ic_sd_card);
        }
        else {
            holder.sdcard.setImageResource(R.drawable.ic_sd_card1);
        }
        holder.pdf_name.setText(pdfs.get(position).getName());
        holder.pdf_name.setSelected(true);
        holder.pdf_size.setText(get_actual_size(pdfs.get(position).length()));
        holder.option_item.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.option_item);
            //inflating menu from xml resource
            popup.inflate(R.menu.item_option);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.share:
                        Uri uri= Uri.parse(pdfs.get(position).getAbsolutePath());
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("application/pdf");
                        intent.putExtra(Intent.EXTRA_STREAM,uri);
                        context.startActivity(Intent.createChooser(intent,"Share via"));
                        return true;
                    case R.id.convert:
                        return true;
                    case R.id.info:
                        try {
                            Toast.makeText(context, "Path is :-"+pdfs.get(position).getCanonicalPath(),
                                    Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    case R.id.delete:
                        AlertDialog.Builder builder1=new AlertDialog.Builder(context);
                        builder1.setTitle("Delete pdf");
                        builder1.setMessage("It will deleted permanently\nwill you want to proceed?");
                        builder1.setPositiveButton("OK", (dialog1, which) -> {
                            File file= null;
                            try {
                                file = new File(pdfs.get(position).getCanonicalPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            assert file != null;
                            boolean val=file.delete();
                            if (val){
                                pdfs.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, "Unable to delete", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("CANCEL", (dialog12, which) -> dialog12.dismiss()).show();
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
        holder.container.setOnClickListener(v -> {
            select.onPdfSelect(pdfs.get(position));
        });
    }
    private String get_actual_size(long size) {
        String app_size;
        if (size<1024){
            app_size=String.format(context.getString(R.string.app_size_b),(double)size);
        }
        else if (size<Math.pow(1024,2)){
            app_size=String.format(context.getString(R.string.app_size_kib),(double)size/1024);
        }
        else if (size<Math.pow(1024,3)){
            app_size=String.format(context.getString(R.string.app_size_mib),(double)size/Math.pow(1024,2));
        }
        else{
            app_size=String.format(context.getString(R.string.app_size_gib),(double)size/Math.pow(1023,3));
        }
        return app_size;
    }
    @Override
    public int getItemCount() {
        return pdfs.size();
    }
    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<File> files=new ArrayList<>();
            if (constraint.toString().isEmpty()){
                files.addAll(searched);
            }
            else {
                for (File file:searched){
                    if (file.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        files.add(file);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=files;
            return filterResults;
        }
        //run on ui thread.
        @SuppressLint("NotifyDataSetChanged")
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pdfs.clear();
            pdfs.addAll((Collection<? extends File>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
