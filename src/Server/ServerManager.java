package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.ResultSet;
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
    private Thread runThread, receiveThread, sendThread;
    private DatabaseManager db;

    public ServerManager(int port) {

        this.port = port;
        try {
            socket = new DatagramSocket(port);
            System.out.println("SERVER: Successfully Created Socket For Server");
        } catch (SocketException ex) {
            System.out.println("SERVER: Couldn't Create Socket For Server");
            return;
        }
        runThread = new Thread(this, "Server");
        runThread.start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("SERVER: Server Started");
        ReceivePacket();
    }

    private void SendPacket(final byte[] data, InetAddress ip, int port) {
        sendThread = new Thread("Send Thread Server") {
            public void run() {
                try {
                    DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
                    socket.send(packet);
                    System.out.println("SERVER: Successfully Sent to-> ip: " + packet.getAddress() + ", port: " + packet.getPort());
                } catch (IOException ex) {
                    System.out.println("SERVER: Couldn't send to client");
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
                        System.out.println("SERVER: Successfully Received Packet");
                        String msg = new String(packet.getData());
                        if (msg.startsWith("/c/")) {
                            System.out.println("SERVER: Received Connection Packet From-> ip:  " + packet.getAddress() + ",port: " + packet.getPort() + ": ");
                            System.out.println(msg);
                            msg = msg.substring(3, msg.indexOf(ENDINDEX));
                            msg = "/c/" + msg + ENDINDEX;
                            System.out.println(msg);
                        } else if (msg.startsWith("/L/")) {
                            System.out.print("SERVER: Received Login Packet From-> ip:  " + packet.getAddress() + ", " + packet.getPort() + ": ");
                            msg = msg.substring(3, msg.indexOf(ENDINDEX));
                            String parts[] = msg.split(",");
                            String name = parts[0], psd = parts[1];
                            System.out.println("SERVER: Received name : " + name + "and password : " + psd);
                            db = new DatabaseManager();
                            try {
                                boolean v1 = db.isLoggedInIpPort(packet.getAddress().toString(), packet.getPort()),
                                        v2 = db.isLoggedInUser(name);
                                if (!v1 && !v2) {
                                    if (db.LoginDB(name, psd)) {
                                        msg = "TRUE";
                                        db.InsertLoggedinIP_POrt(packet.getAddress().toString(), packet.getPort());
                                        db.InsertUserAsLoggedIn(name);
                                        System.out.println("SERVER: Successfully logged in Client's name : " + name + "and password : " + psd);
                                    } else {
                                        msg = "FALSE";
                                        System.out.println("SERVER: Couldn't login.");
                                    }
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            msg = "/L/" + msg + ENDINDEX;
                        } else if (msg.startsWith("/R/")) {
                            System.out.print("SERVER: Received Registration Packet From-> ip:  " + packet.getAddress() + ", " + packet.getPort() + ": ");
                            msg = msg.substring(3, msg.indexOf(ENDINDEX));
                            String parts[] = msg.split(",");
                            String name = parts[0], psd = parts[1];
                            System.out.println("SERVER: Received name : " + name + "and password : " + psd);
                            db = new DatabaseManager();
                            try {
                                if (db.InsertClientDB(name, psd)) {
                                    msg = "TRUE";
                                    System.out.println("SERVER: Successfully Registerd Client's name : " + name + "and password : " + psd);

                                } else {
                                    msg = "FALSE";
                                    System.out.println("SERVER: Couldn't Register Client's name : " + name + "and password : " + psd);

                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            msg = "/R/" + msg + ENDINDEX;

                        } else if (msg.startsWith("/Info/")) {
                            System.out.print("SERVER: Received GetInfo Packet From-> ip:  " + packet.getAddress() + ", " + packet.getPort() + ": ");
                            msg = msg.substring(6, msg.indexOf(ENDINDEX));

                            db = new DatabaseManager();
                            try {
                                msg = db.InfoDB(Integer.parseInt(msg));

                            } catch (SQLException ex) {
                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("SERVER:Passing GetInfo Response Packet To Client With Msg: " + msg);
                            msg = "/info/" + msg + ENDINDEX;

                        } else if (msg.startsWith("/Insert/")) {
                            System.out.print("SERVER: Received InsertStudent Packet From-> ip:  " + packet.getAddress() + ", " + packet.getPort() + ": ");
                            msg = msg.substring(8, msg.indexOf(ENDINDEX));
                            String parts[] = msg.split(",");
                            int id = Integer.parseInt(parts[0]), sem = Integer.parseInt(parts[3]);
                            String name = parts[1], dept = parts[2];
                            System.out.println(parts.length);
                            System.out.println(parts[0] + parts[1] + parts[2] + parts[3]);
                            System.out.println("SERVER:To Insert Given Student's name: " + name + " id: " + id + " semester: " + sem + " department: " + dept);
                            db = new DatabaseManager();
                            try {
                                msg = db.InsertStudentDB(id, name, dept, sem);

                            } catch (SQLException ex) {
                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("SERVER:Passing InsertStudent Response Packet To Client With Msg: " + msg);
                            msg = "/Insert/" + msg + ENDINDEX;

                        } else if (msg.startsWith("/OUT/")) {
                            System.out.print("SERVER: Received Logout Packet From-> ip:  " + packet.getAddress() + ", " + packet.getPort() + ": ");
                            msg = msg.substring(5, msg.indexOf(ENDINDEX));
                            db = new DatabaseManager();
                            try {
                                db.deleteLoggedInInfo(packet.getAddress().toString(), packet.getPort(), msg);
                            } catch (SQLException ex) {
                                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
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
