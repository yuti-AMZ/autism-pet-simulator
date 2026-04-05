package com.autismpetsimulator;

import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.image.ImageView;

public class EmotionAnimator {
    public static void animate(Pet pet, ImageView view) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), view);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    }
}