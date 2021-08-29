package com.example.pdftool.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.pdftool.databinding.ActivityViewPdfBinding;
import java.io.File;
import java.util.Objects;

public class ViewPdf extends AppCompatActivity {
    public ActivityViewPdfBinding binding;
    private String file_path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        file_path=getIntent().getStringExtra("path");
        File file=new File(file_path);
        Objects.requireNonNull(getSupportActionBar()).setTitle(file.getName());
        binding.pdfview
                .fromFile(file)
                .defaultPage(0)
                .enableSwipe(true)
                .load();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}