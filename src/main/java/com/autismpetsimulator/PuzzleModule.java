package com.autismpetsimulator;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.util.concurrent.atomic.AtomicReference;

public class PuzzleModule {

    public void play(Pet pet, ImageView view, Text speech, String level) {
        // Show puzzle selection dialog
        String selectedPuzzle = showPuzzleSelectionDialog();

        if (selectedPuzzle == null) {
            // User cancelled
            return;
        }

        Puzzle puzzle = createPuzzle(selectedPuzzle, level);

        int score = puzzle.play();

        // Level-based score multiplier
        int multiplier = switch (level) {
            case "Medium 🌻" -> 2;
            case "Hard ⭐" -> 3;
            default -> 1;
        };

        score *= multiplier;
        pet.completePuzzle(score);
        pet.saveProgress();

        // Set pet emotion based on puzzle outcome
        if (score > 0) {
            pet.setEmotion("excited");
        } else {
            pet.setEmotion("sad");
        }

        EmotionAnimator.animate(pet, view);

        String message = switch (level) {
            case "Easy 🌱" -> "Great gentle start! 🌱";
            case "Medium 🌻" -> "Beautiful focus! 🌻";
            case "Hard ⭐" -> "Amazing concentration! ⭐";
            default -> "Wonderful effort! ❤️";
        };

        speech.setText(pet.getName() + " says: " + message + "\nFocus + " + score + "!");
    }

    private String showPuzzleSelectionDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Choose Your Puzzle! 🎮");

        AtomicReference<String> selection = new AtomicReference<>(null);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e8f5e8, #f1f8e9);");

        Text title = new Text("Which puzzle would you like to play? 🎯");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        HBox puzzleButtons = new HBox(15);
        puzzleButtons.setAlignment(Pos.CENTER);

        Button memoryBtn = new Button("Memory Match\n🌈");
        memoryBtn.setPrefSize(120, 80);
        memoryBtn.setOnAction(e -> {
            selection.set("MemoryPuzzle");
            dialog.close();
        });

        Button colorBtn = new Button("Color Match\n🎨");
        colorBtn.setPrefSize(120, 80);
        colorBtn.setOnAction(e -> {
            selection.set("ColorMatchPuzzle");
            dialog.close();
        });

        Button sortBtn = new Button("Sort Shapes\n🔺");
        sortBtn.setPrefSize(120, 80);
        sortBtn.setOnAction(e -> {
            selection.set("SortPuzzle");
            dialog.close();
        });

        Button themedBtn = new Button("Theme Match\n🎭");
        themedBtn.setPrefSize(120, 80);
        themedBtn.setOnAction(e -> {
            selection.set("ThemedMatchPuzzle");
            dialog.close();
        });

        puzzleButtons.getChildren().addAll(memoryBtn, colorBtn, sortBtn, themedBtn);

        Button cancelBtn = new Button("Cancel ❌");
        cancelBtn.setStyle(
                "-fx-font-size: 14px; -fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 20;");
        cancelBtn.setOnAction(e -> {
            selection.set(null);
            dialog.close();
        });

        root.getChildren().addAll(title, puzzleButtons, cancelBtn);

        Scene scene = new Scene(root, 600, 300);
        dialog.setScene(scene);
        dialog.showAndWait();

        return selection.get();
    }

    private Puzzle createPuzzle(String puzzleType, String level) {
        return switch (puzzleType) {
            case "MemoryPuzzle" -> new MemoryPuzzle(level);
            case "ColorMatchPuzzle" -> new ColorMatchPuzzle(level);
            case "SortPuzzle" -> new SortPuzzle(level);
            case "ThemedMatchPuzzle" -> new ThemedMatchPuzzle(level);
            default -> new MemoryPuzzle(level);
        };
    }
}