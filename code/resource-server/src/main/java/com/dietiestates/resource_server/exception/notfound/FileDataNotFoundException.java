package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;
import com.dietiestates.resource_server.repository.FileDataRepository;

public class FileDataNotFoundException extends AppException {
    public FileDataNotFoundException() {
        super(BusinessErrorCodes.FILE_DATA_NOT_FOUND);
    }
}
