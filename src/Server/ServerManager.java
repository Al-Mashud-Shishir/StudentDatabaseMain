package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author :al Created At:Dec 10, 2016
 */
public class ServerManager implements Runnable {

    public DatagramSocket socket;
    public int port;
    public boolean running;
    private Thread runThread, manageThread, receiveThread;

    public ServerManager(int port) {

        this.port = port;
        try {
            socket = new DatagramSocket(port);
            System.out.println("Successfully Created Socket For Server");
        } catch (SocketException ex) {
            System.out.println("Couldn't Create Socket For Server");
            return;
        }
        runThread = new Thread(this, "Server");
        runThread.start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Server Started");
        ManageClients();
        ReceivePacket();
    }

    private void ManageClients() {
        manageThread = new Thread() {
            public void run() {
                while (running) {

                }
            }
        };
        manageThread.start();
    }

    private void ReceivePacket() {
        receiveThread = new Thread() {
            public void run() {
                while (running) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                        System.out.println("Successfully Received Packet For Server");
                        String msg=new String(packet.getData());
                        System.out.println(msg);
                    } catch (IOException ex) {
                        System.out.println("Couldn't Receive Packet For Server");
                    }
                }
            }
        };
        receiveThread.start();
    }
}
