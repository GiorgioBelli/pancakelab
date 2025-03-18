package org.pancakelab.model.pancakes;

public enum Ingredient {
    DARK_CHOCOLATE("dark chocolate"),
    WHIPPED_CREAM("whipped cream"),
    HAZELNUTS("hazelnuts"),
    MILK_CHOCOLATE("milk chocolate");

    private final String name;

    Ingredient(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
