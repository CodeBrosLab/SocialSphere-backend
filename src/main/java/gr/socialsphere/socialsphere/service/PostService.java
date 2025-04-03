package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public void addPost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }
}
