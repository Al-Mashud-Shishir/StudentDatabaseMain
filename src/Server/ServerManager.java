package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author :al Created At:Dec 10, 2016
 */
public class ServerManager implements Runnable {

    private String ENDINDEX = "//INFO_END//";
    public DatagramSocket socket;
    public int port;
    public boolean running;
    private Thread runThread, manageThread, receiveThread, sendThread;
    private DatabaseManager db;

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

    private void SendPacket(final byte[] data, InetAddress ip, int port) {
        sendThread = new Thread("Send Thread Server") {
            public void run() {
                try {
                    DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
                    socket.send(packet);
                    System.out.println("Successfully Sent to client from server");
                } catch (IOException ex) {
                    System.out.println("Couldn't send to client from server");
                }
            }
        };
        sendThread.start();
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
                        String msg = new String(packet.getData());
                        if (msg.startsWith("/c/")) {
                            System.out.println("From " + packet.getAddress() + ", " + packet.getPort() + ": ");
                            System.out.println(msg);
                            msg = msg.substring(3,msg.indexOf(ENDINDEX));
                            msg = "/c/" + msg+ENDINDEX;
                            System.out.println(msg);
                        } else if (msg.startsWith("/L/")) {
                            System.out.print("From " + packet.getAddress() + ", " + packet.getPort() + ": ");
                            // System.out.println(msg+" "+msg.length());
                            msg = msg.substring(3, msg.indexOf(ENDINDEX));
                            String parts[]=msg.split(",");
                            String name=parts[0],psd=parts[1];
                            System.out.println(name + " : " +psd );
                            //  msg += "Successfully Received Name";
                            db = new DatabaseManager();
                            try {
                                if(db.LoginDB(name,psd)){
                                    msg="TRUE";                              
                                }
                                else{
                                msg="FALSE";
                                }
                               // System.out.println("Password: " + psd);
                            } catch (SQLException ex) {
                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            msg = "/L/" + msg+ENDINDEX;
                        }
                        else if(msg.startsWith("/R/")){
                        System.out.print("From " + packet.getAddress() + ", " + packet.getPort() + ": ");
                            // System.out.println(msg+" "+msg.length());
                            msg = msg.substring(3, msg.indexOf(ENDINDEX));
                            String parts[]=msg.split(",");
                            String name=parts[0],psd=parts[1];
                            System.out.println(name + " : " +psd );
                            //  msg += "Successfully Received Name";
                            db = new DatabaseManager();
                            try {
                                if(db.InsertClientDB(name,psd)){
                                    msg="TRUE";                              
                                }
                                else{
                                msg="FALSE";
                                }
                               // System.out.println("Password: " + psd);
                            } catch (SQLException ex) {
                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            msg = "/R/" + msg+ENDINDEX;
                        
                        }
                         else if(msg.startsWith("/Info/")){
//                        System.out.print("From " + packet.getAddress() + ", " + packet.getPort() + ": ");
//                            // System.out.println(msg+" "+msg.length());
//                            msg = msg.substring(5, msg.indexOf(ENDINDEX));
//                           
//                            db = new DatabaseManager();
//                            try {
//                                if(db.InsertClientDB(name,psd)){
//                                    msg="TRUE";                              
//                                }
//                                else{
//                                msg="FALSE";
//                                }
//                               // System.out.println("Password: " + psd);
//                            } catch (SQLException ex) {
//                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                            msg = "/R/" + msg+ENDINDEX;
//                        
                        }
                        SendPacket(msg.getBytes(), packet.getAddress(), packet.getPort());
                    } catch (IOException ex) {
                        System.out.println("Couldn't Receive Packet For Server");
                    }
                }
            }
        };
        receiveThread.start();
    }
}
