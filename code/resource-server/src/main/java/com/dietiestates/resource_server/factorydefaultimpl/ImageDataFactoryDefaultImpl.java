package com.dietiestates.resource_server.factorydefaultimpl;

import com.dietiestates.resource_server.factory.ImageDataFactory;
import com.dietiestates.resource_server.model.ImageData;
import com.dietiestates.resource_server.utils.ImageUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Component
public class ImageDataFactoryDefaultImpl implements ImageDataFactory {

    @Override
    public ImageData createImage(MultipartFile file) throws IOException {
        return ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build();
    }
}
