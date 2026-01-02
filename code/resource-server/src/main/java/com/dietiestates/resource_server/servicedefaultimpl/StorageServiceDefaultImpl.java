package com.dietiestates.resource_server.servicedefaultimpl;

import com.dietiestates.resource_server.config.ResourceServerProperties;
import com.dietiestates.resource_server.factory.FileDataFactory;
import com.dietiestates.resource_server.factory.ImageDataFactory;
import com.dietiestates.resource_server.finder.FileDataFinder;
import com.dietiestates.resource_server.finder.ImageDataFinder;
import com.dietiestates.resource_server.model.FileData;
import com.dietiestates.resource_server.repository.FileDataRepository;
import com.dietiestates.resource_server.repository.ImageDataRepository;
import com.dietiestates.resource_server.service.StorageService;
import com.dietiestates.resource_server.utils.ImageUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceDefaultImpl implements StorageService {

    private final ResourceServerProperties properties;

    private final ImageDataFinder imageDataFinder;
    private final FileDataFinder fileDataFinder;
    private final ImageDataFactory imageDataFactory;
    private final FileDataFactory fileDataFactory;
    private final ImageDataRepository imageDataRepository;
    private final FileDataRepository fileDataRepository;

    @PostConstruct
    public void init() throws IOException {
        Path folder = Paths.get(properties.imagesFolderPath());
        Files.createDirectories(folder);
    }

    @Override
    public String uploadImage(MultipartFile imageFile) throws IOException {
        var image = imageDataFactory.createImage(imageFile);
        imageDataRepository.save(image);
        return null;
    }

    @Override
    public byte[] downloadImage(String fileName){
        var image = imageDataFinder.getByName(fileName);
        return ImageUtils.decompressImage(image.getValue());
    }

    @Override
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {

        String originalFilenameRaw =
                Objects.requireNonNull(file.getOriginalFilename(), "Missing original filename");

        String originalFilename = StringUtils.cleanPath(originalFilenameRaw);

        String ext = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < originalFilename.length() - 1) {
            ext = originalFilename.substring(dotIndex);
        }

        String storedFileName = UUID.randomUUID() + ext;

        Path folder = Paths.get(properties.imagesFolderPath());
        Files.createDirectories(folder);

        Path filePath = folder.resolve(storedFileName);

        file.transferTo(filePath.toFile());

        var fileData = fileDataFactory.createFileData(file, filePath.toString());
        fileDataRepository.save(fileData);

        return "/images/" + storedFileName;
    }


    @Override
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        var fileData = fileDataFinder.getByName(fileName);
        var filePath = fileData.getPath();
        return Files.readAllBytes(new File(filePath).toPath());
    }

    @Override
    public void deleteImageFromFileSystem(String imageUrl) throws IOException {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        Path folder = Paths.get(properties.imagesFolderPath());
        Path filePath = folder.resolve(fileName);
        String absolutePathStr = filePath.toString();

        Files.deleteIfExists(filePath);

        Optional<FileData> fileDataOpt = fileDataRepository.findByPath(absolutePathStr);

        if (fileDataOpt.isPresent()) {
            fileDataRepository.delete(fileDataOpt.get());
        }
    }
}
