package com.dietiestates.resource_server.finder;

import com.dietiestates.resource_server.model.FileData;

public interface FileDataFinder {
    FileData getByName(String name);
}
