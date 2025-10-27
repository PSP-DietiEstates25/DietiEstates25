package com.dietiestates.resource_server.exception.notowned;

import com.dietiestates.resource_server.enums.BusinessErrorCodes;
import com.dietiestates.resource_server.exception.AppException;

public class CadastralFilterNotOwnedBySearchException extends AppException {

    private static final long serialVersionUID = -5111554009412342855L;

    public CadastralFilterNotOwnedBySearchException() {
        super(BusinessErrorCodes.CADASTRAL_FILTER_NOT_OWNED_BY_SEARCH);
    }
}
