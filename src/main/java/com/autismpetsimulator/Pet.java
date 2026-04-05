package com.autismpetsimulator;

import org.json.JSONObject;
import java.nio.file.*;

public class Pet {
    private String name, type;
    private String currentEmotion = "happy";
    private int happiness = 50, energy = 50, focusLevel = 50, emotionLevel = 50, bondLevel = 50;
    private final EmotionLearner learner = new EmotionLearner();

    public Pet(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public void feed() {
        happiness += 10;
        energy += 5;
        clamp();
    }

    public void play() {
        happiness += 15;
        energy -= 10;
        clamp();
    }

    public void reactEmotion(String action, String emotion) {
        boolean correct = switch (emotion) {
            case "sad", "scared" -> action.equals("comfort") || action.equals("cuddle");
            case "happy", "excited" -> action.equals("play") || action.equals("cuddle");
            default -> false;
        };

        bondLevel += correct ? 5 : -5;

        switch (action) {
            case "comfort", "cuddle" -> {
                happiness += 10;
                emotionLevel += 5;
            }
            case "play" -> {
                happiness += 5;
                energy -= 5;
                emotionLevel += 10;
            }
        }
        clamp();
        learner.update(this, correct);
    }

    public void completePuzzle(int score) {
        focusLevel += score;
        clamp();
    }

    public void decreaseHappiness(int amount) {
        happiness = Math.max(0, happiness - amount);
    }

    private void clamp() {
        happiness = Math.max(0, Math.min(100, happiness));
        energy = Math.max(0, Math.min(100, energy));
        focusLevel = Math.max(0, Math.min(100, focusLevel));
        emotionLevel = Math.max(0, Math.min(100, emotionLevel));
        bondLevel = Math.max(0, Math.min(100, bondLevel));
    }

    public String getEmotion() {
        return currentEmotion;
    }

    public void setEmotion(String e) {
        currentEmotion = e;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getEnergy() {
        return energy;
    }

    public int getFocusLevel() {
        return focusLevel;
    }

    public int getBondLevel() {
        return bondLevel;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public EmotionLearner getLearner() {
        return learner;
    }

    public void saveProgress() {
        try {
            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("type", type);
            data.put("happiness", happiness);
            data.put("energy", energy);
            data.put("focusLevel", focusLevel);
            data.put("emotionLevel", emotionLevel);
            data.put("bondLevel", bondLevel);
            data.put("currentEmotion", currentEmotion);
            data.put("qTable", learner.toJSON());

            Path dir = Paths.get("pets");
            if (!Files.exists(dir))
                Files.createDirectories(dir);
            Files.writeString(dir.resolve(name + ".json"), data.toString(2));
        } catch (Exception e) {
            System.out.println("Save error for " + name);
        }
    }

    public void loadProgress() {
        try {
            Path path = Paths.get("pets/" + name + ".json");
            if (!Files.exists(path))
                return;
            String content = Files.readString(path);
            JSONObject data = new JSONObject(content);

            happiness = data.getInt("happiness");
            energy = data.getInt("energy");
            focusLevel = data.getInt("focusLevel");
            emotionLevel = data.optInt("emotionLevel", 50);
            bondLevel = data.optInt("bondLevel", 50);
            currentEmotion = data.optString("currentEmotion", "happy");
            learner.fromJSON(data.optJSONArray("qTable"));
        } catch (Exception e) {
            System.out.println("Load error for " + name);
        }
    }
}