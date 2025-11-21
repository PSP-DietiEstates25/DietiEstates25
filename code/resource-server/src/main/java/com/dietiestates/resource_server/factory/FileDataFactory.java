package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileDataFactory {
    FileData createFileData(MultipartFile file, String path) throws IOException;
}
