package com.team.post;

import com.team.post.dto.output.SavePostOutput;
import com.team.post.dto.request.SavePostRequest;
import com.team.post.dto.response.SavePostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping(value = "")
    public ResponseEntity<SavePostResponse> savePost(@Valid @ModelAttribute SavePostRequest request) {
        SavePostOutput output = postService.save(request.toInput());
        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(PostController.class).savePost(request))
                .build();

        return ResponseEntity
                .created(uriComponents.toUri())
                .body(new SavePostResponse(output));
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<Void> deletePost(@PathVariable("post-id") Long postId) {
        postService.delete(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
