package com.ajou.travely.service;

import com.ajou.travely.domain.Event;
import com.ajou.travely.domain.Notice;
import com.ajou.travely.domain.Photo;
import com.ajou.travely.domain.Post;
import com.ajou.travely.repository.PhotoRepository;
import java.util.List;
import java.util.Optional;
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
            s3Service.uploadFiles(photos)
                .stream()
                .map(photoName -> new Photo(post, photoName))
                .collect(Collectors.toList())
        );
    }

    public void removePhotos(List<Photo> photos) {
        List<String> photoNames = photos
            .stream()
            .map(Photo::getName)
            .collect(Collectors.toList());
        photoRepository.deleteAllById(photos
            .stream()
            .map(Photo::getId)
            .collect(Collectors.toList()));
        photoNames.forEach(s3Service::deleteFile);
    }

    public void removePhotoIds(List<Long> photoIds) {
        List<Photo> validPhotos = photoRepository.findAllById(photoIds);
        if (photoIds.size() != validPhotos.size()) {
            throw new IllegalArgumentException("Invalid photo ids");
        }
        removePhotos(validPhotos);
    }

    // notice

    public List<Photo> createPhotosOfNotice(Notice notice, List<MultipartFile> photos) {
        return photoRepository.saveAll(
                s3Service.uploadFiles(photos)
                        .stream()
                        .map(photoName -> new Photo(notice, photoName))
                        .collect(Collectors.toList())
        );
    }

    // event

    public List<Photo> createPhotosOfEvent(Event event, List<MultipartFile> photos) {
        return photoRepository.saveAll(
                s3Service.uploadFiles(photos)
                        .stream()
                        .map(photoName -> new Photo(event, photoName))
                        .collect(Collectors.toList())
        );
    }

    // for Refactor

    public <T> void createPhotosOfSomething(T record, List<MultipartFile> photos) {
        photoRepository.saveAll(
                s3Service.uploadFiles(photos)
                        .stream()
                        .map(photoName -> new Photo(record, photoName))
                        .collect(Collectors.toList())
        );
    }
}
