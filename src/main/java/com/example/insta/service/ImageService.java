package com.example.insta.service;

import com.example.insta.entity.Image;
import com.example.insta.entity.Post;
import com.example.insta.entity.User;
import com.example.insta.exceptions.ResourceNotFoundException;
import com.example.insta.repository.ImageRepository;
import com.example.insta.repository.PostRepository;
import com.example.insta.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {

    public static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserRepository userRepository, PostRepository postRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Image uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        LOG.info("Uploading image profile to User {}", user.getUsername());
        imageRepository.findByUserId(user.getId()).ifPresent(imageRepository::delete);
        Image image = new Image();
        image.setUserId(user.getId());
        image.setImageBytes(compressByte(file.getBytes()));
        image.setName(file.getOriginalFilename());
        return imageRepository.save(image);
    }

    public Image uploadImageToPost(MultipartFile file, Long postId, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
//        Post post = user.getPosts()
//                .stream()
//                .filter(p -> p.getId().equals(postId))
//                .collect(toSinglePostCollector());

        Post post = postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Post can not found for username: " + user.getUsername()));

        Image image = new Image();
        image.setPostId(post.getId());
        image.setImageBytes(compressByte(file.getBytes()));
        image.setName(file.getOriginalFilename());
        LOG.info("Uploading image to Post {}", post.getId());

        return imageRepository.save(image);
    }

    public Image getImageToUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        Image image = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(decompressByte(image.getImageBytes()));
        }
        return image;
    }

    public Image getImageToPost(Long postId) {
        Image image = imageRepository.findByPostId(postId).orElseThrow(() -> new ResourceNotFoundException("Cannot find image to Post: " + postId));
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(decompressByte(image.getImageBytes()));
        }
        return image;
    }

    private byte[] compressByte(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private byte[] decompressByte(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
//        inflater.end();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }


}
