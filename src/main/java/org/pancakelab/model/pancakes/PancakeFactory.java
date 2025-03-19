package org.pancakelab.model.pancakes;

import java.util.Collection;

public class PancakeFactory {

    public static Pancake custom(Collection<Ingredient> ingredients) {
        return new Pancake(ingredients);
    }

    public static Pancake darkChocolatePancake() {
        return new Pancake(Ingredient.DARK_CHOCOLATE);
    }

    public static Pancake darkChocolateWhippedCreamPancake() {
        return new Pancake(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM);
    }

    public static Pancake darkChocolateWhippedCreamHazelnutsPancake() {
        return new Pancake(Ingredient.DARK_CHOCOLATE, Ingredient.WHIPPED_CREAM, Ingredient.HAZELNUTS);
    }

    public static Pancake milkChocolatePancake() {
        return new Pancake(Ingredient.MILK_CHOCOLATE);
    }

    public static Pancake milkChocolateHazelnutsPancake() {
        return new Pancake(Ingredient.MILK_CHOCOLATE, Ingredient.HAZELNUTS);
    }
}
