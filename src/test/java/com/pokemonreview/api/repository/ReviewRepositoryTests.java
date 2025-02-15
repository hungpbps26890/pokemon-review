package com.pokemonreview.api.repository;

import com.pokemonreview.api.TestDataUtil;
import com.pokemonreview.api.models.Review;
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
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository underTest;

    @Test
    public void verify_ReviewRepository_SaveAll_ReturnsSavedReview() {
        //Arrange
        Review review = TestDataUtil.review();

        //Act
        Review result = underTest.save(review);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
    }

    @Test
    public void verify_ReviewRepository_FindAll_ReturnsMoreThanOneReview() {
        //Arrange
        Review review1 = TestDataUtil.review();
        Review review2 = TestDataUtil.review();

        underTest.save(review1);
        underTest.save(review2);

        //Act
        List<Review> result = underTest.findAll();

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void verify_ReviewRepository_FindById_ReturnsReview() {
        //Arrange
        Review review = TestDataUtil.review();

        underTest.save(review);

        //Act
        Review result = underTest.findById(review.getId()).get();

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(review.getTitle());
        assertThat(result.getContent()).isEqualTo(review.getContent());
        assertThat(result.getStars()).isEqualTo(review.getStars());
    }

    @Test
    public void verify_ReviewRepository_Update_ReturnsUpdatedReview() {
        //Arrange
        Review review = TestDataUtil.review();

        underTest.save(review);

        Review existingReview = underTest.findById(review.getId()).get();

        String newTitle = "New Review Title";
        String newContent = "New Review Content";
        int newStars = 1;

        existingReview.setTitle(newTitle);
        existingReview.setContent(newContent);
        existingReview.setStars(newStars);

        //Act
        Review result = underTest.save(existingReview);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(newTitle);
        assertThat(result.getContent()).isEqualTo(newContent);
        assertThat(result.getStars()).isEqualTo(newStars);
    }

    @Test
    public void verify_ReviewRepository_DeleteById_ReturnsEmpty() {
        //Arrange
        Review review = TestDataUtil.review();

        underTest.save(review);

        int id = review.getId();

        //Act
        underTest.deleteById(id);
        Optional<Review> result = underTest.findById(id);

        //Assert
        assertThat(result).isEmpty();
    }
}
