package com.example.pdftool.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.example.pdftool.databinding.ActivityShowImageBinding;
public class show_image extends AppCompatActivity {
    ActivityShowImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShowImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Uri uri=Uri.parse(getIntent().getExtras().get("path").toString());
        String name=getIntent().getExtras().get("name").toString();
        setTitle(name);
        binding.showImage.setImageURI(uri);
        binding.shareCard.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM,uri);
            startActivity(new Intent(Intent.createChooser(intent,"Choose a platform to share")));
        });
    }
}