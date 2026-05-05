package com.autismpetsimulator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SplashScreen extends Application {

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #f3e5f5);");

        ImageView logo = new ImageView();
        try {
            logo.setImage(new Image(getClass().getResourceAsStream("/logo.png")));
        } catch (Exception e) {
            logo.setImage(new Image(getClass().getResourceAsStream("/pet_cat_happy.png")));
        }
        logo.setFitWidth(250);
        logo.setPreserveRatio(true);

        Text title = new Text("Autism Pet Simulator");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #4CAF50;");

        Text subtitle = new Text("A gentle friend for emotion practice 🐾");
        subtitle.setStyle("-fx-font-size: 18px; -fx-fill: #666;");

        Button startButton = new Button("Start Gentle Adventure 🌟");
        startButton.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-background-color: linear-gradient(#4CAF50, #45a049); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12 30; " +
                        "-fx-background-radius: 25;");
        startButton.setOnAction(e -> {
            stage.close();
            new AutismPetSimulator().start(new Stage());
        });

        root.getChildren().addAll(logo, title, subtitle, startButton);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Welcome");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}