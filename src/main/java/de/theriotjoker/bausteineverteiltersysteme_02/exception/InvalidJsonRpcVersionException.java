package de.theriotjoker.bausteineverteiltersysteme_02.exception;

public class InvalidJsonRpcVersionException extends RuntimeException {
    public InvalidJsonRpcVersionException(String message) {
        super(message);
    }
}
