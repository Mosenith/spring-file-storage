package com.filestorage.service;

import com.filestorage.domain.FileData;
import com.filestorage.domain.ImageData;
import com.filestorage.repository.FileDataRepository;
import com.filestorage.repository.StorageRepository;
import com.filestorage.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    private StorageRepository storageRepository;    // For database storage

    @Autowired
    private FileDataRepository fileDataRepository;  // For File System Storage

    private final String FOLDER_PATH = "/Users/mosenithheang/Downloads/FileStorage/";

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

    // For File System Storage Approach
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());

        // upload image to file system
        file.transferTo(new File(filePath));

        if(fileData != null) {
            return "File Successfully Uploaded : " + filePath;
        }

        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
