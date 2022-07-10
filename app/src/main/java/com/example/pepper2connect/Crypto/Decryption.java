package com.example.pepper2connect.Crypto;

import android.util.Base64;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;

public class Decryption {
    public Decryption() {}

    /**
     * Source https://www.geeksforgeeks.org/how-to-encrypt-and-decrypt-text-in-android-using-cryptography/
     * @param strEncodedMessage
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */

    public String decrypt(String strEncodedMessage) throws NoSuchPaddingException, NoSuchAlgorithmException {
/*

        byte[] cipherMessage = Base64.decode(strEncodedMessage, Base64.NO_WRAP);

        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        //use first 12 bytes for iv
        AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, cipherMessage, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);

//use everything from 12 bytes on as ciphertext
        byte[] plainText = cipher.doFinal(cipherMessage, 12, cipherMessage.length - 12);

*/


        String invalid = "Invalid Code";

        // create the same initial
        // string as in encode class
        String ini = "11111111";
        Boolean flag = true;

        // run a loop of size 8
        for (int i = 0; i < 8; i++) {
            // check if the initial value is same
            if (ini.charAt(i) != strEncodedMessage.charAt(i)) {
                flag = false;
                break;
            }
        }
        String val = "";

        // reverse the encrypted code
        for (int i = 8; i < strEncodedMessage.length(); i++) {
            char ch = strEncodedMessage.charAt(i);
            val = val.concat(String.valueOf(ch));
        }

        // create a 2 dimensional array
        int arr[][] = new int[11101][8];
        int ind1 = -1;
        int ind2 = 0;

        // run a loop of size of the encrypted code
        for (int i = 0; i < val.length(); i++) {

            // check if the position of the
            // string if divisible by 7
            if (i % 7 == 0) {
                // start the value in other
                // column of the 2D array
                ind1++;
                ind2 = 0;
                char ch = val.charAt(i);
                arr[ind1][ind2] = ch - '0';
                ind2++;
            } else {
                // otherwise store the value
                // in the same column
                char ch = val.charAt(i);
                arr[ind1][ind2] = ch - '0';
                ind2++;
            }
        }
        // create an array
        int num[] = new int[11111];
        int nind = 0;
        int tem = 0;
        int cu = 0;

        // run a loop of size of the column
        for (int i = 0; i <= ind1; i++) {
            cu = 0;
            tem = 0;
            // convert binary to decimal and add them
            // from each column and store in the array
            for (int j = 6; j >= 0; j--) {
                int tem1 = (int) Math.pow(2, cu);
                tem += (arr[i][j] * tem1);
                cu++;
            }
            num[nind++] = tem;
        }
        String ret = "";
        char ch;
        // convert the decimal ascii number to its
        // char value and add them to form a decrypted
        // string using conception function
        for (int i = 0; i < nind; i++) {
            ch = (char) num[i];
            ret = ret.concat(String.valueOf(ch));
        }
        Log.e("dec", "text 11 - " + ret);

        // check if the encrypted code was
        // generated for this algorithm
        if (val.length() % 7 == 0 && flag == true) {
            // return the decrypted code
            return ret;
        } else {
            // otherwise return an invalid message
            return invalid;

        }
    }
}


