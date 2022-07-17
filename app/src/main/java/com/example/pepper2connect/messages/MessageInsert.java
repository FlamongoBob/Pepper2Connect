package com.example.pepper2connect.messages;

public class MessageInsert extends Message {
    private String strTitle, strFirstName, strLastName, strPicture, strUserName, strPassword;
    private int intRoleID = -1, intEmployeeID = -1;

    public MessageInsert(MessageType msgType
            , String strTitle
            , String strFirstName
            , String strLastName
            , String strPicture
            , int intRoleID
            , String strUserName
            , String strPassword) {

        super(msgType);
        this.strTitle = strTitle;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strPicture = strPicture;
        this.intRoleID = intRoleID;
        this.strUserName = strUserName;
        this.strPassword = strPassword;
    }

    @Override
    public String toString() {
        return type.toString() +
                '|' + intEmployeeID +
                '|' + strTitle +
                '|' + strFirstName +
                '|' + strLastName +
                '|' + strPicture +
                '|' + intRoleID +
                '|' + strUserName +
                '|' + strPassword;
    }

    public int getIntEmployeeID() {
        return intEmployeeID;
    }

    public void setIntEmployeeID(int intEmployeeID) {
        this.intEmployeeID = intEmployeeID;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public String getStrPicture() {
        return strPicture;
    }

    public int getIntRoleID() {
        return intRoleID;
    }
}
