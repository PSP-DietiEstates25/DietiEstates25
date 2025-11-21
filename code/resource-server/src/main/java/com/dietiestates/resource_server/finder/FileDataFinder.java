package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.model.FileData;
import com.dietiestates.resource_server.model.ImageData;

public interface FileDataFinder {
    FileData getByName(String name);
}
