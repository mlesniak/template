package com.mlesniak.template.sms;

public class SMSMessage {
    private String to;
    private String messageId;
    private String status;
    private String remainingBalance;
    private String messagePrice;
    private String network;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getMessagePrice() {
        return messagePrice;
    }

    public void setMessagePrice(String messagePrice) {
        this.messagePrice = messagePrice;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    public String toString() {
        return "SMSMessage{" +
                "to='" + to + '\'' +
                ", messageId='" + messageId + '\'' +
                ", status='" + status + '\'' +
                ", remainingBalance='" + remainingBalance + '\'' +
                ", messagePrice='" + messagePrice + '\'' +
                ", network='" + network + '\'' +
                '}';
    }
}
