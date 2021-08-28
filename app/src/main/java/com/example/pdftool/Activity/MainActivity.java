package com.example.pdftool.Activity;
import static java.util.Objects.requireNonNull;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.pdftool.Adapter.Pdf_Adapter;
import com.example.pdftool.R;
import com.example.pdftool.databinding.ActivityMainBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPdfSelect {
    ActivityMainBinding binding;
    Pdf_Adapter adapter;
    ProgressDialog dialog;
    private final String[] permission ={"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog=new ProgressDialog(MainActivity.this);
        dialog.setTitle("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        TakePermission();
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new GridLayoutManager(this,1));
        binding.scrollView.setRecyclerView(binding.recyclerview);
    }
    private void TakePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Arrays.toString(permission)) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,permission,31);
        }
        else {
            DisplayPdfs();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 31 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            DisplayPdfs();
        }
        else {
            Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }
    public ArrayList<File> findPdf(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null){
            for (File file1 : requireNonNull(files)) {
                if (file1.isDirectory() && !file1.isHidden()) {
                    arrayList.addAll(findPdf(file1));
                } else {
                    if (file1.getName().endsWith(".pdf")) {
                        arrayList.add(file1);
                    }
                }
            }
    }
        return arrayList;
    }
    private void DisplayPdfs() {
        dialog.show();
        List<File> pdfFiles = new ArrayList<>(findPdf(Environment.getExternalStorageDirectory()));
        try {
            String path="/storage/sdcard1/";
            if(path != null){
                List<File> arrayList1=new ArrayList<>(findPdf(new File(path)));
                pdfFiles.addAll(arrayList1);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        adapter = new Pdf_Adapter(this, pdfFiles, this);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);
        dialog.dismiss();
    }
    @Override
    public void onPdfSelect(File file) {
        startActivity(new Intent(MainActivity.this,ViewPdf.class).putExtra("path",
                file.getAbsolutePath()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView  searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @SuppressWarnings("all")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.create:
                startActivity(new Intent(MainActivity.this,create_pdf.class));
                break;
        }
        return true;
    }
}