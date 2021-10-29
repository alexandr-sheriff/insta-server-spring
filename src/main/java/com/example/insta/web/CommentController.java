package com.example.insta.web;

import com.example.insta.dto.CommentDTO;
import com.example.insta.dto.PostDTO;
import com.example.insta.entity.Comment;
import com.example.insta.entity.Post;
import com.example.insta.facade.CommentFacade;
import com.example.insta.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments/")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final CommentFacade commentFacade;

    @Autowired
    public CommentController(CommentService commentService, CommentFacade commentFacade) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
    }

    @PostMapping("{postId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable("postId") Long postId, @Valid @RequestBody CommentDTO commentDTO, Principal principal) {
        Comment comment = commentService.saveComment(postId, commentDTO, principal);
        return new ResponseEntity<>(getCommentDTO(comment), HttpStatus.CREATED);
    }

    @GetMapping("{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable("postId") Long postId) {
        List<CommentDTO> commentsDTO = commentService.getAllCommentsForPost(postId)
                .stream()
                .map(commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Principal principal) {
        commentService.deleteComment(commentId, principal);
        return ResponseEntity.noContent().build();
    }

    private CommentDTO getCommentDTO(Comment comment) {
        return commentFacade.commentToCommentDTO(comment);
    }


}
