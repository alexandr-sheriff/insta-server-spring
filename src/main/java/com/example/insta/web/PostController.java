package com.example.insta.web;

import com.example.insta.dto.PostDTO;
import com.example.insta.dto.UserDTO;
import com.example.insta.entity.Post;
import com.example.insta.entity.User;
import com.example.insta.facade.PostFacade;
import com.example.insta.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts/")
@CrossOrigin
public class PostController {

    private final PostService postService;
    private final PostFacade postFacade;

    @Autowired
    public PostController(PostService postService, PostFacade postFacade) {
        this.postService = postService;
        this.postFacade = postFacade;
    }

    @PostMapping()
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO, Principal principal) {
        Post post = postService.createPost(postDTO, principal);
        return new ResponseEntity<>(getPostDTO(post), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postsDTO = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @GetMapping("users/current-user")
    public ResponseEntity<List<PostDTO>> getAllPostsForUser(Principal principal) {
        List<PostDTO> postsDTO = postService.getAllPostsForUser(principal)
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @PostMapping("{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") Long postId, @PathVariable("username") String username) {
        Post post = postService.likePost(postId, username);
        return new ResponseEntity<>(getPostDTO(post), HttpStatus.OK);
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId, Principal principal) {
        postService.deletePost(postId, principal);
        return ResponseEntity.noContent().build();
    }

    private PostDTO getPostDTO(Post post) {
        return postFacade.postToPostDTO(post);
    }

}
