package com.example.pepper2connect.messages;

public class messageSystem extends Message {


    private String strSystemNotification;
    public messageSystem(String strSystemNotification){
        super(messageType.System);
        this.strSystemNotification = strSystemNotification;
    }
    public String getStrSystemNotification() {
        return strSystemNotification;
    }

    public void setStrSystemNotification(String strSystemNotification) {
        this.strSystemNotification = strSystemNotification;
    }


}
