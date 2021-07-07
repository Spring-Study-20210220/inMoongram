package com.team.post;

import com.team.post.dto.input.PostScrapInput;
import com.team.post.dto.request.PostScrapSaveRequest;
import com.team.post.dto.response.PostScrapSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class PostScrapController {
    private final PostScrapService postScrapService;

    @PostMapping("")
    public ResponseEntity<PostScrapSaveResponse> scrap(@Valid @RequestBody PostScrapSaveRequest request) {
        UriComponents uri = MvcUriComponentsBuilder
                .fromMethodCall(on(PostScrapController.class).scrap(request))
                .build();
        return ResponseEntity
                .created(uri.toUri())
                .body(
                        new PostScrapSaveResponse(
                                postScrapService.postScrap(
                                        new PostScrapInput(request.getUserId(), request.getPostId())
                                )
                        )
                );
    }
}
