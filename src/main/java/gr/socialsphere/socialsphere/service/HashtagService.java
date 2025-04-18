package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagService {

    @Autowired
    private HashtagRepository hashtagRepository;

    public List<Post> getPostsByHashtag(String hashtag) {
        return hashtagRepository.findPostsByHashtag(hashtag);
    }

    public List<Post> getPostsByMultipleHashtags(List<String> hashtags, long minCount) {
        return hashtagRepository.findPostsByMultipleHashtags(hashtags, minCount);
    }
}
