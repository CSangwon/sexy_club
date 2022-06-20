package com.example.chattest.global.advice.exception;

public class ChatRoomNameInvalidException extends RuntimeException {
    public ChatRoomNameInvalidException(){}

    public ChatRoomNameInvalidException(String message) {
        super(message);
    }

    public ChatRoomNameInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
