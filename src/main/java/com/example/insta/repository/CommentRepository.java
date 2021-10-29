package com.example.insta.repository;

import com.example.insta.entity.Comment;
import com.example.insta.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    Optional<Comment> findCommentByIdAndUserId(Long commentId, Long userId);

}
