package gr.socialsphere.socialsphere.controller;

import gr.socialsphere.socialsphere.dto.PostDTO;
import gr.socialsphere.socialsphere.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PutMapping("/edit/{postId}")
    public ResponseEntity<String> editPost(
            @PathVariable Long postId,
            @RequestBody PostDTO postDTO
    ) {
        return postService.editPost(postId, postDTO);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @RequestParam Long creatorId) {
        return postService.deletePost(postId, creatorId);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        return postService.likePost(postId, userId);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> commentOnPost(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestBody String content
    ) {
        return postService.commentOnPost(postId, userId, content);
    }
}