package com.team.post;

import com.team.post.dto.input.PostScrapDeleteInput;
import com.team.post.dto.input.PostScrapGetInput;
import com.team.post.dto.input.PostScrapSaveInput;
import com.team.post.dto.request.PostScrapSaveRequest;
import com.team.post.dto.response.PostScrapGetResponse;
import com.team.post.dto.response.PostScrapSaveResponse;
import com.team.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class PostScrapController {
    private final PostScrapService postScrapService;

    @PostMapping("")
    public ResponseEntity<PostScrapSaveResponse> scrap(@NotNull @CurrentUser Long userId,
                                                       @Valid @RequestBody PostScrapSaveRequest request) {
        UriComponents uri = MvcUriComponentsBuilder
                .fromMethodCall(on(PostScrapController.class).scrap(userId, request))
                .build();
        return ResponseEntity
                .created(uri.toUri())
                .body(
                        new PostScrapSaveResponse(
                                postScrapService.postScrap(
                                        new PostScrapSaveInput(userId, request.getPostId())
                                )
                        )
                );
    }

    @GetMapping("")
    public ResponseEntity<PostScrapGetResponse> getScrap(@NotNull @CurrentUser Long userId) {
        return ResponseEntity
                .ok(
                        new PostScrapGetResponse(
                                postScrapService.getScrap(new PostScrapGetInput(userId))
                        )
                );
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<?> unScrap(@NotNull @CurrentUser Long userId, @NotNull @PathVariable(name = "post-id") Long postId) {
        postScrapService.unScrap(new PostScrapDeleteInput(userId, postId));

        return ResponseEntity.noContent()
                .build();
    }
}
