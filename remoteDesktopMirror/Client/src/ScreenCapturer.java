import java.awt.*;
import java.awt.image.BufferedImage;

// This class captures screenshots
public class ScreenCapturer {
    private Robot robot;

    private GraphicsDevice screen;
    private Rectangle screenRect;

    public ScreenCapturer(){        
        try {
            this.screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
            this.robot = new Robot(this.screen);
            this.screenRect = screen.getDefaultConfiguration().getBounds();

        } catch (AWTException e) {
            System.err.println("Error while initializing screen capture proccess.");
            System.exit(1);
        }
    }

    public BufferedImage captureScreen(){
        return this.robot.createScreenCapture(this.screenRect);
    }
}
