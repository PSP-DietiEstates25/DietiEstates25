package com.dietiestates.auth.exception;

import com.dietiestates.auth.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;

import com.dietiestates.auth.enums.BusinessErrorCodes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 484890383291704422L;
    private final int businessErrorCode;
    private final HttpStatus httpErrorStatusCode;

    public AppException(BusinessErrorCodes error) {
        super(error.getMessage());
        this.businessErrorCode = error.getCode();
        this.httpErrorStatusCode = error.getHttpStatus();
    }

    public static <T extends AppException> ResponseEntity<ExceptionResponse> responseEntityFactory(T appException){
        return ResponseEntity.status(appException.getHttpErrorStatusCode()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(appException.getBusinessErrorCode())
                        .businessErrorMessage(appException.getMessage())
                        .build()
        );
    }
}
