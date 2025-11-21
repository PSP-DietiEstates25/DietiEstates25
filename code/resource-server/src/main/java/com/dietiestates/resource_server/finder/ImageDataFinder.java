package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.model.ImageData;

public interface ImageDataFinder {
    ImageData getByName(String name);
}
