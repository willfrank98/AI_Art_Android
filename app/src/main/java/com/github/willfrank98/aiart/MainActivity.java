package com.github.willfrank98.aiart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

//import android.widget.Button;

//import com.leinardi.android.speeddial.SpeedDialActionItem;
//import com.leinardi.android.speeddial.SpeedDialView;

public class MainActivity extends AppCompatActivity {

    //private String shape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.newImageFAB);
        fab.setOnClickListener(v -> makeNewImage());
    }

//    private void initFAB() {
//        SpeedDialView speedDialView = findViewById(R.id.speedDial);
//        speedDialView.addActionItem(
//                new SpeedDialActionItem.Builder(R.id.buttonSquare, R.drawable.ic_square)
//                        .setLabel("Square Image")
//                        .create()
//        );
//
//        speedDialView.addActionItem(
//                new SpeedDialActionItem.Builder(R.id.buttonTriangle, R.drawable.ic_triangle)
//                        .setLabel("Triangle Image")
//                        .create()
//        );
//
//        speedDialView.addActionItem(
//                new SpeedDialActionItem.Builder(R.id.buttonCircle, R.drawable.ic_circle)
//                        .setLabel("Circle Image")
//                        .create()
//        );
//
//        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
//            @Override
//            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
//                switch (speedDialActionItem.getId()) {
//                    case R.id.buttonSquare:
//                        makeNewImage("Square");
//                        return false;
//                    case R.id.buttonCircle:
//                        makeNewImage("Circle");
//                        return false;
//                    case R.id.buttonTriangle:
//                        makeNewImage("Triangle");
//                        return false;
//                    default:
//                        return false;
//                }
//            }
//        });
//    }

    public static final int PICK_IMAGE = 1;

    private void makeNewImage() {
        //create intent to select new image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            // start NewImageActivity with chosen image and shape
            Intent intent = new Intent(this, NewImageActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
        } else {
            showToast("You haven't picked an image.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
