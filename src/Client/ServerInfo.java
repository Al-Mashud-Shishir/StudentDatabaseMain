package Client;

import java.net.InetAddress;

/**
 * author :al
 * Created At:Dec 10, 2016
 */
public class ServerInfo {
    private InetAddress ip;
    private int port;

    public ServerInfo(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
