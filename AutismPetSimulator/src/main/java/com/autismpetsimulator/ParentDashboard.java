package com.autismpetsimulator;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class ParentDashboard {

    public void showStats(List<Pet> pets, DailyGoals goals) {
        Stage stage = new Stage();
        stage.setTitle("Gentle Progress Report 🌱");
        stage.setResizable(true);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Summary Tab
        Tab summaryTab = createSummaryTab(goals);
        tabPane.getTabs().add(summaryTab);

        // Individual Pet Tabs
        for (Pet pet : pets) {
            Tab petTab = createPetProgressTab(pet, goals);
            tabPane.getTabs().add(petTab);
        }

        Scene scene = new Scene(tabPane, 1100, 750);
        stage.setScene(scene);
        stage.show();
    }

    private Tab createSummaryTab(DailyGoals goals) {
        Tab summaryTab = new Tab("📊 Today's Summary");
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f9fbe7;");

        // Header
        Label header = new Label("Daily Gentle Progress Report");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.web("#2E7D32"));

        // Progress explanation
        Label explanation = new Label(
                "This report shows how your child is building:\n" +
                        "❤️ EMOTION SKILLS - Recognizing and responding to feelings\n" +
                        "🤝 BONDING - Creating trusting relationships\n" +
                        "🧠 FOCUS - Concentration through gentle activities\n" +
                        "🌟 STARS - Rewards for kind and consistent care");
        explanation.setFont(Font.font("Arial", 14));
        explanation.setStyle("-fx-background-color: #fff3e0; -fx-padding: 15; -fx-background-radius: 10;");

        // Daily report
        Label reportLabel = new Label(goals.getReport());
        reportLabel.setFont(Font.font("Arial", 16));
        reportLabel.setWrapText(true);
        reportLabel.setStyle(
                "-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10;");

        // Progress tips
        Label tips = new Label(
                "💡 Gentle Tips:\n" +
                        "• Celebrate small successes with praise\n" +
                        "• Try different emotions each day\n" +
                        "• Let them choose their favorite pet\n" +
                        "• Short sessions (10-15 min) work best\n" +
                        "• Follow their lead - let them guide the play");
        tips.setFont(Font.font("Arial", 14));
        tips.setStyle("-fx-background-color: #e8f5e8; -fx-padding: 15; -fx-background-radius: 10;");

        content.getChildren().addAll(header, explanation, reportLabel, tips);
        summaryTab.setContent(content);

        return summaryTab;
    }

    private Tab createPetProgressTab(Pet pet, DailyGoals goals) {
        Tab petTab = new Tab(pet.getName() + " the " + pet.getType() + " 🐾");

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f3e5f5;");

        // Pet header
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        try {
            ImageView petImage = new ImageView();
            petImage.setFitWidth(80);
            petImage.setFitHeight(80);
            headerBox.getChildren().add(petImage);
        } catch (Exception e) {
            Text petEmoji = new Text("🐱");
            petEmoji.setFont(Font.font(40));
            headerBox.getChildren().add(petEmoji);
        }

        VBox petInfo = new VBox(5);
        Label petName = new Label(pet.getName() + " the " + pet.getType());
        petName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        petName.setTextFill(Color.web("#4A148C"));

        HBox stats = new HBox(30);
        stats.getChildren().addAll(
                createStatLabel("❤️ Happiness", pet.getHappiness()),
                createStatLabel("🤝 Bond", pet.getBondLevel()),
                createStatLabel("🧠 Focus", pet.getFocusLevel()));

        petInfo.getChildren().addAll(petName, stats);
        headerBox.getChildren().add(petInfo);
        content.getChildren().add(headerBox);

        // Stars and session info
        HBox bottomStats = new HBox(20);
        bottomStats.setAlignment(Pos.CENTER);

        Label starsLabel = new Label("Total Stars: " + goals.getStars() + " 🌟");
        starsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        starsLabel.setStyle(
                "-fx-background-color: #fff3e0; -fx-padding: 10; -fx-background-radius: 10; -fx-text-fill: #E65100;");

        Label sessionsLabel = new Label(
                "Sessions Today: " + goals.getProgress().values().stream().mapToInt(Integer::intValue).sum());
        sessionsLabel.setFont(Font.font("Arial", 14));
        sessionsLabel.setStyle(
                "-fx-background-color: #e8f5e8; -fx-padding: 10; -fx-background-radius: 10; -fx-text-fill: #2E7D32;");

        bottomStats.getChildren().addAll(starsLabel, sessionsLabel);
        content.getChildren().add(bottomStats);

        petTab.setContent(content);
        return petTab;
    }

    private HBox createStatLabel(String label, int value) {
        HBox statBox = new HBox(5);
        Label statLabel = new Label(label + ": " + value);
        statLabel.setFont(Font.font("Arial", 14));

        // Color coding based on value
        if (value >= 80) {
            statLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        } else if (value >= 50) {
            statLabel.setStyle("-fx-text-fill: #FF9800;");
        } else {
            statLabel.setStyle("-fx-text-fill: #F44336;");
        }

        statBox.getChildren().add(statLabel);
        return statBox;
    }
}