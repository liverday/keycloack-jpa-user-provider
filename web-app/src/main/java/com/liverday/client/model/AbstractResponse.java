package com.liverday.client.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractResponse<T> {
    private final T responseBody;
    private final boolean success;
    private final String error;

    public AbstractResponse(boolean success, T responseBody, String error) {
        this.success = success;
        this.responseBody = responseBody;
        this.error = error;
    }
}
