package com.github.willfrank98.aiart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class DisplayImageActivity extends AppCompatActivity {

//    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        Intent intent = getIntent();
        Bitmap image = ((Intent) intent).getParcelableExtra("bitmap");

        ImageView view = findViewById(R.id.imageViewFinal);
        view.setImageBitmap(image);
    }
}
