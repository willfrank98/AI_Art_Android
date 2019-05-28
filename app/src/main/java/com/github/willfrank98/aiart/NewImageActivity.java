package com.github.willfrank98.aiart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);

        Intent intent = getIntent();
        String shape = intent.getStringExtra("shape");
        ((TextView)findViewById(R.id.imageType)).setText("New " + shape + " Image");
    }
}
