package com.pokemonreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.TestDataUtil;
import com.pokemonreview.api.controllers.PokemonController;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.service.PokemonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTests {

    public static final String BASED_URL = "/api/pokemon";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pokemon pokemon;
    private PokemonDto pokemonDto;

    @BeforeEach
    public void init() {
        pokemon = TestDataUtil.pikachu();
        pokemonDto = TestDataUtil.pikachuDto();
    }

    @Test
    public void verify_PokemonController_CreatePokemon_ReturnsHttp201Created() throws Exception {
        //Arrange
        when(pokemonService.createPokemon(any(PokemonDto.class)))
                .thenReturn(pokemonDto);

        //Act
        ResultActions result = mockMvc.perform(post(BASED_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto))
        );

        //Assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(pokemon.getName()))
                .andExpect(jsonPath("$.type").value(pokemon.getType()))
                .andDo(print());
    }

    @Test
    public void verify_PokemonController_GetPokemons_ReturnsHttp200OKAndPokemonResponse() throws Exception {
        //Arrange
        PokemonResponse pokemonResponse = PokemonResponse.builder()
                .pageNo(1)
                .pageSize(10)
                .last(true)
                .totalElements(1)
                .totalPages(1)
                .content(List.of(pokemonDto))
                .build();

        when(pokemonService.getAllPokemon(1, 10)).thenReturn(pokemonResponse);

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(BASED_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10")
        );

        //Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(pokemonResponse.getContent().size()))
                .andExpect(jsonPath("$.pageNo").value(pokemonResponse.getPageNo()))
                .andExpect(jsonPath("$.pageSize").value(pokemonResponse.getPageSize()))
                .andExpect(jsonPath("$.totalElements").value(pokemonResponse.getTotalElements()))
                .andExpect(jsonPath("$.last").value(pokemonResponse.isLast()))
                .andDo(print());
    }

    @Test
    public void verify_PokemonController_PokemonDetail_ReturnsHttp200OKAndPokemonDto() throws Exception {
        //Arrange
        int id = 1;
        pokemonDto.setId(id);

        when(pokemonService.getPokemonById(id))
                .thenReturn(pokemonDto);

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(BASED_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pokemonDto.getId()))
                .andExpect(jsonPath("$.name").value(pokemonDto.getName()))
                .andExpect(jsonPath("$.type").value(pokemonDto.getType()))
                .andDo(print());
    }

    @Test
    public void verify_PokemonController_UpdatePokemon_ReturnsHttp200OKAndPokemonDto() throws Exception {
        //Arrange
        int id = 1;
        pokemonDto.setId(id);

        when(pokemonService.updatePokemon(pokemonDto, id))
                .thenReturn(pokemonDto);

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(BASED_URL + "/" + id + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto))
        );

        //Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pokemonDto.getId()))
                .andExpect(jsonPath("$.name").value(pokemonDto.getName()))
                .andExpect(jsonPath("$.type").value(pokemonDto.getType()))
                .andDo(print());
    }

    @Test
    public void verify_PokemonController_DeletePokemon_ReturnsHttp200OKAndString() throws Exception {
        //Arrange
        int id = 1;

        doNothing().when(pokemonService).deletePokemonId(id);

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(BASED_URL + "/" + id + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
        );

        //Assert
        result.andExpect(status().isOk())
                .andExpect(content().string("Pokemon delete"))
                .andDo(print());
    }
}
