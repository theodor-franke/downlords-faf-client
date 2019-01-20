package com.faforever.client.review;

import com.faforever.client.remote.FafService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class ReviewService {
  private final FafService fafService;

  public ReviewService(FafService fafService) {
    this.fafService = fafService;
  }

  public CompletableFuture<Void> saveGameReview(Review review, int gameId) {
    return fafService.saveGameReview(review, gameId);
  }

  public CompletableFuture<Void> saveModVersionReview(Review review, String modVersionId) {
    return fafService.saveModVersionReview(review, modVersionId);
  }

  public CompletableFuture<Void> saveMapVersionReview(Review review, String mapVersionId) {
    return fafService.saveMapVersionReview(review, mapVersionId);
  }

  public CompletableFuture<Void> deleteGameReview(Review review) {
    return fafService.deleteGameReview(review);
  }

  public CompletableFuture<Void> deleteMapVersionReview(Review review) {
    return fafService.deleteMapVersionReview(review);
  }

  public CompletableFuture<Void> deleteModVersionReview(Review review) {
    return fafService.deleteModVersionReview(review);
  }

  public CompletableFuture<List<Review>> findNonEmptyMapReviews(String id, int count, int page) {
    return fafService.findNonEmptyMapReviews(id, count, page);
  }

  public CompletionStage<List<Review>> findNonEmptyGameReviews(int id, int count, int page) {
    return fafService.findNonEmptyGameReviews(id, count, page);
  }

  public CompletionStage<Optional<ReviewsSummary>> findGameReviewsSummary(int id) {
    return fafService.findGameReviewsSummary(id);
  }

  public CompletionStage<Optional<Review>> findOwnMapReview(String id) {
    return fafService.findOwnMapReview(id);
  }

  public CompletionStage<List<Review>> findNonEmptyModReviews(String id, int count, int page) {
    return fafService.findNonEmptyModReviews(id, count, page);
  }

  public CompletionStage<Optional<ReviewsSummary>> findModReviewsSummary(String id) {
    return fafService.findModReviewsSummary(id);
  }
}
