package com.ajou.travely.service;

import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.ImageTypeException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<String> uploadFiles(List<MultipartFile> files) {
        return files
            .stream()
            .map(this::uploadFile)
            .collect(Collectors.toList());
    }

    public String uploadFile(MultipartFile file) {
        isImage(file);

        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "?????? ???????????? ??????????????????.");
        }

        return getFileUrl(fileName);
    }

    private String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void isImage(MultipartFile file) {
        if(!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            throw new ImageTypeException(
                "????????? ????????? ????????????.",
                ErrorCode.INVALID_FILE_TYPE
            );
        }
    }

    public void deleteFile(String fileName) {
        if (!amazonS3.doesObjectExist(bucket, fileName)) {
            throw new AmazonS3Exception("Object " + fileName + " does not exist!");
        }
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String fileName) { // ?????? ????????? ???, ???????????? ??????????????? ?????? random ??????
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file ????????? ????????? ????????? ???????????? ?????? ???????????? ????????????, ?????? ????????? ???????????? ???????????? ??? ?????? ?????? ?????? .??? ?????? ????????? ?????????????????????.
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "????????? ????????? ??????(" + fileName + ") ?????????.");
        }
    }

}
