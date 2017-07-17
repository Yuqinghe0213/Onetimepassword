/**
 * Created by Administrator on 2017/5/20.
 * Start the thread of server and establish a ssl socket with its
 * certificate
 */

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.sql.Connection;

public class ServerThread extends Thread{
    private int port;
    public ServerThread(int port) {
        this.port = port;
    }

    public void run() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore tks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream("cert/kserver.ks"), "heyuqing".toCharArray());
            tks.load(new FileInputStream("cert/tserver.ks"), "heyuqing".toCharArray());

            kmf.init(ks, "heyuqing".toCharArray());
            tmf.init(tks);

            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            SSLServerSocket serverSocket = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(port);
            serverSocket.setNeedClientAuth(true);
            int clientnum = 0;
            while(true){
                Socket clientSocket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(clientSocket);
                clientConnection.start();
                clientnum++;
                clientConnection.setName("Thread" + clientnum);
                ServerState.getInstance().clientConnected(clientConnection);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        finally {
//            if (serverSocket != null) {
//                try {
//                    serverSocket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }
}
