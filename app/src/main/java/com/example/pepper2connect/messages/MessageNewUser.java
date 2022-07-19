package com.example.pepper2connect.messages;


public class MessageNewUser extends Message{
    private String strTitle;
    private String strFirstname;
    private String strLastname;
    private String strUserName;
    private String strPassword;
    private String strPicture;

    public MessageNewUser(String strTitle, String strFirstname, String strLastname, String strPicture, String strUserName, String strPassword) {
        super(MessageType.InsertUser);
        this.strTitle = strTitle;
        this.strFirstname = strFirstname;
        this.strLastname = strLastname;
        this.strUserName = strUserName;
        this.strPassword = strPassword;
        this.strPicture = strPicture;
    }

    @Override
    public String toString() {
        return type.toString() + '|' + strTitle + '|' + strFirstname+ '|' + strLastname+ '|' + strPicture+'|' + strUserName+'|' + strPassword;
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

    public String getStrPicture() {
        return strPicture;
    }

    public String getStrUserName() {
        return strUserName;
    }

    public String getStrPassword() {
        return strPassword;
    }
}
