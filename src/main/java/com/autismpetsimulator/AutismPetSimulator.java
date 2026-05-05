package com.autismpetsimulator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.function.Consumer;

public class AutismPetSimulator extends Application {

    private Pet currentPet;
    private DailyGoals dailyGoals = new DailyGoals();
    private ImageView petView;
    private Text speechText;
    private Text starsText;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        showPetSelection(primaryStage);
    }

    private void showPetSelection(Stage stage) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e8f5e8, #f1f8e9);");

        Text title = new Text("Choose Your Gentle Friend 🐾");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        HBox petsBox = new HBox(40);
        petsBox.setAlignment(Pos.CENTER);

        addPetSelection(petsBox, "Meow the Cat", "pet_cat_happy.png", "cat", stage);
        addPetSelection(petsBox, "Bubu the Dog", "pet_dog_happy.png", "dog", stage);
        addPetSelection(petsBox, "Lala the Bunny", "pet_bunny_happy.png", "bunny", stage);

        root.getChildren().addAll(title, petsBox);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Choose Your Friend");
        stage.setResizable(false);
        stage.show();
    }

    private void addPetSelection(HBox box, String name, String imagePath, String type, Stage selectionStage) {
        VBox petCard = new VBox(15);
        petCard.setAlignment(Pos.CENTER);
        petCard.setPadding(new Insets(20));
        petCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        ImageView petImage = new ImageView();
        try {
            petImage.setImage(new Image(getClass().getResourceAsStream("/" + imagePath)));
        } catch (Exception e) {
            petImage.setImage(new Image(getClass().getResourceAsStream("/pet_cat_happy.png")));
        }
        petImage.setFitWidth(120);
        petImage.setFitHeight(120);

        Text petName = new Text(name);
        petName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #2E7D32;");

        Button chooseButton = new Button("Choose Friend");
        chooseButton.setStyle(
                "-fx-font-size: 14px; -fx-background-color: linear-gradient(#4CAF50, #45a049); -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 20;");
        chooseButton.setOnAction(e -> startGameWithPet(type, selectionStage));

        petCard.getChildren().addAll(petImage, petName, chooseButton);
        box.getChildren().add(petCard);
    }

    private void startGameWithPet(String type, Stage selectionStage) {
        if (selectionStage != null) {
            selectionStage.close();
        }

        Stage gameStage = new Stage();
        currentPet = PetSelector.choosePet(type);
        currentPet.loadProgress();
        dailyGoals.trackProgress(currentPet);

        showMainGame(gameStage);
    }

    private void showMainGame(Stage stage) {
        VBox mainRoot = new VBox(10);
        mainRoot.setStyle("-fx-background-color: transparent;");

        // Custom back button
        HBox controlsBar = new HBox(5);
        controlsBar.setAlignment(Pos.CENTER_LEFT);
        controlsBar.setPadding(new Insets(10));
        controlsBar.setStyle("-fx-background-color: rgba(0,0,0,0.1);");

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> {
            stage.close();
            showPetSelection(new Stage());
        });
        backBtn.setStyle(
                "-fx-font-size: 14px; -fx-background-color: rgba(255,255,255,0.8); -fx-text-fill: #2E7D32; -fx-background-radius: 10;");

        controlsBar.getChildren().add(backBtn);
        mainRoot.getChildren().add(controlsBar);

        Pane root = new Pane();
        root.setStyle("-fx-background-image: url('background_home.png'); -fx-background-size: cover;");

        petView = new ImageView();
        petView.setFitWidth(250);
        petView.setFitHeight(250);
        petView.setX(375);
        petView.setY(100);
        root.getChildren().add(petView);

        speechText = new Text("");
        speechText.setFont(Font.font("Arial", 22));
        speechText.setStyle("-fx-fill: white; -fx-stroke: #333; -fx-stroke-width: 1.5;");
        speechText.setX(300);
        speechText.setY(380);
        speechText.setWrappingWidth(400);
        root.getChildren().add(speechText);

        starsText = new Text("Stars: " + dailyGoals.getStars() + " 🌟");
        starsText.setFont(Font.font("Arial", 18));
        starsText.setStyle("-fx-fill: gold; -fx-stroke: #8B4513; -fx-stroke-width: 1;");
        starsText.setX(50);
        starsText.setY(50);
        root.getChildren().add(starsText);

        Text challengeText = new Text("Daily Goal: " + dailyGoals.getCurrentChallenge());
        challengeText.setFont(Font.font("Arial", 16));
        challengeText.setStyle("-fx-fill: #FF9800; -fx-stroke: #E65100; -fx-stroke-width: 1;");
        challengeText.setX(50);
        challengeText.setY(80);
        root.getChildren().add(challengeText);

        ComboBox<String> puzzleLevel = new ComboBox<>();
        puzzleLevel.getItems().addAll("Easy 🌱", "Medium 🌻", "Hard ⭐");
        puzzleLevel.setValue("Easy 🌱");
        root.getChildren().add(puzzleLevel);

        mainRoot.getChildren().add(root);

        Scene scene = new Scene(mainRoot, 600, 500);

        // Fixed positions for 600x500 screen
        double centerX = 300;
        petView.setX(centerX - 125); // center the 250 width
        petView.setY(100);

        speechText.setX(centerX - 200); // center the 400 width wrapping
        speechText.setY(300);

        starsText.setX(50);
        starsText.setY(50);

        challengeText.setX(50);
        challengeText.setY(80);

        puzzleLevel.setLayoutX(50);
        puzzleLevel.setLayoutY(350);

        double buttonY = 400;
        double buttonSpacing = 140;
        double startX = 10; // start from left

        createActionButton(root, "Feed 🍎", startX, buttonY, () -> doAction(Pet::feed));
        createActionButton(root, "Comfort ❤️", startX + buttonSpacing, buttonY, () -> doEmotion());
        createActionButton(root, "Puzzle 🎮", startX + buttonSpacing * 2, buttonY,
                () -> doPuzzle(puzzleLevel.getValue()));
        createActionButton(root, "Progress 📊", startX + buttonSpacing * 3, buttonY, () -> showDashboard());

        stage.setScene(scene);
        stage.setTitle("Caring for " + currentPet.getName() + " 🐾");
        stage.setResizable(false);
        stage.show();

        updateDisplay();
    }

    private void createActionButton(Pane root, String text, double x, double y, Runnable action) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefSize(120, 50);
        button.setFont(Font.font("Arial", 14));
        button.setStyle(
                "-fx-background-color: linear-gradient(#FF6B6B, #FF8E53); -fx-text-fill: white; -fx-background-radius: 25;");
        button.setOnAction(e -> action.run());
        root.getChildren().add(button);
    }

    private void doAction(Consumer<Pet> action) {
        action.accept(currentPet);
        currentPet.saveProgress();
        dailyGoals.trackProgress(currentPet);
        updateDisplay();
    }

    private void doEmotion() {
        new EmotionModule().play(currentPet, petView, speechText);
        updateDisplay();
    }

    private void doPuzzle(String level) {
        new PuzzleModule().play(currentPet, petView, speechText, level);
        updateDisplay();
    }

    private void showDashboard() {
        new ParentDashboard().showStats(java.util.List.of(currentPet), dailyGoals);
    }

    private void updateDisplay() {
        try {
            String path = "/pet_" + currentPet.getType().toLowerCase() + "_" + currentPet.getEmotion() + ".png";
            petView.setImage(new Image(getClass().getResourceAsStream(path)));
            EmotionAnimator.animate(currentPet, petView);
        } catch (Exception e) {
            petView.setImage(new Image(getClass().getResourceAsStream("/pet_cat_happy.png")));
        }

        speechText.setText(currentPet.getName() + " feels " + currentPet.getEmotion() + "!\nHappiness: "
                + currentPet.getHappiness());
        starsText.setText("Stars: " + dailyGoals.getStars() + " 🌟");
    }
}