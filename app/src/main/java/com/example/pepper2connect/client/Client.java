package com.example.pepper2connect.client;

import com.example.pepper2connect.clientmodel.Clientmodel;
import com.example.pepper2connect.controller.Controller;
import com.example.pepper2connect.messages.*;

public class Client {
    private Clientmodel cModel;

    /**
     * Constructor for the client class.
     * Creates a new clientModel, and sends it the Port, the Name, the Password.
     * @param strIP
     * @param intPort
     * @param strUserName
     * @param strPassword
     * @param controller
     */

    public Client(String strIP, int intPort, String strUserName, String strPassword, Controller controller) {
        if (!controller.isClientConnected) {
            cModel = new Clientmodel(controller);
            cModel.StartServer(controller.isClientConnected, strUserName, strPassword,strIP,intPort);

            //cModel.connect(strIP, intPort, strName, strPassword);
            //cModel.newestMessage.addListener((o, oldValue, newValue) -> view.textArea.appendText(newValue));

        }

    }

    /**
     * Function to start the disconnect process from the server. hashem
     *
     */
    public void disconnect(){
        cModel.disconnect();
    }



    /**
     * Sending System Messages
     * @param messageSystem
     *
     */
    public void sendSysMessage(MessageSystem messageSystem) {

        cModel.sendMessage(messageSystem);
    }
    public void sendUpadetMessage(MessageU msgU) {

        cModel.sendMessage(msgU);
    }

    public void sendDeleteMessage(MessageD msgD) {
            cModel.sendMessage(msgD);

    }

    /**
     * Sending Insert Messages
     * @param messageI
     *
     */
    public void sendInsertMessage(MessageI messageI) {

        cModel.sendMessage(messageI);
    }
    /*

    /**
     * Sending Player messages
     * @param plMsg

    public void SendMessage(PlayerMsg plMsg) {

        cModel.sendMessage(plMsg);
    }

    /**
     * Sending Button messages
     * @param btnMsg

    public void SendMessage(ButtonMessage btnMsg) {

        cModel.sendMessage(btnMsg);
    }

    /**
     * Sending Gamemessages
     * @param GameMsg

    public void SendMessage(GameMessage GameMsg) {

        cModel.sendMessage(GameMsg);
    }

    /**
     * Sending Start / End messages of the Game
     * @param seMessage

    public void SendMessage(StartEndGameMessage seMessage) {

        cModel.sendMessage(seMessage);
    }

    public void SendMessage(IncorrectPwdMessage incMessage) {

        cModel.sendMessage(incMessage);
    }

    public void disconnect(messageType dcMessage){
        cModel.disconnect(dcMessage);
    }
 */


}

