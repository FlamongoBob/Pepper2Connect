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

    public void StartServer(boolean isClientConnected, String strUserName, String strPassword, String strIpAddress, int intPort) {

        listener = new OnProcessedListener() {
            @Override
            public void onProcessed(Message msg) {
                // Use the handler so we're not trying to update the UI from the bg thread
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Update the UI here
                        //updateUi(result);

                        if (msg instanceof MessageSystem) {

                            controller.appendSystemMessage2EditText((MessageSystem) msg);

                            if (msg.getType().equals(MessageType.Disconnect)) {
                                controller.disconnectFromPepper((MessageSystem) msg);

                            } else if (msg.getType().equals(MessageType.Unsuccessful_LogIn)) {
                                controller.disconnectFromPepper((MessageSystem) msg);

                            } else if (msg.getType().equals(MessageType.Successful_LogIn)) {
                                controller.clientSuccessfulLogin((MessageSystem) msg);

                            } else if (msg.getType().equals(MessageType.LogOut)) {
                                controller.disconnectFromPepper((MessageSystem) msg);

                            } else if (msg.getType().equals(MessageType.System)) {

                            } else if (msg.getType().equals(MessageType.Patient)) {

                            } else if (msg.getType().equals(MessageType.User)) {
                                controller.fillCurrentUser((MessageUser) msg);

                            }else if (msg.getType().equals(MessageType.Error)) {
                                controller.showLoginStatusInformation(msg);

                            }else if (msg.getType().equals(MessageType.Test)) {
                                controller.appendSystemMessage2EditText((MessageSystem) msg);
                            }
                        }

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
                MessageSystem msgSys = new MessageSystem("Connecting");
                msgSys.setType(MessageType.Connect);
                //msgSys.setBoolean(false);

                listener.onProcessed(msgSys);

                try {
                    if (socket != null) {
                        socket = new Socket(strIpAddress, intPort);
                    }
                    controller.isClientConnected = true;

                    while (controller.isClientConnected) {

                        Message msg = Message.receive(socket);

                        if (msg.getType().equals(MessageType.Test)) {
                            responseTimer();
                        } else {
                            listener.onProcessed(msg);
                        }


                    }
                } catch (Exception e) {
                    // logger.warning(e.toString());

                    controller.isClientConnected = false;
                    String err = e.getMessage();
                    err += "";

                    // Controlle Show as Error when Login
                    MessageSystem msgSysError = new MessageSystem(" Could not Connect to the Server! \n Please try again!");
                    msgSysError.setType(MessageType.Error);
                    listener.onProcessed(msgSysError);
                }


            }
        };

        mExecutor.execute(backgroundRunnable);
    }

    private void Login(String strUserName, String strPassword) {

        if (!controller.isLoggedIn) {
            MessageLogin msgLogin = new MessageLogin(strUserName, strPassword, MessageType.Login);
            msgLogin.send(socket);
        }
    }

    public void disconnect() {
        MessageSystem msgSysDisconnect = new MessageSystem("Disconnecting");
        msgSysDisconnect.setType(MessageType.Disconnect);
        msgSysDisconnect.send(socket);

        controller.isClientConnected = false;
        controller.isLoggedIn = false;
        mExecutor.shutdown();
    }

    public void responseTimer() {
        MessageSystem msgSysTimer = new MessageSystem("Response Time");
        msgSysTimer.setType(MessageType.Test);
        msgSysTimer.send(socket);
    }

    public void sendMessage(Message message) {
        if (message != null) {
            message.send(socket);
        }
    }


}
