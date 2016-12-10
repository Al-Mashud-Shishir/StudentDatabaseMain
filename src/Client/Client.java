package Client;

import java.net.DatagramSocket;

/**
 * author :al
 * Created At:Dec 10, 2016
 */
public class Client extends ClientManager{
    private String name,password;
    

    public Client(DatagramSocket socket, ServerInfo serverInfo) {
        super(socket, serverInfo);
    }
    
}
