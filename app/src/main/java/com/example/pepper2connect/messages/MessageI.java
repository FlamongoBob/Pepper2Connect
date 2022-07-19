package com.example.pepper2connect.messages;

public class MessageI  extends Message {
    private String strTitle, strFirstName, strLastName, strPicture, strUserName, strPassword;
    private int intRoleID = -1, intConfidentialInfoID = -1;

    public MessageI( String strTitle
            , String strFirstName
            , String strLastName

            , String strPicture

            , int intRoleID

            , String strUserName
            , String strPassword

            , int intConfidentialInfoID) {

        super(MessageType.InsertUser);
        this.strTitle = strTitle;
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.strPicture = strPicture;
        this.intRoleID = intRoleID;
        this.strUserName = strUserName;
        this.strPassword = strPassword;
        this.intConfidentialInfoID = intConfidentialInfoID;
    }

    @Override
    public String toString() {
        return type.toString() +
                '|' + strTitle +
                '|' + strFirstName +
                '|' + strLastName +
                '|' + strPicture +
                '|' + intRoleID +
                '|' + strUserName +
                '|' + strPassword +
                '|' + intConfidentialInfoID;
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

    public String getStrUserName() {
        return strUserName;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public int getIntConfidentialInfoID() {
        return intConfidentialInfoID;
    }
}