package com.ajou.travely.service;

import com.ajou.travely.controller.photo.dto.SimplePhotoResponseDto;
import com.ajou.travely.domain.Photo;
import com.ajou.travely.domain.Post;
import com.ajou.travely.repository.PhotoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    private final AwsS3Service s3Service;

    public List<Photo> getPhotos(List<Long> photoIds) {
        return photoRepository.findAllById(photoIds);
    }

    public List<String> getPhotoNames(List<Long> photoIds) {
        return photoRepository.findPhotoNamesByIdsInQuery(photoIds);
    }

    public List<Photo> createPhotos(Post post, List<MultipartFile> photos) {
        return photoRepository.saveAll(
            s3Service.uploadFile(photos)
                .stream()
                .map(photoName -> new Photo(post, photoName))
                .collect(Collectors.toList())
        );
    }

    public void removePhotos(List<Long> photoIds) {
        List<String> photoNames = photoRepository.findPhotoNamesByIdsInQuery(photoIds);
        photoRepository.deleteAllById(photoIds);
        photoNames.forEach(s3Service::deleteFile);
    }

    public void updatePhotos(Post post, List<MultipartFile> addPhotos, List<Long> deletePhotoIds) {
        createPhotos(post, addPhotos);
        removePhotos(deletePhotoIds);
    }

}
