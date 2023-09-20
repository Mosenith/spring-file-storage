package com.filestorage.service;

import com.filestorage.domain.ImageData;
import com.filestorage.repository.StorageRepository;
import com.filestorage.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    StorageRepository storageRepository;

    public String uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = storageRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressFile(file.getBytes())).build());

        if(imageData != null) {
            return "Image Successfully Uploaded : " + file.getOriginalFilename();
        }

        return null;
    }

    public byte[] downloadImage(String fileName) {
        Optional<ImageData> imageData = storageRepository.findByName(fileName);
        byte[] images = ImageUtils.decompressFile(imageData.get().getImageData());
        return images;
    }
}
