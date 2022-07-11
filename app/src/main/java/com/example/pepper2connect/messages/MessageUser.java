package com.example.pepper2connect.messages;

public class MessageUser extends Message{
    private String strTitle;
    private String strFirstname;
    private String strLastname;
    private int intUserID;

    public MessageUser(int intUserID,String strTitle, String strFirstname, String strLastname) {
        super(MessageType.User);
        this.intUserID = intUserID;
        this.strTitle = strTitle;
        this.strFirstname = strFirstname;
        this.strLastname = strLastname;
    }

    @Override
    public String toString() {
        return type.toString() + '|' + strTitle + '|' + strFirstname + '|' + strLastname;
    }

    public int getIntUserID() {
        return intUserID;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public String getStrFirstname() {
        return strFirstname;
    }

    public String getStrLastname() {
        return strLastname;
    }




}
