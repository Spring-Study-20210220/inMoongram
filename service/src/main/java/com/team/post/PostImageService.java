package com.team.post;

import com.team.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostImageService {
    @Value("${file.dir}")
    private String location;

    private final PostImageRepository postImageRepository;

    @Transactional
    public PostImage storeImage(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String uploadFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(uploadFileName);
        String storePath = getFullPath(storeFileName);
        multipartFile.transferTo(new File(storePath));
        return postImageRepository.save(new PostImage(uploadFileName, storeFileName, storePath));
    }

    @Transactional
    public List<PostImage> storeImages(List<MultipartFile> multipartFiles) {
        List<PostImage> postImages = new ArrayList<>();
        multipartFiles.stream()
                .filter(it -> !it.isEmpty())
                .forEach(it -> {
                    try {
                        postImages.add(storeImage(it));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return postImages;
    }

    public List<PostImage> findImagesByIds(List<Long> postImageId) {
        return postImageId.stream()
                .map(id -> postImageRepository.findById(id)
                        .orElseThrow(
                                IdNotFoundException::new
                        )).collect(Collectors.toList());
    }

    private String createStoreFileName(String uploadFileName) {
        String extension = extractExtension(uploadFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + extension;
    }

    // 파일 확장자를 가져오는 메소드
    private String extractExtension(String uploadFileName) {
        int idx = uploadFileName.lastIndexOf(".");
        return uploadFileName.substring(idx + 1);
    }

    private String getFullPath(String filename) {
        String absolutePath = new File(location).getAbsolutePath();
        return absolutePath + "/" + filename;
    }
}
