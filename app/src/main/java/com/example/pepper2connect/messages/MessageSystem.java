package com.example.pepper2connect.messages;

public class MessageSystem extends Message {


    private String strSystemNotification;
    public MessageSystem(String strSystemNotification){
        super(messageType.System);
        this.strSystemNotification = strSystemNotification;
    }
    public String getStrSystemNotification() {
        return strSystemNotification;
    }

    public void setStrSystemNotification(String strSystemNotification) {
        this.strSystemNotification = strSystemNotification;
    }
    @Override
    public String toString() {
        return type.toString() + '|' + strSystemNotification;
    }

}
