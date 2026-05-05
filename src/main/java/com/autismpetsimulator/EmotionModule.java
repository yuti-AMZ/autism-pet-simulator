package com.autismpetsimulator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmotionModule {

    public void play(Pet pet, ImageView view, Text speech) {
        String emotion = pet.getLearner().chooseEmotion(pet);
        pet.setEmotion(emotion);
        EmotionAnimator.animate(pet, view);
        speech.setText(pet.getName() + " feels " + emotion + "...");

        Stage stage = new Stage();
        stage.setTitle("Help " + pet.getName());

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #fff3e0;");

        Text prompt = new Text("How can you help?");
        prompt.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox buttons = new HBox(20);
        Button help = new Button("Help ❤️");
        Button ignore = new Button("Ignore 🚫");
        Button throwBtn = new Button("Throw Something 🎯");
        Button cuddle = new Button("Hug 🤗");

        help.setOnAction(e -> respond("help", emotion, pet, stage, speech));
        ignore.setOnAction(e -> respond("ignore", emotion, pet, stage, speech));
        throwBtn.setOnAction(e -> respond("throw", emotion, pet, stage, speech));
        cuddle.setOnAction(e -> respond("cuddle", emotion, pet, stage, speech));

        buttons.getChildren().addAll(help, ignore, throwBtn, cuddle);
        root.getChildren().addAll(prompt, buttons);

        stage.setScene(new Scene(root, 450, 250));
        stage.showAndWait();
    }

    private void respond(String action, String emotion, Pet pet, Stage stage, Text speech) {
        switch (action) {
            case "help":
                pet.reactEmotion("comfort", emotion); // assuming reactEmotion handles "comfort"
                speech.setText("Thank you! I feel better now 🌟");
                break;
            case "ignore":
                pet.setEmotion("sad");
                pet.decreaseHappiness(10);
                speech.setText("You ignored me... I feel sad 😢");
                break;
            case "throw":
                pet.setEmotion("scared");
                pet.decreaseHappiness(15);
                speech.setText("I'm scared! 😱");
                break;
            case "cuddle":
                pet.reactEmotion("cuddle", emotion);
                speech.setText("Thank you! I feel better now 🌟");
                break;
        }
        pet.saveProgress();
        stage.close();
    }
}