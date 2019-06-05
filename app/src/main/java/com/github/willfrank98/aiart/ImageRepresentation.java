package com.github.willfrank98.aiart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import java.util.Arrays;

public class ImageRepresentation {
    private int height;
    private int width;
    private Bitmap image;
    private IShape[] shapes;
//    private double[] fitness;

    public ImageRepresentation(Bitmap image) {
        this.image = image;
        this.height = image.getHeight();
        this.width = image.getWidth();
    }

    public void NewBatch(int num, String type, int minLength, int maxLength) {
        IShape[] shapes;
        shapes = new IShape[num];
//        int onePercent = num / 100;

        for (int i = 0; i < num; i++) {
            switch (type) {
                case "Triangle":
                    shapes[i] = new Triangle(this.height, this.width, minLength, maxLength);
                    break;
                case "Square":
                    shapes[i] = new Square(this.height, this.width, minLength, maxLength);
                    break;
                case "Circle":
                    shapes[i] = new Circle(this.height, this.width, minLength, maxLength);
                    break;
            }


//            if (i % onePercent == 0)
//            {
//                decimal percent = decimal.Divide(i, shapes.Length) * 100;
//                Console.SetCursorPosition(0, 0);
//                Console.WriteLine("Generating: {0:0}%", percent);
//            }
        }

        this.shapes = shapes;
    }

    public void EvaluateFitness(int granularity) {
        //get all pixel colors
        int[] colors = new int[this.width * this.height];
        this.image.getPixels(colors, 0, this.width, 0, 0, this.width, this.height);

        int onePercent = this.shapes.length / 100;
        for (IShape shape : this.shapes) {
            int pixels = 0;
            double tempFitness = 0.0;
            int shapeColor = shape.GetColor();
            for (Point point : shape.IteratePoints(granularity)) {
                if (point.x >= 0 && point.x < this.width && point.y >= 0 && point.y < this.height) {
                    int picColor = colors[point.x + point.y * width];

                    double[] c1 = {Color.red(picColor) / 255.0, Color.green(picColor) / 255.0, Color.blue(picColor) / 255.0};
                    double[] c2 = {Color.red(shapeColor) / 255.0, Color.green(shapeColor) / 255.0, Color.blue(shapeColor) / 255.0};

                    // investigate eventually
//                    ColorSpace lab = ColorSpace.get(ColorSpace.Named.CIE_LAB);
//                    long tempc1 = Color.convert(shapeColor, lab);
//                    long tempc2 = Color.convert(picColor, lab);

                    // convert RGB to XYZ
                    c1 = RGBtoXYZ(c1);
                    c2 = RGBtoXYZ(c2);

                    // XYZ to Lab
                    c1 = XYZtoLab(c1);
                    c2 = XYZtoLab(c2);

                    // Delta E (CIE 1976)
                    double E = Math.sqrt(Math.pow(c1[0] - c2[0], 2) + Math.pow(c1[1] - c2[1], 2) + Math.pow(c1[2] - c2[2], 2));
                    tempFitness += E;

                    pixels++;
                }
            }

            tempFitness = tempFitness / Math.pow(pixels, .5);
            shape.SetFitness(1 / tempFitness);

//            if (i % onePercent == 0)
//            {
//                decimal percent = decimal.Divide(i, shapes.Length) * 100;
//                Console.SetCursorPosition(0, 1);
//                Console.WriteLine("Evaluating: {0:0}%", percent);
//            }
        }

        Arrays.sort(this.shapes);
    }

    private double[] RGBtoXYZ(double[] c) {
        for (int i = 0; i < c.length; i++) {
            if (c[i] <= 0.0405) {
                c[i] = c[i] / 12.92;
            } else {
                c[i] = Math.pow((c[i] + 0.055) / 1.055, 2.4);
            }
        }

        double[][] M = {{0.4124564, 0.3575761, 0.1804375},
                {0.2126729, 0.7151522, 0.0721750},
                {0.0193339, 0.1191920, 0.9503041}};

        double[] newC = new double[c.length];
        for (int i = 0; i < 3; i++) {
            double temp = 0;
            for (int j = 0; j < 3; j++) {
                temp += c[j] * M[i][j];
            }
            newC[i] = temp;
        }

        return newC;
    }

    private double[] XYZtoLab(double[] c) {
        double[] white = {0.95047, 1.0000001, 1.08883};

        double epsilon = 216 / 24389.0;
        double kappa = 24389 / 27.0;

        double[] r = new double[3];
        for (int i = 0; i < r.length; i++) {
            r[i] = c[i] / white[i];
        }

        double[] f = new double[3];
        for (int i = 0; i < f.length; i++) {
            if (r[i] > epsilon) {
                f[i] = Math.pow(r[i], 1.0 / 3.0);
            } else {
                f[i] = (r[i] * kappa + 16) / 116;
            }
        }

        double[] Lab = {116 * f[1] - 16, 500 * (f[0] - f[1]), 200 * (f[1] - f[2])};
        return Lab;
    }

    public Bitmap Draw(int thisMany, String shapeType) {
        Bitmap finalImage = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_4444);
        Canvas drawing = new Canvas(finalImage);

        drawing.drawRGB(255, 255, 255);

        //draws the top ranking thisMany shapes, from worst to best
        int onePercent = thisMany / 100;
        for (int i = shapes.length - thisMany; i < shapes.length; i++) {
            drawing = this.shapes[i].AddShape(drawing);

//            if (i % onePercent == 0)
//            {
//                decimal percent = decimal.Divide(i, shapes.Length) * 100;
//                Console.SetCursorPosition(0, 2);
//                Console.WriteLine("Drawing: {0:0}%", percent);
//            }
        }

        drawing.save();
        return finalImage;
    }
}
