package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * author :al Created At:Dec 10, 2016
 */
public class ClientManager implements Runnable {

    public LoginToServer loginToServer;
    private String ENDINDEX = "//INFO_END//";
    public DatagramSocket socket;
    public ServerInfo serverInfo;
    private Thread sendThread, runThread, receiveThread;
    private boolean running;
    JTextArea ta;
    private String UserName;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    
    public ClientManager(DatagramSocket socket, ServerInfo serverInfo) {
        this.socket = socket;
        this.serverInfo = serverInfo;
        runThread = new Thread(this, "Server");
        runThread.start();
    }

    @Override
    public void run() {
        running = true;
        ReceivePacket();
    }

    public void ReceivePacket() {
        receiveThread = new Thread() {
            public void run() {
                while (running) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                        String msg = new String(packet.getData());
                        System.out.println("Client: Successfully Received Data :"+msg);
                        if (msg.startsWith("/L/")) {
                            String pString = msg.substring(3, msg.indexOf(ENDINDEX));
                            if (pString.isEmpty() || pString.equals("FALSE")) {
                                System.out.println("SERVER: User Not Found,Please Register First");
                            } else if (pString.equals("TRUE")) {

                                System.out.println("Client: Successfully Logged In");
                                new ClientActivity(ClientManager.this).setVisible(true);
                            }
                        } else if (msg.startsWith("/R/")) {
                            String pString = msg.substring(3, msg.indexOf(ENDINDEX));
                            if (pString.equals("FALSE")) {
                                System.out.println("SERVER: User Couldn't Be Registered ");
                            } else if (pString.equals("TRUE")) {

                                System.out.println("Client: Successfully Registered User");
                                //  new ClientActivity().setVisible(true);
                            }
                        } else if (msg.startsWith("/c/")) {
                            msg = msg.substring(3, msg.indexOf(ENDINDEX));
                            // if(pString.isEmpty()||pString.equals("FALSE")){
                            System.out.println("SERVER: "+msg);
                        } else if (msg.startsWith("/info/")) {
                            msg = msg.substring(6, msg.indexOf(ENDINDEX));
                             System.out.println("SERVER: "+msg);
                        }
                        else if (msg.startsWith("/Insert/")) {
                            msg = msg.substring(8, msg.indexOf(ENDINDEX));
                            // if(pString.isEmpty()||pString.equals("FALSE")){
                            System.out.print("Server: Did Added New Student ? ");
                            System.out.println(msg);
                        }
                    } catch (IOException ex) {
                        System.out.println("Client: Didn't Receive Data From Server");
                    }
                }
            }
        };
        receiveThread.start();
    }

    public void SendPacket(final byte[] data) {
        sendThread = new Thread("SendThread In Client") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, serverInfo.getIp(), serverInfo.getPort());
                try {
                    socket.send(packet);
                    System.out.println("Client: Successfully Sent Data  To The Server");
                } catch (IOException ex) {
                    System.out.println("Client: Can't Send Data To The Server");
                }
            }
        };
        sendThread.start();
    }

    public void ConnectionMessageToServer() {
        String msg = "hello Server";
        System.out.println("To Server: " + msg);
        msg = "/c/" + msg + ENDINDEX;
        SendPacket(msg.getBytes());
    }
}
