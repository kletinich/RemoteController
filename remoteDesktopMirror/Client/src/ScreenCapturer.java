import java.awt.*;
import java.awt.image.BufferedImage;

// This class captures screenshots
public class ScreenCapturer {
    private Robot _robot;

    private GraphicsDevice _screen;
    private Rectangle _screenRect;

    public ScreenCapturer(){        
        try {
            this._screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
            this._robot = new Robot(this._screen);
            this._screenRect = this._screen.getDefaultConfiguration().getBounds();

        } catch (AWTException e) {
            System.err.println("Error while initializing screen capture proccess.");
            System.exit(1);
        }
    }

    public BufferedImage captureScreen(){
        return this._robot.createScreenCapture(this._screenRect);
    }
}
