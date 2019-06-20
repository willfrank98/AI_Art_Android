package com.github.willfrank98.aiart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class NewImageActivity extends AppCompatActivity {

    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);

        Intent intent = getIntent();

        //get selected image as bitmap
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
        Button generateButton = findViewById(R.id.buttonGenerate);
        generateButton.setOnClickListener(v -> startNewImageProcess());

        //click listener for save and clear
        Button saveButton = findViewById(R.id.buttonSave);
        Button clearButton = findViewById(R.id.buttonClear);
        saveButton.setOnClickListener(v -> saveImage());
        clearButton.setOnClickListener(v -> resetActivity());
        //set to invisible
//        saveButton.setVisibility(View.INVISIBLE);
//        clearButton.setVisibility(View.INVISIBLE);

        //setup spinner
        Spinner spinner = findViewById(R.id.shapeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.shapes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void startNewImageProcess() {
        String shape = ((Spinner) findViewById(R.id.shapeSpinner)).getSelectedItem().toString();
        int numGen = Integer.parseInt(((TextView) findViewById(R.id.editTextGenNum)).getText().toString());
        int numDraw = Integer.parseInt(((TextView) findViewById(R.id.editTextDrawNum)).getText().toString());
        int granularity = Integer.parseInt(((TextView) findViewById(R.id.editTextGranularity)).getText().toString());
        int minLen = Integer.parseInt(((TextView) findViewById(R.id.editTextMinLength)).getText().toString());
        int maxLen = Integer.parseInt(((TextView) findViewById(R.id.editTextMaxLength)).getText().toString());

        ImageRepresentation imageRep = new ImageRepresentation(this.image);
        imageRep.NewBatch(numGen, shape, minLen, maxLen);
        imageRep.EvaluateFitness(granularity);
        Bitmap finalImage = imageRep.Draw(numDraw, shape);

        //setup to resize image before drawing
        double ratio = (double) image.getWidth() / image.getHeight();
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        int newWidth = screenSize.x - 24;
        Bitmap scaledImage = Bitmap.createScaledBitmap(finalImage, newWidth, (int) (newWidth * ratio), false);

        //prepare to draw the image to the screen
        Bitmap tempBitmap = Bitmap.createBitmap(scaledImage.getWidth(), scaledImage.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);

        tempCanvas.drawBitmap(scaledImage, 0, 0, null);

        ImageView view = findViewById(R.id.imageViewFinal);
        view.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

        ConstraintLayout bg = findViewById(R.id.layout);
        bg.setBackgroundColor(getColor(R.color.background));

        view.bringToFront();
        view.setVisibility(View.VISIBLE);
    }

    private void resetActivity() {

    }

    private void saveImage() {

    }
}
