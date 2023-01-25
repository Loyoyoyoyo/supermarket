package com.lolo.supermarket.exception;

import lombok.Data;

@Data
public class NotEnoughException extends Exception {
    public NotEnoughException(String message) {
        super(message);
    }
}
