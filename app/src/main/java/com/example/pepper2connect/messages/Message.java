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
    protected MessageType type;
    private boolean value;
    private static Encryption encryption;
    private static Decryption decryption;
    public Message(MessageType type) {
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

                if (parts[0].equals(MessageType.Disconnect.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.Disconnect);

                } else if (parts[0].equals(MessageType.Unsuccessful_LogIn.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.Unsuccessful_LogIn);

                } else if (parts[0].equals(MessageType.Successful_LogIn.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.Successful_LogIn);

                }else if (parts[0].equals(MessageType.LogOut.toString())) {
                    message.setType(MessageType.LogOut);

                } else if (parts[0].equals(MessageType.Disconnect.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.Disconnect);

                } else if (parts[0].equals(MessageType.Patient.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.Patient);

                } else if (parts[0].equals(MessageType.System.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.System);

                } else if (parts[0].equals(MessageType.Test.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.Test);

                } else if (parts[0].equals(MessageType.User.toString())) {
                    message = new MessageUser(Integer.parseInt(parts[1])
                            ,parts[2]
                            ,parts[3]
                            ,parts[4]

                            ,Integer.parseInt(parts[5])
                            ,parts[6]

                            ,Integer.parseInt(parts[7])
                            ,parts[8]

                            ,Integer.parseInt(parts[9])
                            ,parts[10]
                            ,parts[11]

                            ,Integer.parseInt(parts[12])
                    );
                    message.setType(MessageType.User);
                } else if (parts[0].equals(MessageType.Suc_IUD.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.Suc_IUD);
                } else if (parts[0].equals(MessageType.Error.toString())) {
                    message = new MessageSystem(parts[1]);
                    message.setType(MessageType.Error);
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
    public MessageType getType() {
        return this.type;
    }
    public void setType(MessageType msgType){
        this.type = msgType;
    }


    public boolean getBoolean() {
        return value;
    }

    public void setBoolean(boolean value) {
        this.value = value;
    }
}

