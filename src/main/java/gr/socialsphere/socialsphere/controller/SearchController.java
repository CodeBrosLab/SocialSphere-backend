package gr.socialsphere.socialsphere.controller;

import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.service.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private HashtagService hashtagService;

    @GetMapping("/posts-by-hashtag")
    public ResponseEntity<List<Post>> getPostsByHashtag(@RequestParam String hashtag) {
        List<Post> posts = hashtagService.getPostsByHashtag(hashtag);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts-by-hashtags")
    public ResponseEntity<List<Post>> getPostsByHashtags(
            @RequestParam List<String> hashtags,
            @RequestParam(defaultValue = "2") long minCount) {
        List<Post> posts = hashtagService.getPostsByMultipleHashtags(hashtags, minCount);
        return ResponseEntity.ok(posts);
    }
}