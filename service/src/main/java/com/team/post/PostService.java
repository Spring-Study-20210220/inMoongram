package com.team.post;

import com.team.exception.IdNotFoundException;
import com.team.post.dto.input.FeedInput;
import com.team.post.dto.input.SavePostInput;
import com.team.post.dto.output.FeedOutput;
import com.team.post.dto.output.SavePostOutput;
import com.team.tag.PostTaggedKeyword;
import com.team.tag.PostTaggedUser;
import com.team.user.User;
import com.team.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final PostTaggedUserService taggedUserService;
    private final PostTaggedKeywordService postTaggedKeywordService;

    public SavePostOutput save(Long userId, SavePostInput input) {
        User user = userService.findUserById(userId);
        Post post = new Post(input.getContent(), user);
        Post savePost = postRepository.save(post);

        tagUsers(input, savePost);
        tagKeywords(input, savePost);
        uploadImages(input, savePost);
        return new SavePostOutput(savePost);
    }

    public FeedOutput getFeed(FeedInput input) {
        List<Post> feed = postRepository.getFeed(input.getUserId(), input.of());
        return new FeedOutput(feed);
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(IdNotFoundException::new);
        post.delete();
        postRepository.delete(post);
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(IdNotFoundException::new);
    }

    private void tagUsers(SavePostInput input, Post post) {
        List<User> taggedUsers = getTaggedUsers(input.getTaggedUserIds());
        if (taggedUsers != null) {
            List<PostTaggedUser> postTaggedUsers = taggedUserService.tagAll(taggedUsers, post);
            post.addTaggedUsers(postTaggedUsers);
        }
    }

    private void tagKeywords(SavePostInput input, Post post) {
        List<PostTaggedKeyword> postTaggedKeywords = postTaggedKeywordService.tagAll(input.getTaggedKeywords(), post);
        if (postTaggedKeywords != null) {
            post.addTaggedKeywords(postTaggedKeywords);
        }
    }

    private void uploadImages(SavePostInput input, Post post) {
        List<PostImage> postImages = postImageService.storeImages(input.getPostImages());
        post.addImages(postImages);
    }

    private List<User> getTaggedUsers(List<Long> userIds) {
        if (userIds == null) {
            return null;
        }
        return userIds.stream()
                .map(userService::findUserById)
                .collect(Collectors.toList());
    }
}
