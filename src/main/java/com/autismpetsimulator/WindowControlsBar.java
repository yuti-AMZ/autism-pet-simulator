package com.autismpetsimulator;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class WindowControlsBar extends HBox {
    public WindowControlsBar(Stage stage, Runnable onBack) {
        setSpacing(10);
        setAlignment(Pos.CENTER_RIGHT);
        setStyle("-fx-background-color: transparent;");

        Button backButton = new Button("←");
        backButton.setOnAction(e -> {
            if (onBack != null)
                onBack.run();
        });
        backButton.setTooltip(new javafx.scene.control.Tooltip("Back"));

        Button minButton = new Button("_ ");
        minButton.setOnAction(e -> stage.setIconified(true));
        minButton.setTooltip(new javafx.scene.control.Tooltip("Minimize"));

        Button maxButton = new Button("□");
        maxButton.setOnAction(e -> {
            stage.setMaximized(!stage.isMaximized());
        });
        maxButton.setTooltip(new javafx.scene.control.Tooltip("Maximize/Restore"));

        Button closeButton = new Button("✕");
        closeButton.setOnAction(e -> stage.close());
        closeButton.setTooltip(new javafx.scene.control.Tooltip("Close"));

        getChildren().addAll(backButton, minButton, maxButton, closeButton);
    }
}
