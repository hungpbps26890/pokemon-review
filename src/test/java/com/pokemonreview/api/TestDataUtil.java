package com.pokemonreview.api;

import com.pokemonreview.api.models.Pokemon;

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
}
