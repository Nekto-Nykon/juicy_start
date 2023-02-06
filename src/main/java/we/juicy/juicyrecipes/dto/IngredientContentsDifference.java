package we.juicy.juicyrecipes.dto;

import we.juicy.juicyrecipes.domain.Ingredient;


public record IngredientContentsDifference(Ingredient ingredient, Integer difference) {
}
