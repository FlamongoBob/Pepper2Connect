package com.example.pepper2connect.clientmodel;

import static java.lang.Boolean.valueOf;

import android.os.Handler;
import android.os.Looper;

import com.example.pepper2connect.Crypto.Encryption;
import com.example.pepper2connect.controller.Controller;
import com.example.pepper2connect.messages.*;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Clientmodel {
    //Controller controller;
    public static Logger logger = Logger.getLogger("");
    Controller controller;

    private Socket socket;
    private String strUserName;
    private String strPassword;
    Thread t;

    String strIpAddress;
    int intPort;
    static OnProcessedListener listener;
    private Encryption encrpytion = new Encryption();

    // Create some member variables for the ExecutorService
    // and for the Handler that will update the UI from the main thread
    ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    Handler mHandler = new Handler(Looper.getMainLooper());

    public Clientmodel(Controller controller) {
        this.controller = controller;
    }



    // Create an interface to respond with the result after processing
    public interface OnProcessedListener {
        void onProcessed(Message msg);
    }

    public void StartServer(boolean isClientConnected,String strUserName, String strPassword, String strIpAddress, int intPort) {

        listener = new OnProcessedListener() {
            @Override
            public void onProcessed(Message msg) {
                // Use the handler so we're not trying to update the UI from the bg thread
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Update the UI here
                        //updateUi(result);

                        if (msg instanceof messageSystem) {

                            if(msg.getType().equals(messageType.Disconnect)){

                            }else if(msg.getType().equals(messageType.Unsuccessful_Login)){

                            }else if(msg.getType().equals(messageType.Successful_Login)){

                            }else if(msg.getType().equals(messageType.System)){

                            }else if(msg.getType().equals(messageType.Patient)){

                            }
                        } /*else if (msg instanceof messageSystem) {

                        } else if (msg instanceof messageConnect) {

                        } else if (msg instanceof messageDisconnect) {

                        }*/

                        // If we're done with the ExecutorService, shut it down.
                        // (If you want to re-use the ExecutorService, make sure to shut it down whenever everything's completed and you don't need it any more.)
                        if (!isClientConnected) {
                            mExecutor.shutdown();
                        }
                    }
                });
            }
        };
        /**
         *  Perform background connection to server and receive messages to send them to the Mainthread (UI) to show the  User
         */
        Runnable backgroundRunnable = new Runnable() {
            @Override
            public void run() {

                /** Use the interface to pass along the message to Mainthread / User
                 * @param msgConnect with Boolean false --> Trying to Connect, message to User
                 *                   with Boolean True -->  Connected to Server,  message to User
                 */
                messageSystem msgSys = new messageSystem("Connecting");
                msgSys.setType(messageType.Connect);
                //msgSys.setBoolean(false);

                listener.onProcessed(msgSys);

                try {
                    if(socket != null) {
                        socket = new Socket(strIpAddress, intPort);
                    }
                    controller.isClientConnected = true;

                    while (controller.isClientConnected) {

                        Message msg = Message.receive(socket);

                        if(msg.getType().equals(messageType.Timer)){
                            responseTimer();
                        }else {
                            listener.onProcessed(msg);
                        }


                    }
                } catch (Exception e) {
                    // logger.warning(e.toString());

                    controller.isClientConnected = false;
                    String err = e.getMessage();
                    err += "";

                    /* Controlle Show as Error when Login
                    messageSystem msgSysError = new messageSystem();
                    msgSysError.setType(messageType.Error);
                    msgSysError.setStrSystemNotification(" Could not Connect to the Server, \" +\n"
                            + "\"Please try again! +\n"
                            + "Error Message: +\n" + err);
                    listener.onProcessed(msgSysError);*/
/*
            controller.appendTextToChat("Could not Connect to the Server, " +
                            "Please try again or try a different Server"
                    , Controller.eTextType.System);
*/
                }


            }
        };

        mExecutor.execute(backgroundRunnable);
    }
    private void Login(String strUserName, String strPassword){

        if(!controller.isLoggedIn){
            messageLogin msgLogin = new messageLogin(strUserName, strPassword,messageType.Login);
            msgLogin.send(socket);
        }
    }

    public void disconnect() {
        messageSystem msgSysDisconnect = new messageSystem("Disconnecting");
        msgSysDisconnect.setType(messageType.Disconnect);
        msgSysDisconnect.send(socket);

        controller.isClientConnected =false;
        mExecutor.shutdown();
    }
    public void responseTimer() {
        messageSystem msgSysTimer = new messageSystem("Response Time");
        msgSysTimer.setType(messageType.Timer);
        msgSysTimer.send(socket);
    }


