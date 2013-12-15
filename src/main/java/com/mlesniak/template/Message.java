package com.mlesniak.template;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * This model is used both as the DO and (currently) as the DTO for the web frontend. Hence it supports a clone-
 * constructor to allow for the creation of a clone before persisting.
 */
@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    public Message() {
        // Empty.
    }

    public Message(Message that) {
        message = that.message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
