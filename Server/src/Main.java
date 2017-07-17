/**
 * Created by Administrator on 2017/4/17.
 * This program is the server of one-time password system.
 * In this server, it receive the message from desktop application and
 * deal with this message and give a reply.
 * The main functions of server include database connect, data encryption
 * and decryption, generate identifier for user and password  verification.
 * This is the entrance the of propgram, it start a thread fro listening
 * the message received from 8000 port.
 */

public class Main {

    public static void main(String[] args) {

        ServerThread clientListener = new ServerThread(8000);
        clientListener.start();
    }
}

