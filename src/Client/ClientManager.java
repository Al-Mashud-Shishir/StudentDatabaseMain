package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
/**
 * author :al Created At:Dec 10, 2016
 */
public class ClientManager {
    public DatagramSocket socket;
    public ServerInfo serverInfo;

    public ClientManager(DatagramSocket socket, ServerInfo serverInfo) {
        this.socket = socket;
        this.serverInfo = serverInfo;
    }
    public  String ReceivePacketServer(){
        byte[]data=new byte[1024];
        DatagramPacket packet=new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException ex) {
            System.out.println("Didn't Receive Data From Server");
        }
        System.out.println("Successfully Received Data");
        String msg=new String(packet.getData());
        return msg;
    }
}
