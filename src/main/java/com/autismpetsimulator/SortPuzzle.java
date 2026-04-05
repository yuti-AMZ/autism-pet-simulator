package com.autismpetsimulator;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortPuzzle implements Puzzle {
    private final String level;

    public SortPuzzle(String level) {
        this.level = level;
    }

    @Override
    public int play() {
        Stage stage = new Stage();
        stage.setTitle("Sort by Size 📏 - " + level);

        VBox root = new VBox(25);
        root.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #f0f8ff;");

        Text instruction = new Text("Drag circles from small to large →");
        instruction.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #4CAF50;");

        Text status = new Text("Get ready! Drag the circles and click Check ✅");
        status.setStyle("-fx-font-size: 18px; -fx-fill: #666;");

        Pane gameArea = new Pane();
        gameArea.setPrefSize(600, 300);
        gameArea.setStyle(
                "-fx-background-color: white; -fx-border-color: #bbdefb; -fx-border-width: 4; -fx-border-radius: 15;");

        int numItems = switch (level) {
            case "Easy 🌱" -> 3;
            case "Medium 🌻" -> 4;
            case "Hard ⭐" -> 5;
            default -> 3;
        };

        List<Double> sizes = new ArrayList<>();
        for (int i = 1; i <= numItems; i++) {
            sizes.add(40.0 + i * 25); // Bigger circles — easier to grab
        }
        Collections.shuffle(sizes);

        List<Circle> circles = new ArrayList<>();

        // Create draggable circles
        for (int i = 0; i < numItems; i++) {
            double size = sizes.get(i);
            Circle circle = new Circle(size, Color.hsb(i * 360.0 / numItems, 0.8, 0.9));
            circle.setLayoutX(100 + i * 100);
            circle.setLayoutY(80);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(3);

            double[] dragDelta = new double[2];
            circle.setOnMousePressed(e -> {
                dragDelta[0] = circle.getLayoutX() - e.getSceneX();
                dragDelta[1] = circle.getLayoutY() - e.getSceneY();
                circle.setOpacity(0.8);
                circle.toFront();
            });

            circle.setOnMouseDragged(e -> {
                circle.setLayoutX(e.getSceneX() + dragDelta[0]);
                circle.setLayoutY(e.getSceneY() + dragDelta[1]);
            });

            circle.setOnMouseReleased(e -> circle.setOpacity(1.0));

            circles.add(circle);
            gameArea.getChildren().add(circle);
        }

        // Big target zones
        for (int i = 0; i < numItems; i++) {
            Circle target = new Circle(70 + i * 25);
            target.setFill(Color.TRANSPARENT);
            target.setStroke(Color.LIGHTBLUE);
            target.setStrokeWidth(5);
            target.setLayoutX(100 + i * 100);
            target.setLayoutY(200);
            gameArea.getChildren().add(target);
        }

        // CHECK BUTTON — now you can check anytime!
        Button checkButton = new Button("Check My Work ✅");
        checkButton.setStyle(
                "-fx-font-size: 18px; -fx-padding: 12 30; -fx-background-radius: 30; " +
                        "-fx-background-color: linear-gradient(#4CAF50, #43A047); -fx-text-fill: white;");

        checkButton.setOnAction(e -> checkSorting(circles, status, numItems, stage));

        root.getChildren().addAll(instruction, gameArea, status, checkButton);

        stage.setScene(new Scene(root, 700, 550));
        stage.showAndWait();

        return numItems * 10; // Reward for trying!
    }

    private void checkSorting(List<Circle> circles, Text status, int total, Stage stage) {
        int correct = 0;
        List<Double> expectedSizes = new ArrayList<>();
        for (int i = 1; i <= total; i++) {
            expectedSizes.add(40.0 + i * 25);
        }

        for (int i = 0; i < total; i++) {
            Circle c = circles.get(i);
            double targetX = 100 + i * 100;
            double expectedSize = expectedSizes.get(i);

            boolean goodPosition = Math.abs(c.getLayoutX() - targetX) < 80 && // Very forgiving!
                    Math.abs(c.getLayoutY() - 200) < 80;
            boolean goodSize = Math.abs(c.getRadius() - expectedSize) < 15;

            if (goodPosition && goodSize) {
                c.setStroke(Color.GREEN);
                c.setStrokeWidth(8);
                correct++;
            } else {
                c.setStroke(Color.RED);
                c.setStrokeWidth(5);
            }
        }

        if (correct == total) {
            status.setText("Wonderful! All sorted perfectly! 🌟");
            status.setFill(Color.web("#4CAF50"));
            // Auto close after 2 seconds
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(2));
            pause.setOnFinished(e -> stage.close());
            pause.play();
        } else {
            status.setText("Good effort! " + correct + "/" + total + " correct. Try again gently ❤️");
            status.setFill(Color.web("#FF9800"));
        }
    }
}