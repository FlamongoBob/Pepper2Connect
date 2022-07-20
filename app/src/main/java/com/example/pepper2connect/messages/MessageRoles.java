package com.example.pepper2connect.messages;

public class MessageRoles extends Message{
    int intRoleID;
    String strRole;
    public MessageRoles(int intRoleID, String strRole){
        super(MessageType.Roles);
        this.intRoleID=intRoleID;
        this.strRole=strRole;
    }
    @Override
    public String toString() {
        return type.toString() + '|' + intRoleID + '|' + strRole;
    }

    public int getIntRoleID() {
        return intRoleID;
    }

    public String getStrRole() {
        return strRole;
    }
}
