package com.autismpetsimulator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryPuzzle implements Puzzle {
    private final String level;

    public MemoryPuzzle(String level) {
        this.level = level;
    }

    @Override
    public int play() {
        Stage stage = new Stage();
        stage.setTitle("Memory Match 🌈 - " + level);

        int pairs = switch (level) {
            case "Medium 🌻" -> 4;
            case "Hard ⭐" -> 6;
            default -> 3;
        };

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(javafx.geometry.Pos.CENTER);

        List<Color> colors = new ArrayList<>();
        List<Color> availableColors = List.of(
                Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
                Color.PURPLE, Color.PINK, Color.CYAN, Color.YELLOW);

        Random rand = new Random();
        for (int i = 0; i < pairs; i++) {
            Color color = availableColors.get(rand.nextInt(availableColors.size()));
            colors.add(color);
            colors.add(color);
        }
        Collections.shuffle(colors);

        AtomicInteger matches = new AtomicInteger(0);
        AtomicInteger tries = new AtomicInteger(0);
        List<Rectangle> flipped = new ArrayList<>();

        for (int i = 0; i < colors.size(); i++) {
            final int colorIndex = i;

            Rectangle card = new Rectangle(60, 60, Color.LIGHTGRAY);
            card.setArcWidth(10);
            card.setArcHeight(10);
            card.setStroke(Color.GRAY);
            card.setStrokeWidth(2);

            card.setOnMouseClicked(e -> {
                if (flipped.size() >= 2 || card.getFill() != Color.LIGHTGRAY) {
                    return;
                }

                card.setFill(colors.get(colorIndex));
                flipped.add(card);

                if (flipped.size() == 2) {
                    tries.incrementAndGet();
                    Color c1 = (Color) flipped.get(0).getFill();
                    Color c2 = (Color) flipped.get(1).getFill();

                    if (c1.equals(c2)) {
                        matches.incrementAndGet();
                        flipped.clear();
                        if (matches.get() == pairs) {
                            Platform.runLater(() -> stage.close());
                        }
                    } else {
                        // SMOOTH DELAY — NO FREEZING!
                        Timeline timeline = new Timeline(new KeyFrame(
                                Duration.millis(1000),
                                ae -> {
                                    flipped.forEach(c -> c.setFill(Color.LIGHTGRAY));
                                    flipped.clear();
                                }));
                        timeline.play();
                    }
                }
            });

            int row = i / (colors.size() / 2);
            int col = i % (colors.size() / 2);
            grid.add(card, col, row);
        }

        stage.setScene(new Scene(grid, 600, 500));
        stage.showAndWait();

        return Math.max(0, (pairs * 10) - (tries.get() * 2));
    }
}