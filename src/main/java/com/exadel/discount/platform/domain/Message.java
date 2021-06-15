package com.exadel.discount.platform.domain;

import lombok.Data;

@Data
public class Message {
    private String message;

    public Message(String message){
        this.message = message;
    }
}