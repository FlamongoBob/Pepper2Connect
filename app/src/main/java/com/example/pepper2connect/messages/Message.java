package com.example.pepper2connect.messages;

import static com.example.pepper2connect.clientmodel.Clientmodel.logger;

import com.example.pepper2connect.Crypto.Decryption;
import com.example.pepper2connect.Crypto.Encryption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public abstract class Message {
    protected messageType type;
    private boolean value;
    private static Encryption encryption;
    private static Decryption decryption;
    public Message(messageType type) {
        this.type = type;

        encryption = new Encryption();
        decryption = new Decryption();
    }

    /**
     * Creates and outputstream to send the message from the sender to the receiver
     * @param socket
     */
    public void send(Socket socket) {
        OutputStreamWriter out;
        try {
            String strEncryptedMessage;
            out = new OutputStreamWriter(socket.getOutputStream());
            logger.info("Sending message Plain message: " + this.toString());
            strEncryptedMessage =encryption.encrypt(this.toString());
            logger.info("Sending message Encrypted message: " + strEncryptedMessage);
            out.write(this.toString() + "\n");
            out.flush();
        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }

    /**
     * The reader get all incoming messages and splits them into parts, to figure out what message types was sent to
     * the receiver, and splits the other parts of the message, for further usage of the receiving program
     * @param socket
     * @return
     */
    public static Message receive(Socket socket) {
        BufferedReader bfr;
        Message message = null;
        try {
            bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msgText = bfr.readLine(); // Will wait here for complete line
            if (msgText != null) {
                logger.info("Receiving  Encrypted message: " + msgText);

                String strDecryptedMessage = decryption.decrypt(msgText);
                logger.info("Receiving  Decrypted message: " + strDecryptedMessage);

                // Parse message
                String[] parts = strDecryptedMessage.split("\\|");

                message = new MessageSystem(parts[1]);
                if (parts[0].equals(messageType.Disconnect.toString())) {
                    message.setType(messageType.Disconnect);
                } else if (parts[0].equals(messageType.Unsuccessful_Login.toString())) {
                    message.setType(messageType.Unsuccessful_Login);
                } else if (parts[0].equals(messageType.Successful_Login.toString())) {
                    message.setType(messageType.Successful_Login);
                } else if (parts[0].equals(messageType.Disconnect.toString())) {
                    message.setType(messageType.Disconnect);
                } else if (parts[0].equals(messageType.Patient.toString())) {
                    message.setType(messageType.Patient);
                } else if (parts[0].equals(messageType.System.toString())) {
                    message.setType(messageType.System);
                } else if (parts[0].equals(messageType.Test.toString())) {
                    message.setType(messageType.Test);
                }
            }
        } catch (IOException e) {
            logger.warning(e.toString());
            //Controller.ClientIsConnected.set(false);
           // Controller.ServerIsStarted.set(false);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * Returns the type of the Message
     */
    public messageType getType() {
        return this.type;
    }
    public void setType(messageType msgType){
        this.type = msgType;
    }

    public boolean getBoolean() {
        return value;
    }

    public void setBoolean(boolean value) {
        this.value = value;
    }
}

