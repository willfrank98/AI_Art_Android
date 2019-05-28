package com.github.willfrank98.aiart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFAB();
    }

    private void initFAB() {
        SpeedDialView speedDialView = findViewById(R.id.speedDial);
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.buttonSquare, R.drawable.ic_square)
                        .setLabel("Square Image")
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.buttonTriangle, R.drawable.ic_triangle)
                        .setLabel("Triangle Image")
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.buttonCircle, R.drawable.ic_circle)
                        .setLabel("Circle Image")
                        .create()
        );

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                //create intent here and switch to New Image view
                switch (speedDialActionItem.getId()) {
                    case R.id.buttonSquare:
                        makeNewImage("Square");
                        return false;
                    case R.id.buttonCircle:
                        makeNewImage("Circle");
                        return false;
                    case R.id.buttonTriangle:
                        makeNewImage("Triangle");
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    private void makeNewImage(String shape) {
        Intent intent = new Intent(this, NewImageActivity.class);
        intent.putExtra("shape", shape);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
