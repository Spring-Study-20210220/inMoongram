package com.team.comment;

import com.team.comment.dto.request.CommentSaveRequest;
import com.team.comment.dto.response.CommentInfoResponse;
import com.team.comment.dto.response.CommentSaveResponse;
import com.team.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<CommentSaveResponse> saveComment(@CurrentUser Long userId,
                                                           @RequestBody CommentSaveRequest request) {
        var output = commentService.saveComment(userId, request.toServiceDto());
        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(CommentController.class)
                        .saveComment(userId, request))
                .build();
        return ResponseEntity
                .created(uriComponents.toUri())
                .body(new CommentSaveResponse(output));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/comments")
    public ResponseEntity<CommentInfoResponse> getComments(@RequestParam("postId") Long postId) {
        var output = commentService.getComments(postId);
        return ResponseEntity
                .ok(new CommentInfoResponse(output));
    }
}
