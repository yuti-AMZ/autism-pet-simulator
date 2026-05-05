package com.autismpetsimulator;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ColorMatchPuzzle implements Puzzle {
    private final String level;

    public ColorMatchPuzzle(String level) {
        this.level = level;
    }

    @Override
    public int play() {
        Stage stage = new Stage();
        stage.setTitle("Find the Color 🌈 - " + level);

        Random rand = new Random();
        Color target = switch (level) {
            case "Easy 🌱" -> Color.RED;
            case "Medium 🌻" -> Color.BLUE;
            case "Hard ⭐" -> Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
            default -> Color.GREEN;
        };

        List<Color> options = new ArrayList<>();
        options.add(target);

        List<Color> distractors = switch (level) {
            case "Easy 🌱" -> List.of(Color.BLUE, Color.YELLOW);
            case "Medium 🌻" -> List.of(Color.GREEN, Color.ORANGE, Color.PURPLE);
            case "Hard ⭐" -> List.of(
                    Color.color(target.getRed() * 0.8, target.getGreen() * 0.8, target.getBlue() * 0.8),
                    Color.color(target.getRed() * 1.2, target.getGreen() * 1.2, target.getBlue() * 1.2),
                    Color.hsb((rand.nextDouble() * 360), 0.8, 0.9));
            default -> List.of(Color.RED, Color.BLUE);
        };

        options.addAll(distractors);
        Collections.shuffle(options);

        VBox root = new VBox(30);
        root.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #f0f8ff;");

        Text instruction = new Text("Find the matching color:");
        instruction.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4CAF50;");

        Rectangle targetDisplay = new Rectangle(120, 120, target);
        targetDisplay.setArcWidth(20);
        targetDisplay.setArcHeight(20);
        targetDisplay.setStroke(Color.BLACK);
        targetDisplay.setStrokeWidth(3);

        HBox optionsBox = new HBox(20);
        optionsBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Simple boolean array to capture result from lambda
        final boolean[] isCorrect = { false };

        options.forEach(color -> {
            Rectangle option = new Rectangle(80, 80, color);
            option.setArcWidth(15);
            option.setArcHeight(15);
            option.setStroke(Color.BLACK);
            option.setStrokeWidth(2);
            option.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");

            option.setOnMouseClicked(e -> {
                isCorrect[0] = color.equals(target);
                stage.close();
            });

            optionsBox.getChildren().add(option);
        });

        root.getChildren().addAll(instruction, targetDisplay, optionsBox);
        stage.setScene(new Scene(root, 600, 500));
        stage.showAndWait();

        int score = switch (level) {
            case "Easy 🌱" -> 10;
            case "Medium 🌻" -> 15;
            case "Hard ⭐" -> 25;
            default -> 10;
        };

        return isCorrect[0] ? score : 0;
    }
}