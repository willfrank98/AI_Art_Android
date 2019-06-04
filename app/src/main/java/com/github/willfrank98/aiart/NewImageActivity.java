package com.github.willfrank98.aiart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class NewImageActivity extends AppCompatActivity {

    private Bitmap image;
    private String shape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);

        Intent intent = getIntent();
        String shape = intent.getStringExtra("shape");
        ((TextView)findViewById(R.id.imageType)).setText("New " + shape + " Image");
        this.shape = shape;

        Uri imageUri = intent.getData();
        InputStream imageStream = null;
        try {
            assert imageUri != null;
            imageStream = getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.image = BitmapFactory.decodeStream(imageStream);

        // set click listener for generate button
        Button button = (Button) findViewById(R.id.buttonGenerate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startNewImageProcess();
            }
        });
    }

    private void startNewImageProcess() {
        int numGen = Integer.parseInt(((TextView) findViewById(R.id.editTextGenNum)).getText().toString());
        int numDraw = Integer.parseInt(((TextView) findViewById(R.id.editTextDrawNum)).getText().toString());
        int granularity = Integer.parseInt(((TextView) findViewById(R.id.editTextGranularity)).getText().toString());
        int minLen = Integer.parseInt(((TextView) findViewById(R.id.editTextMinLength)).getText().toString());
        int maxLen = Integer.parseInt(((TextView) findViewById(R.id.editTextMaxLength)).getText().toString());

        ImageRepresentation imageRep = new ImageRepresentation(image);
        imageRep.NewBatch(numGen, this.shape, minLen, maxLen);
        imageRep.EvaluateFitness(granularity);
        imageRep.Draw(numDraw, this.shape);
    }
}
