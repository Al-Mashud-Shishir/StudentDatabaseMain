package Client;

import java.net.DatagramSocket;
import javax.swing.JTextArea;

/**
 * author :al
 * Created At:Dec 10, 2016
 */
public class Client extends ClientManager{
    private String name,password;
    

    
    public Client(DatagramSocket socket, ServerInfo serverInfo) {
        super(socket, serverInfo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
