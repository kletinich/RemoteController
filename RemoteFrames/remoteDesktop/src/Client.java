import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static int DEFAULT_PORT = 8080;
    public static String DEFAULT_ADDRESS = "127.0.0.1";

    private int _port;
    private String _address;

    private Socket clientSocket;

    private DataInputStream in;
    private DataOutputStream out;

    private FrameHandler frameHandler;

    public Client(){
        this(DEFAULT_PORT, DEFAULT_ADDRESS);
    }

    public Client(int port, String address){
        this._port = port;
        this._address = address;

        frameHandler = new FrameHandler();
    }

    public boolean connectToServer(){
        try {
            clientSocket = new Socket(this._address, this._port);
            this.frameHandler.setText("Connected to server.");

            this.in = new DataInputStream(this.clientSocket.getInputStream());
            this.out = new DataOutputStream(this.clientSocket.getOutputStream());

        } catch (IOException e) {
            this.frameHandler.setText("Can't connect to server.");
            return false;
        }

        return true;
    }

    public void work(){
        while(true){
            try {
                this.frameHandler.setText(this.in.readUTF());
                this.out.write(5);
            } catch (IOException e) {
                this.frameHandler.setText("Connection closed");
            }
        }
    }
}
