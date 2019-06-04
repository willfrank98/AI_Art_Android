package com.github.willfrank98.aiart;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.List;

public interface IShape extends Comparable<IShape> {
    void SetFitness(double newFitness);

    double GetFitness();

    List<Point> IteratePoints(int granularity);

    int GetColor();

    Canvas AddShape(Canvas canvas);

    @Override
    public int compareTo(IShape other);
}
