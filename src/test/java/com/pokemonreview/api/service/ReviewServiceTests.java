package com.pokemonreview.api.service;

import com.pokemonreview.api.TestDataUtil;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private ReviewServiceImpl underTest;

    private Pokemon pokemon;
    private PokemonDto pokemonDto;
    private Review review;
    private ReviewDto reviewDto;

    @BeforeEach
    public void init() {
        pokemon = TestDataUtil.pikachu();
        pokemonDto = TestDataUtil.pikachuDto();
        review = TestDataUtil.review();
        reviewDto = TestDataUtil.reviewDto();
    }

    @Test
    public void verify_ReviewService_CreateReview_ReturnsReviewDto() {
        //Arrange
        int pokemonId = 1;

        when(pokemonRepository.findById(pokemonId))
                .thenReturn(Optional.ofNullable(pokemon));

        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);

        //Act
        ReviewDto result = underTest.createReview(pokemonId, reviewDto);

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_ReviewService_GetReviewsByPokemonId_ReturnsListOfReviewDto() {
        //Arrange
        int pokemonId = 1;

        when(reviewRepository.findByPokemonId(pokemonId))
                .thenReturn(List.of(review));

        //Act
        List<ReviewDto> result = underTest.getReviewsByPokemonId(pokemonId);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void verify_ReviewService_GetReviewById_ReturnsReviewDto() {
        //Arrange
        int pokemonId = 1;

        when(pokemonRepository.findById(pokemonId))
                .thenReturn(Optional.ofNullable(pokemon));

        review.setPokemon(pokemon);

        int reviewId = 1;
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.ofNullable(review));

        //Act
        ReviewDto result = underTest.getReviewById(reviewId, pokemonId);

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_ReviewService_UpdateReview_ReturnsReviewDto() {
        //Arrange
        int pokemonId = 1;

        when(pokemonRepository.findById(pokemonId))
                .thenReturn(Optional.ofNullable(pokemon));

        review.setPokemon(pokemon);

        int reviewId = 1;
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.ofNullable(review));

        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);

        //Act
        ReviewDto result = underTest.updateReview(pokemonId, reviewId, reviewDto);

        //Assert
        assertThat(result).isNotNull();
    }

    @Test
    public void verify_ReviewService_DeleteReview_SuccessfullyDeletes() {
        //Arrange
        int pokemonId = 1;

        when(pokemonRepository.findById(pokemonId))
                .thenReturn(Optional.ofNullable(pokemon));

        review.setPokemon(pokemon);

        int reviewId = 1;
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.ofNullable(review));

        //Act and Assert
        Assertions.assertAll(() -> underTest.deleteReview(pokemonId, reviewId));
    }
}
