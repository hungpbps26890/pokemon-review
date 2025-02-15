package com.pokemonreview.api.service;

import com.pokemonreview.api.TestDataUtil;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTests {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonServiceImpl underTest;

    @Test
    public void verify_PokemonService_CreatePokemon_ReturnsPokemonDto() {
        //Arrange
        Pokemon pokemon = TestDataUtil.pikachu();
        PokemonDto pokemonDto = TestDataUtil.pikachuDto();

        when(pokemonRepository.save(any(Pokemon.class))).thenReturn(pokemon);

        //Act
        PokemonDto result = underTest.createPokemon(pokemonDto);

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_PokemonService_GetAllPokemon_ReturnsPokemonResponse() {
        //Arrange
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        when(pokemonRepository.findAll(any(Pageable.class)))
                .thenReturn(pokemons);

        //Act
        PokemonResponse result = underTest.getAllPokemon(1, 10);

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_PokemonService_GetPokemonById_ReturnsPokemonDto() {
        //Arrange
        Pokemon pokemon = TestDataUtil.pikachu();

        int id = 1;

        when(pokemonRepository.findById(id))
                .thenReturn(Optional.ofNullable(pokemon));

        //Act
        PokemonDto result = underTest.getPokemonById(id);

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_PokemonService_UpdatePokemon_ReturnsPokemonDto() {
        //Arrange
        Pokemon pokemon = TestDataUtil.pikachu();
        PokemonDto pokemonDto = TestDataUtil.pikachuDto();

        int id = 1;

        when(pokemonRepository.findById(id))
                .thenReturn(Optional.ofNullable(pokemon));

        when(pokemonRepository.save(any(Pokemon.class))).thenReturn(pokemon);

        //Act
        PokemonDto result = underTest.updatePokemon(pokemonDto, id);

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_PokemonService_DeletePokemonById_SuccessfullyDeletes() {
        //Arrange
        Pokemon pokemon = TestDataUtil.pikachu();

        int id = 1;

        when(pokemonRepository.findById(id))
                .thenReturn(Optional.ofNullable(pokemon));


        //Assert
        Assertions.assertAll(() -> underTest.deletePokemonId(id));
    }
}
