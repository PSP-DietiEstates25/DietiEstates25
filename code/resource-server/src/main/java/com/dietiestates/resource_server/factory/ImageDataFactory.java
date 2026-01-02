package com.dietiestates.resource_server.factory;

import com.dietiestates.resource_server.model.ImageData;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ImageDataFactory {
    ImageData createImage(MultipartFile file) throws IOException;
}
