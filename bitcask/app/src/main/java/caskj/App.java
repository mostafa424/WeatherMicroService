
package caskj;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

// first byte
// open -> 0
// get -> 1
// put -> 2
// close -> 3




public class App {
	
		
    public static void main(String[] args) {
        Bitcask bitcask = new Bitcask4j();

        MessageHandler messageHandler = new MessageHandler();
        try {
            ServerSocket serverSocket = new ServerSocket(6666);
            while(true) {
                Socket socket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                byte[] bytes = dis.readAllBytes();
                String response = messageHandler.handleMessage(bitcask, bytes);
                if(response == Response.CLOSED_SUCCESSULLY.name()) break;
                dis.close();
                System.out.println(response);
            }
            serverSocket.close();

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        
    }
}
