package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.model.Comment;
import gr.socialsphere.socialsphere.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
