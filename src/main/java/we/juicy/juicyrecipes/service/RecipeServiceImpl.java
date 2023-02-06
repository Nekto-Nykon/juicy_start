package we.juicy.juicyrecipes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.juicy.juicyrecipes.domain.Contents;
import we.juicy.juicyrecipes.domain.Ingredient;
import we.juicy.juicyrecipes.domain.Recipe;
import we.juicy.juicyrecipes.dto.IngredientContentsDifference;
import we.juicy.juicyrecipes.repository.ContentsRepository;
import we.juicy.juicyrecipes.repository.RecipeRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final ContentsRepository contentsRepository;
    private final SingleUserService userService;

    @Override
    public Set<Recipe> findAll() {
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().forEach(recipes::add);
        return recipes;
    }

    @Override
    public Optional<Recipe> findById(Integer id) {
        return recipeRepository.findById(id);
    }

    @Override
    public Recipe findOneByName(String name) {
        return recipeRepository.findOneByName(name);
    }

    @Transactional
    @Override
    public Recipe addIngredient(Integer recipeId, Contents contents) {
        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty())
            throw new RuntimeException("Recipe with id is not found");

        Recipe recipe = maybeRecipe.get();
        Optional<Contents> ingredientContents = recipe.getNecessaryAmount()
                .stream()
                .filter(it -> it.getIngredient().equals(contents.getIngredient()))
                .findFirst();

        if (ingredientContents.isEmpty()) {
            contents.setRecipe(recipe);
            Contents savedContents = contentsRepository.save(contents);
            recipe.addContents(savedContents);
        } else {
            Contents updatedIngredientContents = ingredientContents.get();
            updatedIngredientContents.setAmount(updatedIngredientContents.getAmount() + contents.getAmount());
            contentsRepository.save(updatedIngredientContents);
        }

        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe save(Recipe recipe){
        return recipeRepository.save(recipe);
    }
    @Override
    public List<IngredientContentsDifference> findMissingIngredientAndAmount(Integer recipeId){
        Optional<Recipe> maybeRecipe = recipeRepository.findById(recipeId);
        if (maybeRecipe.isEmpty()){
            throw new RuntimeException("Recipe is not found");
        }
        List<Contents> recipeContents = maybeRecipe.get().getNecessaryAmount();
        List<Contents> userContents = userService.getCurrentUser().getAmountPresent();
        List<Integer> nessesaryAmount = recipeContents.stream()
                .map(recipeIngred -> showNessesaryAmount(recipeIngred, userContents))
                .collect(Collectors.toList());

        List<IngredientContentsDifference> ingrDiff = new ArrayList<>();
        List<Ingredient> ingrList = recipeContents.stream().map(it -> it.getIngredient()).collect(Collectors.toList());

        for(int i = 0 ; i < ingrList.size(); i++){
            ingrDiff.add(new IngredientContentsDifference(ingrList.get(i),nessesaryAmount.get(i)));
        }
        return ingrDiff;
    }

    private Integer showNessesaryAmount(Contents ingredientContents, List<Contents> userContents){
        Optional<Contents> maybeFindContents = userContents
                .stream()
                .filter(it -> it.getIngredient().equals(ingredientContents.getIngredient()))
                .findFirst();
        if(maybeFindContents.isPresent()){
            Contents findUserContents = maybeFindContents.get();
            Integer result = 0;
            if(findUserContents.getAmount() < ingredientContents.getAmount()){
                result = ingredientContents.getAmount() - findUserContents.getAmount();
                return result;
            }
            return result;
        }
        return ingredientContents.getAmount();
    }

}
