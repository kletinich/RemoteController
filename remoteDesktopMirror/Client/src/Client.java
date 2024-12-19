import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

public class Client {
    public static String DEFAULT_IP = "127.0.0.1";
    public static int DEFAULT_PORT = 8080;

    private String _ip;
    private int _port;

    private Socket _socket;

    private DataInputStream _in;
    private DataOutputStream _out;

    private ScreenCapturer screenCapturer;

    public Client(){
        this(DEFAULT_IP, DEFAULT_PORT);
    }

    public Client(String ip, int port){
        this._ip = ip;
        this._port = port;

        this.screenCapturer = new ScreenCapturer();
    }

    public void connectToServer(){
        try{
            this._socket = new Socket(this._ip, this._port);
            System.out.println("Connected to server");

            this._in = new DataInputStream(this._socket.getInputStream());
            this._out = new DataOutputStream(this._socket.getOutputStream());

        }catch(IOException e){
            System.err.println("Can't connect to server");
            System.exit(1);
        }
    }

    public void work(){
        
        while(true){
            try {
                Thread.sleep(50);
                BufferedImage screenCapture = this.screenCapturer.captureScreen();

                // convert BufferedImage to byte array and send to server
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(screenCapture, "png", baos);
                byte[] imageBytes = baos.toByteArray();

                this._out.writeInt(imageBytes.length);
                this._out.write(imageBytes);

                this._in.readUTF();

            } catch (IOException e) {
                System.out.println("Closed connection with the server");
                System.exit(0);
            }catch(InterruptedException e){

            }
        }
    }
}
