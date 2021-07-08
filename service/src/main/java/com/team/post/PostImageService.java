package com.team.post;

import com.team.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostImageService {
    private final PostImageRepository postImageRepository;

    public PostImage save(PostImage postImage) {
        return postImageRepository.save(postImage);
    }

    public List<PostImage> findImagesByIds(List<Long> postImageId) {
        return postImageId.stream()
                .map(id -> postImageRepository.findById(id)
                .orElseThrow(
                        IdNotFoundException::new
                )).collect(Collectors.toList());
    }
}
