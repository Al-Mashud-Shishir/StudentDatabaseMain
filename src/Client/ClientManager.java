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
    private Thread sendThread;

    public ClientManager(DatagramSocket socket, ServerInfo serverInfo) {
        this.socket = socket;
        this.serverInfo = serverInfo;
    }

    public String ReceivePacket() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException ex) {
            System.out.println("Didn't Receive Data From Server");
        }
        System.out.println("Successfully Received Data");
        String msg = new String(packet.getData());
        return msg;
    }

    public void SendPacket(final byte[] data) {
        sendThread = new Thread("SendThread In Client") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, serverInfo.getIp(), serverInfo.getPort());
                try {
                    socket.send(packet);
                } catch (IOException ex) {
                    System.out.println("Can't Send Data To The Server");
                }
            }
        };
        sendThread.start();
    }
}
