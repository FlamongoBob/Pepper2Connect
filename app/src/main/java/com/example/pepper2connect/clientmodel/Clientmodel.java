package com.example.pepper2connect.clientmodel;

import static java.lang.Boolean.valueOf;

import android.os.Handler;
import android.os.Looper;

import com.example.pepper2connect.MainActivity;
import com.example.pepper2connect.controller.Controller;
import com.example.pepper2connect.messages.*;
import com.example.pepper2connect.utils.NotificationUtil;

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
    MainActivity mainActivity;
    String strIpAddress;
    int intPort;
    static OnProcessedListener listener;

    // Create some member variables for the ExecutorService
    // and for the Handler that will update the UI from the main thread
    ExecutorService mExecutor = Executors.newFixedThreadPool(2);
    Handler mHandler = new Handler(Looper.getMainLooper());


    public Clientmodel(Controller controller, MainActivity mainActivity) {
        this.controller = controller;
        this.mainActivity = mainActivity;
    }


    // Create an interface to respond with the result after processing
    public interface OnProcessedListener {
        void onProcessed(Message msg);
    }

    public void StartServer(String strUserName, String strPassword, String strIpAddress, int intPort) {

        listener = new OnProcessedListener() {
            @Override
            public void onProcessed(Message msg) {
                // Use the handler so we're not trying to update the UI from the bg thread
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Update the UI here

                        if (msg instanceof MessageSystem) {

                            controller.appendLogServerCon(" received", msg.getType());

                            if (msg.getType().equals(MessageType.Unsuccessful_LogIn)) {

                                controller.disconnectFromPepper((MessageSystem) msg);
                                controller.backToLogin();

                            } else if (msg.getType().equals(MessageType.Disconnect)) {

                                shutDown();
                                controller.backToLogin();

                            } else if (msg.getType().equals(MessageType.Successful_LogIn)) {

                                controller.clientSuccessfulLogin((MessageSystem) msg);

                            } else if (msg.getType().equals(MessageType.LogOut)) {

                                controller.disconnectFromPepper((MessageSystem) msg);

                            } else if (msg.getType().equals(MessageType.System)) {

                                controller.showInformation(msg);

                            } else if (msg.getType().equals(MessageType.Suc_IUD)) {

                                controller.showInformation(msg);

                            } else if (msg.getType().equals(MessageType.Patient)) {

                                NotificationUtil.createChannel(mainActivity, "Patient_Information");
                                NotificationUtil.setNotification(mainActivity, "New Patient Information received"
                                        , "The Patient is ready and there is information available in the Patient Information Tab");

                                controller.appendPatientInformation((MessageSystem) msg);
                                controller.appendLogServerCon(" received", msg.getType());
                            }
                        }

                        if (msg.getType().equals(MessageType.User)) {

                            controller.fillCurrentUser((MessageUser) msg);
                            controller.appendLogServerCon(" received", msg.getType());

                        } else if (msg.getType().equals(MessageType.Error)) {

                            controller.showInformation(msg);
                            controller.appendLogServerCon(" received", msg.getType());

                        } else if (msg.getType().equals(MessageType.Test)) {
                            controller.appendLogServerCon(" received", msg.getType());

                        } else if (msg.getType().equals(MessageType.AllUser)) {

                            controller.populateArrayAllUsers((MessageUser) msg);
                            controller.appendLogServerCon(" received", msg.getType());

                        } else if (msg.getType().equals(MessageType.Roles)) {

                            controller.populateArrayListRoles((MessageRoles) msg);
                            controller.appendLogServerCon(" received", msg.getType());
                        }
                        // If we're done with the ExecutorService, shut it down.
                        // Shut it down whenever everything is completed and it is not needed anymore.)
                        if (!controller.isClientConnected) {
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

                try {

                    socket = new Socket(strIpAddress, intPort);

                    if (socket != null) {
                        controller.isClientConnected = true;
                        Login(strUserName, strPassword);
                    }

                    while (controller.isClientConnected) {

                        Message msg = Message.receive(socket);
                        if (msg != null) {
                                listener.onProcessed(msg);
                        } else {

                            MessageSystem msgSys = new MessageSystem("You have not been connected to the Server. " +
                                    "Please make sure you are in the correct Wifi-Network and verify that the Server has been started");

                            controller.showInformation(msgSys);
                            socket = null;
                            controller.isClientConnected = false;
                            controller.isLoggedIn = false;
                            break;
                        }
                    }
                } catch (Exception e) {
                    // logger.warning(e.toString());

                    controller.isClientConnected = false;
                    controller.isLoggedIn = false;
                    socket = null;

                    String err = e.getMessage();
                    err += "";

                    // Controlle Show as Error when Login
                    MessageSystem msgSysError = new MessageSystem(" Could not Connect to the Server! \n Please try again!");
                    msgSysError.setType(MessageType.Error);
                    listener.onProcessed(msgSysError);
                }


            }
        };

        try {
            mExecutor.execute(backgroundRunnable);
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


        shutDown();
        controller.backToLogin();


    }

    public void shutDown() {
        try {


            socket.close();

            controller.isClientConnected = false;
            controller.isLoggedIn = false;

            mExecutor.shutdown();

        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err += "";
        }
    }

    public void responseTimer() {
        MessageSystem msgSysTimer = new MessageSystem("Response Time");
        msgSysTimer.setType(MessageType.Test);
        msgSysTimer.send(socket);
    }

    public void sendMessage(Message message) {
        try {
            mExecutor.execute(
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (message != null) {
                                message.send(socket);
                            }

                        }
                    })
            );
        } catch (Exception e) {
            String err = "";
            err = e.getMessage();
            err = "";
        }
    }


}
