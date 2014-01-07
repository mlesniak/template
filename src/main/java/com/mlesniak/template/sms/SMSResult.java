package com.mlesniak.template.sms;

import java.util.List;

public class SMSResult {
    private long messageCount;

    private List<SMSMessage> messages;

    public long getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(long messageCount) {
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
