package com.lisoft.autapi.domain.exceptions;

public class ConflictWithExistingResourcesException extends RuntimeException {
    public ConflictWithExistingResourcesException(String message) {
        super(message);
    }

}
