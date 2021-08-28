package com.example.pdftool.Holder;

import android.net.Uri;

public class New_pdf_holder {
    private String size,name;
    private Uri path;

    public New_pdf_holder(String size, String name, Uri path) {
        this.size = size;
        this.name = name;
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Uri getPath() {
        return path;
    }
}
