package com.game.repository.specifications;

import com.game.entity.Player;
import org.springframework.data.jpa.domain.Specification;

public class PlayerSpecs {

    public static Specification<Player> nameContains(final String word) {
        return (Specification<Player>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),
                "%" + word + "%");
    }

    //public static Specification<Player> titleContains(String word) {
    //  return (Specification<Player>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),
    // "%" + word + "%");
    //}
}
