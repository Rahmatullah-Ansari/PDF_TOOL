package com.example.pdftool.Activity;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pdftool.R;
import com.example.pdftool.databinding.ActivitySplashBinding;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import java.util.Objects;
public class Splash extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        binding.imageview.startAnimation(animation);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }, 3000);
    }
}