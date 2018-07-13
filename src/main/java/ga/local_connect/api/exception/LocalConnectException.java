package ga.local_connect.api.exception;

import ga.local_connect.api.enumeration.APIErrorType;

public class LocalConnectException extends Exception {
    private final int status;
    private final APIErrorType code;

    public LocalConnectException(int status, APIErrorType code) {
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public APIErrorType getCode() {
        return code;
    }
}
