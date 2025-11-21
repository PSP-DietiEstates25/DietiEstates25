package com.dietiestates.resource_server.exception.notfound;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class ImageDataNotFoundException extends AppException {

    public ImageDataNotFoundException(){
        super(BusinessErrorCodes.IMAGE_DATA_NOT_FOUND);
    }
}
