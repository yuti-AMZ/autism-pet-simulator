package com.autismpetsimulator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ThemedMatchPuzzle implements Puzzle {
    private final String level;

    public ThemedMatchPuzzle(String level) {
        this.level = level;
    }

    @Override
    public int play() {
        Stage stage = new Stage();
        stage.setTitle("Match the Gentle Friends 🐾 - " + level);

        VBox root = new VBox(30);
        root.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #f0f8ff;");

        Text instruction = new Text("Tap matching animal friends!");
        instruction.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #4CAF50;");

        Text feedback = new Text("");
        feedback.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(javafx.geometry.Pos.CENTER);

        int pairs = switch (level) {
            case "Easy 🌱" -> 2;
            case "Medium 🌻" -> 3;
            case "Hard ⭐" -> 4;
            default -> 2;
        };

        List<String> animals = new ArrayList<>();
        String[] types = { "cat", "dog", "bunny" };
        for (int i = 0; i < pairs; i++) {
            String animal = types[i % 3];
            animals.add(animal);
            animals.add(animal);
        }
        Collections.shuffle(animals);

        AtomicInteger matches = new AtomicInteger(0);
        List<ImageView> selected = new ArrayList<>();

        for (int i = 0; i < animals.size(); i++) {
            String animal = animals.get(i);

            ImageView animalView;
            try {
                animalView = new ImageView(new Image(getClass().getResourceAsStream("/pet_" + animal + "_happy.png")));
            } catch (Exception e) {
                animalView = new ImageView(new Image(getClass().getResourceAsStream("/pet_cat_happy.png")));
            }
            animalView.setFitWidth(90);
            animalView.setFitHeight(90);
            animalView.setPreserveRatio(true);
            animalView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);");

            final ImageView card = animalView;

            card.setOnMouseClicked(e -> {
                if (selected.size() >= 2 || selected.contains(card)) {
                    return;
                }

                selected.add(card);
                card.setOpacity(0.6);

                if (selected.size() == 2) {
                    ImageView first = selected.get(0);
                    ImageView second = selected.get(1);

                    String animal1 = extractAnimal(first.getImage().getUrl());
                    String animal2 = extractAnimal(second.getImage().getUrl());

                    if (animal1.equals(animal2)) {
                        // CORRECT MATCH
                        matches.incrementAndGet();
                        selected.forEach(v -> v.setOpacity(0.3));
                        selected.clear();

                        feedback.setText("Great match! 🌟");
                        feedback.setFill(Color.valueOf("#4CAF50"));

                        if (matches.get() == pairs) {
                            feedback.setText("Yay! All friends matched! Your pet feels excited! 🎉");
                            feedback.setFill(Color.valueOf("#FF9800"));

                            Timeline closeTimeline = new Timeline(new KeyFrame(
                                    Duration.millis(2000),
                                    ae -> stage.close()));
                            closeTimeline.play();
                        }
                    } else {
                        // WRONG MATCH — FIXED!
                        feedback.setText("Try again gently ❤️ Your pet feels a little sad...");
                        feedback.setFill(Color.valueOf("#F44336"));

                        Timeline flipBack = new Timeline(new KeyFrame(
                                Duration.millis(1200),
                                ae -> {
                                    selected.forEach(v -> v.setOpacity(1.0));
                                    selected.clear();
                                }));
                        flipBack.play();
                    }
                }
            });

            int col = i % 4;
            int row = i / 4;
            grid.add(card, col, row);
        }

        root.getChildren().addAll(instruction, grid, feedback);
        stage.setScene(new Scene(root, 600, 550));
        stage.showAndWait();

        return matches.get() == pairs ? 30 : 10;
    }

    private String extractAnimal(String url) {
        if (url == null)
            return "cat";
        if (url.contains("cat"))
            return "cat";
        if (url.contains("dog"))
            return "dog";
        if (url.contains("bunny"))
            return "bunny";
        return "cat";
    }
}