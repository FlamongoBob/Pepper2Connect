package com.example.pepper2connect.messages;

public class MessageSystem extends Message {


    private String strSystemNotification;
    public MessageSystem(String strSystemNotification){
        super(MessageType.System);
        this.strSystemNotification = strSystemNotification;
    }
    public String getStrSystemNotification() {
        return strSystemNotification;
    }

    @Override
    public String toString() {
        return type.toString() + '|' + strSystemNotification;
    }

}
