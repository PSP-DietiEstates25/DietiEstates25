package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.ImageDataNotFoundException;
import com.dietiestates.resource_server.finder.ImageDataFinder;
import com.dietiestates.resource_server.model.ImageData;
import com.dietiestates.resource_server.repository.ImageDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageDataFinderDefaultImpl implements ImageDataFinder {

    private final ImageDataRepository imageDataRepository;

    @Override
    public ImageData getByName(String name){
        return imageDataRepository.findByName(name)
                .orElseThrow(ImageDataNotFoundException::new);
    }
}
