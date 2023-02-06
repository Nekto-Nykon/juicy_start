package we.juicy.juicyrecipes.service;

import we.juicy.juicyrecipes.domain.Contents;
import we.juicy.juicyrecipes.domain.Recipe;
import we.juicy.juicyrecipes.dto.IngredientContentsDifference;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RecipeService {
    Set<Recipe> findAll();
    Optional<Recipe> findById(Integer id);
    Recipe findOneByName(String name);
    Recipe addIngredient(Integer recipeId, Contents contents);
    Recipe save(Recipe recipe);
    List<IngredientContentsDifference> findMissingIngredientAndAmount(Integer recipeId);

}

