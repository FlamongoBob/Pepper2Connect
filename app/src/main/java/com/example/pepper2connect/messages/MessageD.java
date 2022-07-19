package com.example.pepper2connect.messages;

public class MessageD extends Message {
    private int intUserID = -1, intEmployeeID = -1, intPictureID = -1;

    public MessageD(int intEmployeeID
            , int intUserID
            , int intPictureID) {

        super(MessageType.DeleteUser);
        this.intEmployeeID = intEmployeeID;
        this.intUserID = intUserID;
        this.intPictureID = intPictureID;

    }

    @Override
    public String toString() {
        return type.toString() +
                '|' + intEmployeeID +
                '|' + intUserID +
                '|' + intPictureID;
    }

    public int getIntEmployeeID() {
        return intEmployeeID;
    }

    public int getIntUserID() {
        return intUserID;
    }

    public int getIntPictureID() {
        return intPictureID;
    }
}