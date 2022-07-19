package com.example.pepper2connect.messages;

public class MessageU  extends Message {
    private String strTitle, strFirstName, strLastName, strPicture, strUserName, strPassword;
    private int intUserID = -1, intEmployeeID = -1, intPictureID = -1, intRoleID = -1, intConfidentialID = -1;

    public MessageU( int intEmployeeID
            , String strTitle
            , String strFirstName
            , String strLastName

            , int intPictureID
            , String strPicture

            , int intUserID
            , String strUserName
            , String strPassword

            , int intRoleID
            , int intConfidentialID) {

        super(MessageType.UpdateUser);
        this.intEmployeeID = intEmployeeID;
        this.strTitle = strTitle;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;

        this.intPictureID = intPictureID;
        this.strPicture = strPicture;

        this.intUserID = intUserID;
        this.strUserName = strUserName;
        this.strPassword = strPassword;

        this.intRoleID = intRoleID;
        this.intConfidentialID = intConfidentialID;
    }

    @Override
    public String toString() {
        return type.toString() +
                '|' + intEmployeeID +
                '|' + strTitle +
                '|' + strFirstName +
                '|' + strLastName +

                '|' + intPictureID +
                '|' + strPicture +

                '|' + intUserID +
                '|' + strUserName +
                '|' + strPassword +

                '|' + intRoleID +
                '|' + intRoleID;
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

    public String getStrUserName() {
        return strUserName;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public int getIntUserID() {
        return intUserID;
    }

    public int getIntEmployeeID() {
        return intEmployeeID;
    }

    public int getIntPictureID() {
        return intPictureID;
    }

    public int getIntRoleID() {
        return intRoleID;
    }

    public int getIntConfidentialID() {
        return intConfidentialID;
    }
}
