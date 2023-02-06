package we.juicy.juicyrecipes.repository;

import org.springframework.data.repository.CrudRepository;
import we.juicy.juicyrecipes.domain.Contents;

import java.util.List;

public interface ContentsRepository extends CrudRepository<Contents, Integer> {
    List<Contents> findByRecipeUserId(Integer recipeUser);

    List<Contents> findByRecipeId(Integer recipeId);
}
