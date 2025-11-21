package com.dietiestates.resource_server.finderdefaultimpl;

import com.dietiestates.resource_server.exception.notfound.FileDataNotFoundException;
import com.dietiestates.resource_server.finder.FileDataFinder;
import com.dietiestates.resource_server.model.FileData;
import com.dietiestates.resource_server.repository.FileDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileDataFinderDefaultImpl implements FileDataFinder {

    private final FileDataRepository fileDataRepository;

    @Override
    public FileData getByName(String name){
        return fileDataRepository.findByName(name)
                .orElseThrow(FileDataNotFoundException::new);
    }
}
