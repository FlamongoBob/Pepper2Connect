package com.example.pepper2connect.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.pepper2connect.client.Client;
import com.example.pepper2connect.controller.Controller;
import com.example.pepper2connect.messages.MessageSystem;
import com.example.pepper2connect.messages.MessageType;

public class LocalService extends Service {
    Client client;
    Controller controller;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Class used for the client Binder.
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void connect2Pepper(String strUsername, String strPassword,Controller controller, int intPort, String strIpAddress) {

        if(this.controller==null){
            this.controller = controller;
        }

        client = controller.connect2Pepper(strUsername, strPassword, intPort, strIpAddress);
    }

    public void sendMessage(MessageSystem messageSystem) {

        client.sendSysMessage(messageSystem);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public final void onTaskRemoved(Intent rootIntent){
        if(Controller.isClientConnected) {
            super.onTaskRemoved(rootIntent);

            MessageSystem msgDisc = new MessageSystem("Disconnect");
            msgDisc.setType(MessageType.Disconnect);
            sendMessage(msgDisc);

            client.disconnect();
        }
    }

    public void appClosedDisconnect(){
        if(Controller.isClientConnected) {
            MessageSystem msgDisc = new MessageSystem("Disconnect");
            msgDisc.setType(MessageType.Disconnect);
            sendMessage(msgDisc);

            client.disconnect();
        }
    }

}

