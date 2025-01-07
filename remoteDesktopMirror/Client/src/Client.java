import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

 
//The client shares a consistant screenview with a server. 
//The server controls the mouse and keyboard of the local desktop.
public class Client {
    public static String DEFAULT_IP = "127.0.0.1";
    public static int DEFAULT_PORT = 8080;

    private String _ip;
    private int _port;

    private Socket _socket;

    private DataOutputStream _out;                      // Sender of data to server
    private DataInputStream _in;                        // Receiver of server data

    private ScreenCapturer _screenCapturer;             // Screenshot of the screen
    private ServerEventsExecuter _serverEventsExecuter; // Executer of server commands

    public Client(){
        this(DEFAULT_IP, DEFAULT_PORT);
    }

    public Client(String ip, int port){
        this._ip = ip;
        this._port = port;

        this._screenCapturer = new ScreenCapturer();
        this._serverEventsExecuter = new ServerEventsExecuter();
    }

    // connect to server
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

    // communicate with the server
    public void work(){
        
        while(true){
            try {
                Thread.sleep(50);

                // capture the current screen status
                BufferedImage screenCapture = this._screenCapturer.captureScreen();

                // convert BufferedImage to byte array and send to server
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(screenCapture, "png", baos);
                byte[] imageBytes = baos.toByteArray();

                this._out.writeInt(imageBytes.length);
                this._out.write(imageBytes);

                // receive mouse and keyboard data from the server
                double mouseRelativeX = this._in.readDouble();  // relative x position of the mouse
                double mouseRelativeY = this._in.readDouble();  // relative y position of the mouse
                boolean mousePress = this._in.readBoolean();    // is mouse pressed in the server side       
                boolean mouseClick = this._in.readBoolean();    // is the mouse clicked in the server side
                boolean in = this._in.readBoolean();            // is the mouse inside the screen bounds 
                int keyCode = this._in.readInt();             // keyboard key pressed in the server side

                // move and click the mouse with the given relative position in the server
                if(in && (mouseClick || mousePress)){
                    this._serverEventsExecuter.moveMouse(mouseRelativeX, mouseRelativeY);
                    this._serverEventsExecuter.clickMouse();
                }

                // press the keyboard key with the given key pressed in the server
                if(keyCode != 0){
                    this._serverEventsExecuter.keyboardPress(keyCode);
                }

            } catch (IOException e) {
                System.out.println("Closed connection with the server");
                System.exit(0);
            } catch (InterruptedException e) {
                System.err.println("Thread sleep error");
                System.exit(1);
            }
        }
    }
}
