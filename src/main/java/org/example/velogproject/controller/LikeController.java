package org.example.velogproject.controller;

import org.example.velogproject.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/toggle/{postId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long userId = getUserIdFromUserDetails(userDetails); // Implement this method to get the userId
            boolean liked = likeService.toggleLike(postId, userId);
            return ResponseEntity.ok().body("{\"success\": true, \"liked\": " + liked + "}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        long count = likeService.getLikeCount(postId);
        return ResponseEntity.ok(count);
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        // Implement this method to extract userId from UserDetails
        return null; // Replace this with the actual implementation
    }
}
