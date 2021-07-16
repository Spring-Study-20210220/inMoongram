package com.team.comment;

import com.team.comment.dto.output.CommentLikePlusOutput;
import com.team.comment.dto.request.CommentLikePlusRequest;
import com.team.comment.dto.response.CommentLikePlusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/comment-like")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("")
    public ResponseEntity<CommentLikePlusResponse> like(@Valid @RequestBody CommentLikePlusRequest request) {
        CommentLikePlusOutput output = commentLikeService.like(request.toInput());
        UriComponents uriComponent = MvcUriComponentsBuilder.fromMethodCall(on(CommentLikeController.class).like(request))
                .build();
        return ResponseEntity
                .created(uriComponent.toUri())
                .body(new CommentLikePlusResponse(output));
    }
}
