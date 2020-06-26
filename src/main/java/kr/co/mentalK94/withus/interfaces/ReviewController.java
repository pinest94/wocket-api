package kr.co.mentalK94.withus.interfaces;

import io.jsonwebtoken.Claims;
import kr.co.mentalK94.withus.applications.ReviewService;
import kr.co.mentalK94.withus.applications.UserService;
import kr.co.mentalK94.withus.domains.Review;
import kr.co.mentalK94.withus.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<?> create(Authentication authentication,
                                    @PathVariable("productId") Long productId,
                                    @RequestBody Review resource)
            throws URISyntaxException {

        Claims claims = (Claims) authentication.getPrincipal();

        String name = claims.get("name", String.class);

        Review review = Review.builder().productId(productId)
                                        .rating(resource.getRating())
                                        .description(resource.getDescription())
                                        .writer(name)
                                        .build();

        reviewService.addReview(review);

        URI location = new URI("/products/" + productId + "/reviews/" + review.getId());
        return ResponseEntity.created(location).body(review.getId());
    }
}
