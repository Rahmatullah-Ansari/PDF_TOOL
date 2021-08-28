package com.example.pdftool.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;
import com.example.pdftool.Adapter.New_pdf_adapter;
import com.example.pdftool.Holder.New_pdf_holder;
import com.example.pdftool.R;
import com.example.pdftool.databinding.ActivityCreatePdfBinding;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
public class create_pdf extends AppCompatActivity {
    ActivityCreatePdfBinding binding;
    int flag=0;
    ArrayList<New_pdf_holder> arrayList;
    New_pdf_adapter adapter;
    String mUri;
    String [] permission={"android.permission.CAMERA","android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreatePdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.addNew.setOnClickListener(v ->{
            if (flag==0){
                binding.addNew.setImageResource(R.drawable.ic_baseline_clear_24);
                binding.gallery.setVisibility(View.VISIBLE);
                binding.camera.setVisibility(View.VISIBLE);
                flag=1;
            }
            else if (flag==1){
                binding.addNew.setImageResource(R.drawable.ic_baseline_add_24);
                binding.gallery.setVisibility(View.GONE);
                binding.camera.setVisibility(View.GONE);
                flag=0;
            }
        });
        arrayList=new ArrayList<>();
        adapter=new New_pdf_adapter(create_pdf.this,arrayList);
        binding.pdfRecyclerView.setHasFixedSize(true);
        binding.pdfRecyclerView.setLayoutManager(new GridLayoutManager(create_pdf.this,2));
        binding.pdfRecyclerView.setAdapter(adapter);
        binding.createPdfScroll.setRecyclerView(binding.pdfRecyclerView);
        binding.gallery.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            startActivityForResult(Intent.createChooser(intent,"select images"),31);
        });
        binding.camera.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(create_pdf.this, Arrays.toString(permission)) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(create_pdf.this, new String[]{Manifest.permission.CAMERA},21);
            }
            else {
                create_folder();
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),38);
            }
        });
    }

    private void create_folder() {
        String folder= Environment.getExternalStorageDirectory()+File.separator+"Pdf Tool";
        File file=new File(folder);
        if (!file.exists() && !file.isDirectory()){
            file.mkdirs();
        }
        mUri= String.format("%s%sFile - %d.jpg", folder, File.separator, System.currentTimeMillis());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 21 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            create_folder();
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),38);
        }
        else {
            Toast.makeText(create_pdf.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 31){
            try {
                if (Objects.requireNonNull(data).getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    New_pdf_holder holder;
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        String size = get_actual_size(new File(Objects.requireNonNull(RealPathUtil.getRealPathFromURI_API19(this, uri))).length());
                        String name = get_file_name(uri);
                        holder = new New_pdf_holder(size, name, uri);
                        arrayList.add(holder);
                        binding.pdfRecyclerView.smoothScrollToPosition(i);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(create_pdf.this, "Select an image please", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == 38){
                File file=new File(mUri);
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    Bundle bundle=data.getExtras();
                    Bitmap bitmap=bundle.getParcelable("data");
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == 14){

            }
        }
    }
    /*private void perform_crop() {
        try {
            Intent crop=new Intent("com.android.camera.action.CROP");
            crop.setDataAndType(Uri.parse(mUri),"image/*");
            crop.putExtra("crop",true);
            crop.putExtra("aspectX",3);
            crop.putExtra("aspectY",3);
            crop.putExtra("outputX",256);
            crop.putExtra("outputY",256);
            crop.putExtra("scaleUpIfneeded",true);
            crop.putExtra("return-data",true);
            startActivityForResult(crop,14);
        }catch (ActivityNotFoundException e){
            Toast.makeText(create_pdf.this, "Can't crop due to :- "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/
    private String get_file_name(Uri uri) {
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String name=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        cursor.close();
        return name;
    }
    private String get_actual_size(long size) {
        String app_size;
        if (size<1024){
            app_size=String.format(getString(R.string.app_size_b),(double)size);
        }
        else if (size<Math.pow(1024,2)){
            app_size=String.format(getString(R.string.app_size_kib),(double)size/1024);
        }
        else if (size<Math.pow(1024,3)){
            app_size=String.format(getString(R.string.app_size_mib),(double)size/Math.pow(1024,2));
        }
        else{
            app_size=String.format(getString(R.string.app_size_gib),(double)size/Math.pow(1023,3));
        }
        return app_size;
    }
}