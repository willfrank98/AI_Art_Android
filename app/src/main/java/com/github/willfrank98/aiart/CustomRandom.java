package com.github.willfrank98.aiart;

import java.util.Random;

class CustomRandom extends Random {

    private Random rand;

    CustomRandom() {
        rand = new Random();
    }

    int nextInt(int min, int max) {
        double temp = rand.nextDouble();
        temp *= (max - min);
        temp += min;
        return (int) Math.round(temp);
    }
}
