package com.dietiestates.resource_server.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadImage(MultipartFile imageFile) throws IOException;
    byte[] downloadImage(String fileName);

    String uploadImageToFileSystem(MultipartFile file) throws IOException;

    byte[] downloadImageFromFileSystem(String fileName) throws IOException;
}
