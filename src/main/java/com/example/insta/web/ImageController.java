package com.example.insta.web;

import com.example.insta.entity.Image;
import com.example.insta.payload.response.MessageResponse;
import com.example.insta.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/images/")
@CrossOrigin
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("profileImage")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file")MultipartFile file, Principal principal) throws IOException {
        imageService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @PostMapping("{postId}")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") Long postId, @RequestParam("file")MultipartFile file, Principal principal) throws IOException {
        imageService.uploadImageToPost(file, postId, principal);
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully for Post: " + postId));
    }

    @GetMapping("profileImage")
    public ResponseEntity<Image> getImageForUser(Principal principal) {
        Image userImage = imageService.getImageToUser(principal);
        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @GetMapping("{postId}")
    public ResponseEntity<Image> getImageToPost(@PathVariable("postId") Long postId) {
        Image postImage = imageService.getImageToPost(postId);
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }

}
