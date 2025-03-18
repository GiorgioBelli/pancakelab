package org.pancakelab.model.pancakes;

import java.util.Collection;
import java.util.List;

public class Pancake implements PancakeRecipe {

    private final List<Ingredient> ingredients;

    public Pancake(Collection<Ingredient> ingredients) {
        this.ingredients = List.copyOf(ingredients);
    }

    public Pancake(Ingredient ...ingredients) {
        this.ingredients = List.of(ingredients);
    }

    @Override
    public List<String> ingredients() {
        return ingredients.stream().map(Ingredient::toString).toList();
    }
}
