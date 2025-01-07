import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

//The server receives a consistant screenview from the client. 
//The server controls the mouse and keyboard of the local client desktop.
public class Server {
    public static String DEFAULT_IP = "127.0.0.1";
    public static int DEFAULT_PORT = 8080;

    private String _ip;
    private int _port;

    private ServerSocket _server;
    private Socket _socket;

    private DataInputStream _in;
    private DataOutputStream _out;

    FrameHandler _frameHandler;

    public Server(){
        this(DEFAULT_IP, DEFAULT_PORT);
    }

    public Server(String ip, int port){
        this._ip = ip;
        this._port = port;

        this._frameHandler = new FrameHandler(this);
    }

    public void startServer(){
        try {
            InetAddress bindAddress = InetAddress.getByName(this._ip);
            this._server = new ServerSocket(this._port, 1, bindAddress);

            System.out.println("Server started");
            System.out.println("Waiting for client");

        } catch (IOException e) {
            System.err.println("Error initializing socket"); 
            System.exit(1);
        }

        try{
            this._socket = this._server.accept();
            System.out.println("Connected to client"); 

            this._in = new DataInputStream(this._socket.getInputStream());
            this._out = new DataOutputStream(this._socket.getOutputStream());

        }catch(IOException e){
            System.err.println("Connection closed"); 
            System.exit(1);
        }
    }

    public void closeServer(){
        try {
            this._server.close();
            
        } catch (IOException e) {
        }

        finally{
            System.out.println("Server closed");
            System.exit(0);
        }
    }

    public void work(){
        while(true){
            try {
                // receiving byte byte array and its size of BufferedImage
                int imageLength = this._in.readInt();
                byte[] imageBytes = new byte[imageLength];

                this._in.readFully(imageBytes);
                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                BufferedImage screenCapture = ImageIO.read(bais);

                // repaint the frame with the new captured screen
                this._frameHandler.repaint(screenCapture);

                this._out.writeDouble(this._frameHandler.getMousePropotionalPosition().get("x"));
                Thread.sleep(20);
                this._out.writeDouble(this._frameHandler.getMousePropotionalPosition().get("y"));
                Thread.sleep(20);
                this._out.writeBoolean(this._frameHandler.isPressed());
                Thread.sleep(20);
                this._out.writeBoolean((this._frameHandler.isClicked()));
                Thread.sleep(20);
                this._out.writeBoolean(this._frameHandler.inFrameBounds());
                Thread.sleep(20);
                this._out.writeInt(this._frameHandler.getKeyCode());
                Thread.sleep(20);      

            } catch (IOException e) {
                System.out.println("Closed connection with the client");
                System.exit(0);
            } catch (InterruptedException e) {
            }
        }
    }
}
