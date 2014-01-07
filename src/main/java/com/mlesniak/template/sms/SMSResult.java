package com.mlesniak.template.sms;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SMSResult {
    @SerializedName("message-count")
    private int messageCount;

    private List<SMSMessage> messages;

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public List<SMSMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<SMSMessage> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "SMSResult{" +
                "messageCount=" + messageCount +
                ", messages=" + messages +
                '}';
    }
}
