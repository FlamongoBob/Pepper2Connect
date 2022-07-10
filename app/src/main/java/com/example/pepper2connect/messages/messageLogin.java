package com.example.pepper2connect.messages;

public class messageLogin extends Message {

    private String strName;
    private String strPassword;

    public messageLogin(String strName, String strPassword, messageType messageType) {
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
