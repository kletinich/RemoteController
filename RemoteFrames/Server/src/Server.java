import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static int DEFAULT_PORT = 8080;
    public static String DEFAULT_ADDRESS = "127.0.0.1";

    private int _port;
    private String _address;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private DataOutputStream out;
    private DataInputStream in;

    private FrameHandler frameHandler;

    public Server(){
        this(DEFAULT_PORT, DEFAULT_ADDRESS);
    }

    public Server(int port, String address){
        this._port = port;
        this._address = address;

        this.frameHandler = new FrameHandler(this);
    }

    public boolean Start(){
        try{
        this.serverSocket = new ServerSocket(this._port);
        this.frameHandler.setText("Waiting for clients");

        this.clientSocket = serverSocket.accept();
        this.frameHandler.setText("Accepted connection with client");

        this.in = new DataInputStream(this.clientSocket.getInputStream());
        this.out = new DataOutputStream(this.clientSocket.getOutputStream());

        }catch(IOException e){
            this.frameHandler.setText("Connection Closed");
            return false;
        }
        
        return true;
    }

    public void work(){
        while(true){
            String text = this.frameHandler.getText();
            try {
                this.out.writeUTF(text);
                this.out.flush();

                this.in.read();

            } catch (IOException e) {
                this.frameHandler.setText("Connection closed");
            }  
        }
    }

    public void closeConnection(){
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            this.frameHandler.setText("Connection closed");
        }
    }

}
