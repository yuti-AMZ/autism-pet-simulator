package com.autismpetsimulator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DailyGoals {
    private Map<String, Integer> progress = new HashMap<>();
    private int stars = 0;
    private LocalDate lastReset = LocalDate.now();
    private String currentChallenge = ""; // Default empty
    private boolean challengeCompleted = false;

    public DailyGoals() {
        resetDailyData(); // Sets challenge on startup
    }

    private void resetDailyData() {
        boolean isNewDay = lastReset.isBefore(LocalDate.now());
        if (isNewDay) {
            progress.clear();
            stars = 0;
            challengeCompleted = false;
            lastReset = LocalDate.now();
        }
        setNewChallenge(); // Always set/refresh challenge
    }

    private void setNewChallenge() {
        String[] challenges = {
                "Comfort a sad friend today ❤️",
                "Play a gentle puzzle 🌱",
                "Feed your friend a treat 🍎",
                "Make your friend happy! 😊",
                "Learn a new emotion today 🌟"
        };
        currentChallenge = challenges[new Random().nextInt(challenges.length)];
    }

    public void trackProgress(Pet pet) {
        resetDailyData();
        String key = pet.getName();
        int count = progress.getOrDefault(key, 0) + 1;
        progress.put(key, count);

        if (count % 2 == 0) {
            stars++;
        }

        if (!challengeCompleted && count >= 3) {
            stars += 2;
            challengeCompleted = true;
        }
    }

    public String getReport() {
        resetDailyData();
        StringBuilder report = new StringBuilder("🌟 Today's Gentle Progress 🌟\n\n");

        if (progress.isEmpty()) {
            report.append("No gentle interactions yet today.\n");
        } else {
            progress.forEach((name, count) -> report.append("• ").append(name).append(": ").append(count)
                    .append(" interactions ❤️\n"));
        }

        report.append("\n✨ Stars Earned: ").append(stars).append(" 🌟\n");
        report.append("🎯 Daily Challenge: ").append(currentChallenge).append("\n");
        report.append(challengeCompleted ? "✅ Completed! Extra stars awarded!" : "⏳ Keep going gently...");

        return report.toString();
    }

    public String getCurrentChallenge() {
        resetDailyData(); // ← This ensures challenge is always set
        return currentChallenge;
    }

    public int getStars() {
        return stars;
    }

    public Map<String, Integer> getProgress() {
        return new HashMap<>(progress);
    }
}