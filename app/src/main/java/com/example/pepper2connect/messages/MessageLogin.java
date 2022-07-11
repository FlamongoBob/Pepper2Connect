package com.example.pepper2connect.messages;

public class MessageLogin extends Message {

    private String strName;
    private String strPassword;

    public MessageLogin(String strName, String strPassword, MessageType messageType) {
        super(messageType);//MessageType.CreatePlayer);

        this.strName = strName;
        this.strPassword=strPassword;
    }

    public String getName() {
        return this.strName;
    }

    public String getPassword() {
        return this.strPassword;
    }

    @Override
    public String toString() {
        return type.toString() + '|' + strName + '|' + strPassword;
    }


}
