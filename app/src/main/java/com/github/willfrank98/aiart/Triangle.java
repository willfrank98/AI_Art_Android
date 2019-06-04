package com.github.willfrank98.aiart;

import android.arch.core.util.Function;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Triangle implements IShape {

    private Point[] points;
    private int color;
    private double fitness;

    public Triangle(int height, int width, int minLength, int maxLength) {
        CustomRandom rand = new CustomRandom();
        int tempX1 = rand.nextInt(minLength / 2, width - minLength / 2);
        int tempY1 = rand.nextInt(minLength / 2, height - minLength / 2);
        Point p1 = new Point(tempX1, tempY1);

        int sideLength = rand.nextInt(minLength, maxLength);
        double rotation = rand.nextDouble() * 2 * Math.PI;

        double shiftX = Math.sin(rotation) * sideLength;
        double shiftY = Math.cos(rotation) * sideLength;
        int tempX2 = tempX1 + (int) shiftX;
        int tempY2 = tempY1 + (int) shiftY;
        Point p2 = new Point(tempX2, tempY2);

        rotation -= (Math.PI / 3) % Math.PI;

        shiftX = Math.sin(rotation) * sideLength;
        shiftY = Math.cos(rotation) * sideLength;
        int tempX3 = tempX1 + (int) shiftX;
        int tempY3 = tempY1 + (int) shiftY;
        Point p3 = new Point(tempX3, tempY3);

        this.points = new Point[]{p1, p2, p3};

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
        List<Point> returnList = new ArrayList<Point>();

        Point pt1 = this.points[0];
        Point pt2 = this.points[1];
        Point pt3 = this.points[2];
        Point tmp;

        if (pt2.x < pt1.x) {
            tmp = pt1;
            pt1 = pt2;
            pt2 = tmp;
        }

        if (pt3.x < pt2.x) {
            tmp = pt2;
            pt2 = pt3;
            pt3 = tmp;

            if (pt2.x < pt1.x) {
                tmp = pt1;
                pt1 = pt2;
                pt2 = tmp;
            }
        }

        final int pt2Y = pt2.y;
        Function<Integer, Double> baseFunc = CreateFunc(pt1, pt3);
        Function<Integer, Double> line1Func = pt1.x == pt2.x ? (x -> (double) pt2Y) : CreateFunc(pt1, pt2);

        for (int x = pt1.x; x < pt2.x; x += granularity) {
            int[] minMax = GetRange(line1Func.apply(x), baseFunc.apply(x));
            int minY = minMax[0];
            int maxY = minMax[1];

            for (int y = minY; y <= maxY; y += granularity) {
                returnList.add(new Point(x, y));
            }
        }

        Function<Integer, Double> line2Func = pt2.x == pt3.x ? (x -> (double) pt2Y) : CreateFunc(pt2, pt3);

        for (int x = pt2.x; x <= pt3.x; x += granularity) {
            int[] minMax = GetRange(line2Func.apply(x), baseFunc.apply(x));
            int minY = minMax[0];
            int maxY = minMax[1];

            for (int y = minY; y <= maxY; y += granularity) {
                returnList.add(new Point(x, y));
            }
        }

        return returnList;
    }

    private int[] GetRange(double y1, double y2) {
        if (y1 < y2) {
            return new int[]{(int) Math.ceil(y1), (int) Math.floor(y2)};
        }

        return new int[]{(int) Math.floor(y1), (int) Math.ceil(y2)};
    }

    private Function<Integer, Double> CreateFunc(Point pt1, Point pt2) {
        int y0 = pt1.y;

        if (y0 == pt2.y) {
            return x -> (double) y0;
        }

        double m = (double) (pt2.y - y0) / (pt2.x - pt1.x);

        return x -> m * (x - pt1.x) + y0;
    }

    @Override
    public int GetColor() {
        return this.color;
    }

    @Override
    public Canvas AddShape(Canvas old) {
        return null;
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
