package com.github.willfrank98.aiart;

import android.arch.core.util.Function;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Square implements IShape {
    private double fitness;
    private int color;
    private Point[] points;

    public Square(int height, int width, int minLength, int maxLength) {
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

        rotation -= (Math.PI / 2) % Math.PI;

        shiftX = Math.sin(rotation) * sideLength;
        shiftY = Math.cos(rotation) * sideLength;
        int tempX3 = tempX1 + (int) shiftX;
        int tempY3 = tempY1 + (int) shiftY;
        Point p3 = new Point(tempX3, tempY3);

        Point p4 = new Point(tempX2 + (int) shiftX, tempY2 + (int) shiftY);

        this.points = new Point[]{p1, p2, p4, p3}; //this order is needed for drawing purposes

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

        Point[] newPoints = new Point[]{this.points[0], this.points[1], this.points[3]};
        for (Point point : IterateTriangle(newPoints, granularity)) {
            points.add(point);
        }

        newPoints = new Point[]{this.points[0], this.points[2], this.points[3]};
        for (Point point : IterateTriangle(newPoints, granularity)) {
            points.add(point);
        }

        return points;
    }

    private List<Point> IterateTriangle(Point[] points, int granularity) {
        List<Point> returnList = new ArrayList<Point>();

        Point pt1 = points[0];
        Point pt2 = points[1];
        Point pt3 = points[2];
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
    public Canvas AddShape(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.color);
        Rect rect = MakeRect();
        return canvas;
    }

    private Rect MakeRect() {
        int[] xs = {this.points[0].x, this.points[1].x, this.points[2].x, this.points[3].x};
        int[] ys = {this.points[0].y, this.points[1].y, this.points[2].y, this.points[3].y};
        Arrays.sort(xs);
        Arrays.sort(ys);

        // ...
    }

    @Override
    public int compareTo(IShape other) {
        return 0;
    }
}
