package com.pokemonreview.api.repository;

import com.pokemonreview.api.TestDataUtil;
import com.pokemonreview.api.models.Pokemon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository underTest;

    @Test
    public void verify_PokemonRepository_SaveAll_ReturnsSavedPokemon() {
        //Arrange
        Pokemon pikachu = TestDataUtil.pikachu();

        //Act
        Pokemon result = underTest.save(pikachu);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
        assertThat(result.getName()).isEqualTo("Pikachu");
        assertThat(result.getType()).isEqualTo("Electric");
    }

    @Test
    public void verify_PokemonRepository_FindAll_ReturnsMoreThanOnePokemon() {
        //Arrange
        Pokemon pikachu = TestDataUtil.pikachu();
        Pokemon charmander = TestDataUtil.charmander();

        underTest.save(pikachu);
        underTest.save(charmander);

        //Act
        List<Pokemon> result = underTest.findAll();

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void verify_PokemonRepository_FindById_ReturnsPokemonNotNull() {
        //Arrange
        Pokemon pikachu = TestDataUtil.pikachu();

        Pokemon savedPikachu = underTest.save(pikachu);

        Integer id = savedPikachu.getId();

        //Act
        Pokemon result = underTest.findById(id).get();

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_PokemonRepository_FindByType_ReturnsPokemonNotNull() {
        //Arrange
        Pokemon pikachu = TestDataUtil.pikachu();

        underTest.save(pikachu);

        //Act
        Pokemon result = underTest.findByType(pikachu.getType()).get();

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_PokemonRepository_Update_ReturnsUpdatedPokemon() {
        //Arrange
        Pokemon pikachu = TestDataUtil.pikachu();

        underTest.save(pikachu);

        Pokemon existingPokemon = underTest.findById(pikachu.getId()).get();

        String newName = "Bulbasaur";
        String newType = "Grass";

        existingPokemon.setName(newName);
        existingPokemon.setType(newType);

        //Act
        Pokemon result = underTest.save(existingPokemon);

        //Assert
        assertThat(result.getName()).isEqualTo(newName);
        assertThat(result.getType()).isEqualTo(newType);
    }

    @Test
    public void verify_PokemonRepository_DeleteById_ReturnsEmpty() {
        //Arrange
        Pokemon pikachu = TestDataUtil.pikachu();

        underTest.save(pikachu);

        //Act
        underTest.deleteById(pikachu.getId());
        Optional<Pokemon> result = underTest.findById(pikachu.getId());

        //Assert
        assertThat(result).isEmpty();
    }
}
