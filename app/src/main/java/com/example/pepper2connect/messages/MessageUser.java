package com.example.pepper2connect.messages;

public class MessageUser extends Message{
    private String strTitle;
    private String strFirstname;
    private String strLastname;
    private int intUserID;
    private String strPicture;
    private int intRoleID;

    public MessageUser(int intUserID,String strTitle, String strFirstname, String strLastname) {
        super(MessageType.User);
        this.intUserID = intUserID;
        this.strTitle = strTitle;
        this.strFirstname = strFirstname;
        this.strLastname = strLastname;
        this.strPicture = strPicture;
        this.intRoleID = intRoleID;
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

    public String getStrPicture() {
        return strPicture;
    }

    public int getIntRoleID() {
        return intRoleID;
    }


}
