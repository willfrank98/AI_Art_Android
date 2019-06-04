package com.github.willfrank98.aiart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Circle implements IShape {
    private int color;
    private double fitness;
    private Point center;
    private int r;

    public Circle(int height, int width, int minLength, int maxLength) {
        CustomRandom rand = new CustomRandom();
        int tempX1 = rand.nextInt(minLength / 2, width - minLength / 2);
        int tempY1 = rand.nextInt(minLength / 2, height - minLength / 2);
        this.center = new Point(tempX1, tempY1);

        this.r = rand.nextInt(minLength, maxLength);

        this.color = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    @Override
    public void SetFitness(double newFitness) {
        this.fitness = newFitness;
    }

    @Override
    public double GetFitness() {
        return this.fitness;
    }

    @Override
    public List<Point> IteratePoints(int granularity) {
        List<Point> points = new ArrayList<Point>();
        int x = this.center.x;
        int y = this.center.y;
        int r = this.r;
        for (int i = y - r; i < y + r; i++) {
            for (int j = x; (j - x) * (j - x) + (i - y) * (i - y) <= r * r; j -= granularity) {
                points.add(new Point(j, i));
            }
            for (int j = x + 1; (j - x) * (j - x) + (i - y) * (i - y) <= r * r; j += granularity) {
                points.add(new Point(j, i));
            }
        }

        return points;
    }

    @Override
    public int GetColor() {
        return this.color;
    }

    @Override
    public Canvas AddShape(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.color);
        canvas.drawCircle(this.center.x, this.center.y, this.r, paint);
        return canvas;
    }

    @Override
    public int compareTo(IShape other) {
        if (this.fitness == other.GetFitness()) {
            return 0;
        } else if (this.fitness > other.GetFitness()) {
            return 1;
        } else {
            return -1;
        }
    }
}
