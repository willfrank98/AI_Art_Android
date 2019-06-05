package com.github.willfrank98.aiart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Square implements IShape {
    private double fitness;
    private int color;
    private int top, left, bottom, right;

    public Square(int height, int width, int minLength, int maxLength) {
        CustomRandom rand = new CustomRandom();

        this.top = rand.nextInt(minLength / 2, width - minLength / 2);
        this.left = rand.nextInt(minLength / 2, height - minLength / 2);

        int sideLength = rand.nextInt(minLength, maxLength);

        this.bottom = this.top + sideLength;
        this.right = this.left + sideLength;

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

        for (int i = this.left; i < this.right; i++) {
            for (int j = this.top; j < this.bottom; j++) {
                points.add(new Point(i, j));
            }
        }

        return points;
    }

//    private List<Point> IterateTriangle(Point[] points, int granularity) {
//        List<Point> returnList = new ArrayList<Point>();
//
//        Point pt1 = points[0];
//        Point pt2 = points[1];
//        Point pt3 = points[2];
//        Point tmp;
//
//        if (pt2.x < pt1.x) {
//            tmp = pt1;
//            pt1 = pt2;
//            pt2 = tmp;
//        }
//
//        if (pt3.x < pt2.x) {
//            tmp = pt2;
//            pt2 = pt3;
//            pt3 = tmp;
//
//            if (pt2.x < pt1.x) {
//                tmp = pt1;
//                pt1 = pt2;
//                pt2 = tmp;
//            }
//        }
//
//        final int pt2Y = pt2.y;
//        Function<Integer, Double> baseFunc = CreateFunc(pt1, pt3);
//        Function<Integer, Double> line1Func = pt1.x == pt2.x ? (x -> (double) pt2Y) : CreateFunc(pt1, pt2);
//
//        for (int x = pt1.x; x < pt2.x; x += granularity) {
//            int[] minMax = GetRange(line1Func.apply(x), baseFunc.apply(x));
//            int minY = minMax[0];
//            int maxY = minMax[1];
//
//            for (int y = minY; y <= maxY; y += granularity) {
//                returnList.add(new Point(x, y));
//            }
//        }
//
//        Function<Integer, Double> line2Func = pt2.x == pt3.x ? (x -> (double) pt2Y) : CreateFunc(pt2, pt3);
//
//        for (int x = pt2.x; x <= pt3.x; x += granularity) {
//            int[] minMax = GetRange(line2Func.apply(x), baseFunc.apply(x));
//            int minY = minMax[0];
//            int maxY = minMax[1];
//
//            for (int y = minY; y <= maxY; y += granularity) {
//                returnList.add(new Point(x, y));
//            }
//        }
//
//        return returnList;
//    }
//
//    private int[] GetRange(double y1, double y2) {
//        if (y1 < y2) {
//            return new int[]{(int) Math.ceil(y1), (int) Math.floor(y2)};
//        }
//
//        return new int[]{(int) Math.floor(y1), (int) Math.ceil(y2)};
//    }
//
//    private Function<Integer, Double> CreateFunc(Point pt1, Point pt2) {
//        int y0 = pt1.y;
//
//        if (y0 == pt2.y) {
//            return x -> (double) y0;
//        }
//
//        double m = (double) (pt2.y - y0) / (pt2.x - pt1.x);
//
//        return x -> m * (x - pt1.x) + y0;
//    }

    @Override
    public int GetColor() {
        return this.color;
    }

    @Override
    public Canvas AddShape(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.color);
        canvas.drawRect(this.left, this.top, this.right, this.bottom, paint);
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
