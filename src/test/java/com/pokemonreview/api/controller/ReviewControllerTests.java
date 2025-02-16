package com.pokemonreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.TestDataUtil;
import com.pokemonreview.api.controllers.ReviewController;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.service.ReviewService;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTests {

    public static final String BASED_URL = "/api/pokemon";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReviewDto reviewDto;

    @BeforeEach
    public void init() {
        reviewDto = TestDataUtil.reviewDto();
    }

    @Test
    public void verify_ReviewController_GetReviewsByPokemonId_ReturnsHttp200OKAndReviewDto() throws Exception {
        //Arrange
        int pokemonId = 1;
        when(reviewService.getReviewsByPokemonId(pokemonId))
                .thenReturn(List.of(reviewDto));

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(BASED_URL + "/" + pokemonId + "/reviews")
                .contentType(MediaType.APPLICATION_JSON)
        );

        //Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andDo(print());
    }

    @Test
    public void verify_ReviewController_GetReviewById_ReturnsHttp200AndReviewDto() throws Exception {
        //Arrange
        int pokemonId = 1;
        int reviewId = 1;
        reviewDto.setId(reviewId);

        when(reviewService.getReviewById(reviewId, pokemonId))
                .thenReturn(reviewDto);

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(BASED_URL + "/" + pokemonId + "/reviews/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewDto.getId()))
                .andExpect(jsonPath("$.title").value(reviewDto.getTitle()))
                .andExpect(jsonPath("$.content").value(reviewDto.getContent()))
                .andExpect(jsonPath("$.stars").value(reviewDto.getStars()))
                .andDo(print());
    }

    @Test
    public void verify_ReviewController_CreateReview_ReturnsHttp201CreatedAndReviewDto() throws Exception {
        //Arrange
        int pokemonId = 1;

        when(reviewService.createReview(pokemonId, reviewDto))
                .thenReturn(reviewDto);

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(BASED_URL + "/" + pokemonId + "/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto))
        );

        //Assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reviewDto.getId()))
                .andExpect(jsonPath("$.title").value(reviewDto.getTitle()))
                .andExpect(jsonPath("$.content").value(reviewDto.getContent()))
                .andExpect(jsonPath("$.stars").value(reviewDto.getStars()))
                .andDo(print());
    }

    @Test
    public void verify_ReviewController_UpdateReview_ReturnsHttp200OKAndReviewDto() throws Exception {
        //Arrange
        int pokemonId = 1;
        int reviewId = 1;
        reviewDto.setId(reviewId);

        when(reviewService.updateReview(pokemonId, reviewId, reviewDto))
                .thenReturn(reviewDto);

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(BASED_URL + "/" + pokemonId + "/reviews/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDto))
        );

        //Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewDto.getId()))
                .andExpect(jsonPath("$.title").value(reviewDto.getTitle()))
                .andExpect(jsonPath("$.content").value(reviewDto.getContent()))
                .andExpect(jsonPath("$.stars").value(reviewDto.getStars()))
                .andDo(print());
    }

    @Test
    public void verify_ReviewController_DeleteReview_ReturnsHttp200AndString() throws Exception {
        //Arrange
        int pokemonId = 1;
        int reviewId = 1;

        doNothing().when(reviewService).deleteReview(pokemonId, reviewId);

        //Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(BASED_URL + "/" + pokemonId + "/reviews/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //Assert
        result.andExpect(status().isOk())
                .andExpect(content().string("Review deleted successfully"))
                .andDo(print());
    }
}
