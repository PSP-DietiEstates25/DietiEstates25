package com.dietiestates.resource_server.factorydefaultimpl;

import com.dietiestates.resource_server.factory.FileDataFactory;
import com.dietiestates.resource_server.model.FileData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileDataFactoryDefaultImpl implements FileDataFactory {

    @Override
    public FileData createFileData(MultipartFile file, String path) throws IOException {
        return FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .path(path)
                .build();
    }
}