/*
    public void connect(String ipAddress, int Port, String name, String password) {
        // logger.info("Connect");
        this.name = name;
        this.password = password;
        try {
            socket = new Socket(ipAddress, Port);

            // controller.isClientConnected.setBoolean(true);


            // Create thread to read incoming messages
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    new Thread() {
                    while(isClientConnected)

                        {

                            message msg = message.receive(socket);

                            if (msg instanceof messageLogin) {
                                //controller.appendTextToChat(((ChatMessage) msg).getContent(), ((ChatMessage) msg).getName());

                            } else if (msg instanceof messageLogin) {
                                //controller.appendTextToChat("You entered the wrong password, please try again.",(Controller.eTextType.System));

/*
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //controller.alertMessage("Incorrect Password", "Enter Correct Password", Alert.AlertType.WARNING);

                                }
                            });


                            } else if (msg instanceof messageLogin) {
                                //controller.appendTextToChat(((DisconnectMessage) msg).getStrUsername() +" Has disconnected, please try to reconnect" +
                                //        " or try to connect to another server",Controller.eTextType.System);
                            /*
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //controller.ClientServerHasShutDown();
                                }
                            });

                                // closeSocket();


                            } else if (msg instanceof messageLogin) {
                                if (msg.getType().equals(messageType.Start)) {
                                    //controller.ClientCleanPlayfield();
                                } else if (msg.getType().equals(messageType.End)) {
                                    //controller.appendTextToChat("Press New Game to Start a New Game", Controller.eTextType.System);
                                    // controller.ClientShowWinner();

                                } else if (msg.getType().equals(messageType.Tie)) {
                                    // controller.ClientENDOFGAMEBlockButtons();
                                    //controller.appendTextToChat("It's a Tie", Controller.eTextType.System);

                                } else if (msg.getType().equals(messageType.NewGame)) {
                                    //controller.ClientCleanPlayfield();
                                }

                            } else if (msg instanceof messageLogin) {
                                if (msg.getType().equals(messageType.CreatePlayer)) {

                                    //controller.ClientPlayerCreation(((PlayerMsg) msg).getName(), ((PlayerMsg) msg).getID());
                                }
                                if (msg.getType().equals(messageType.PlayerTurn)) {

                                    //    controller.ClientSetCurrentPlayer(new Player(((PlayerMsg) msg).getName(), ((PlayerMsg) msg).getID()));
                                    //
                                    // controller.setCurrentPlayer(((PlayerMsg) msg).getID());
                                    //
                                }

                            } else if (msg instanceof messageLogin) {
                                // controller.setButtonOwner(((ButtonMessage) msg).getPressedID());
                            /*
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    //controller.ClientSetButtonOwner(((ButtonMessage) msg).getPressedID());
                                    // OLD
                                    //controller.getPressedID.set(((ButtonMessage) msg).getPressedID());
                                }
                            });

                            } else if (msg instanceof messageLogin) {

                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Code here will run in UI thread
                                    }
                                });

                            }

                        }
                    }.start();
                }
            };
            t = new Thread(r);
            t.start();

            // Send join message to the server
            message msg = new JoinMsg(this.name, this.password);
            msg.send(socket);

        } catch (Exception e) {
            // logger.warning(e.toString());

            //controller.ClientIsConnected.set(false);
            String err = e.getMessage();
            err += "";

/*
            controller.appendTextToChat("Could not Connect to the Server, " +
                            "Please try again or try a different Server"
                    , Controller.eTextType.System);

        }

    }
    */


}
