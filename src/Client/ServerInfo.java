package Client;

/**
 * author :al
 * Created At:Dec 10, 2016
 */
public class ServerInfo {
    private String ip;
    private int port;

    public ServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
