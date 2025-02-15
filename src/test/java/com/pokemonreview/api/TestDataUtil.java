package com.pokemonreview.api;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;

public class TestDataUtil {

    public static Pokemon pikachu() {
        return Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();
    }

    public static Pokemon charmander() {
        return Pokemon.builder()
                .name("Charmander")
                .type("Fire")
                .build();
    }

    public static Review review() {
        return Review.builder()
                .title("Review Title")
                .content("Review Content")
                .stars(5)
                .build();
    }
}
