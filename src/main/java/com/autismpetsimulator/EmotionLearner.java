package com.autismpetsimulator;

import org.json.JSONArray;
import java.util.Random;

public class EmotionLearner {
    private final double[][] qTable = new double[81][4];
    private final double alpha = 0.15;
    private final double gamma = 0.9;
    private final double epsilon = 0.12;
    private final Random random = new Random();
    private int lastState = -1;
    private int lastAction = -1;
    private final String[] emotions = { "happy", "sad", "excited", "scared" };

    public String chooseEmotion(Pet pet) {
        int state = getState(pet);
        lastState = state;
        lastAction = random.nextDouble() < epsilon ? random.nextInt(4) : getBestAction(state);
        return emotions[lastAction];
    }

    public void update(Pet pet, boolean correct) {
        if (lastState == -1)
            return;
        double reward = correct ? 1.5 : -1.2;
        int currentState = getState(pet);
        double futureMax = qTable[currentState][getBestAction(currentState)];
        qTable[lastState][lastAction] += alpha * (reward + gamma * futureMax - qTable[lastState][lastAction]);
    }

    private int getBestAction(int state) {
        int best = 0;
        for (int i = 1; i < 4; i++) {
            if (qTable[state][i] > qTable[state][best])
                best = i;
        }
        return best;
    }

    private int getState(Pet pet) {
        int h = bin(pet.getHappiness());
        int e = bin(pet.getEnergy());
        int f = bin(pet.getFocusLevel());
        int b = bin(pet.getBondLevel());
        return h * 27 + e * 9 + f * 3 + b;
    }

    private int bin(int value) {
        return value < 40 ? 0 : value < 70 ? 1 : 2;
    }

    public JSONArray toJSON() {
        JSONArray array = new JSONArray();
        for (double[] row : qTable) {
            JSONArray rowArray = new JSONArray();
            for (double val : row)
                rowArray.put(val);
            array.put(rowArray);
        }
        return array;
    }

    public void fromJSON(JSONArray array) {
        if (array == null)
            return;
        for (int i = 0; i < 81 && i < array.length(); i++) {
            JSONArray row = array.getJSONArray(i);
            for (int j = 0; j < 4 && j < row.length(); j++) {
                qTable[i][j] = row.getDouble(j);
            }
        }
    }
}