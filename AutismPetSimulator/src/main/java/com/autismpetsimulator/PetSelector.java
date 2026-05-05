package com.autismpetsimulator;

public class PetSelector {
    public static Pet choosePet(String type) {
        return switch (type.toLowerCase()) {
            case "cat" -> new Pet("Meow", "Cat"); // Cute meowing cat!
            case "dog" -> new Pet("Bubu", "Dog"); // Playful barking dog!
            case "bunny" -> new Pet("Lala", "Bunny"); // Soft, happy bunny!
            default -> new Pet("Friend", "Unknown");
        };
    }
}
