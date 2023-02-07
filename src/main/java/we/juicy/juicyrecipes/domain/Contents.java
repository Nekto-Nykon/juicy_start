package we.juicy.juicyrecipes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Entity
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Ingredient ingredient;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private RecipeUser recipeUser;

    public void setAmount(Double amount) {
        this.amount = new BigDecimal(amount);
    }

    public Double getAmount() {
        if (amount == null)
            return .0;
        return amount.doubleValue();
    }

    @Override
    public String toString() {
        return "Contents{" +
                "id=" + id +
                ", ingredient=" + ingredient +
                ", amount=" + amount +
                ", recipe=" + recipe +
                '}';
    }
}
