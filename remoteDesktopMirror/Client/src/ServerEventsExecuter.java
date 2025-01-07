import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;

// This class executes given mouse and keyboard commands
public class ServerEventsExecuter {
    private Robot _robot;
    private GraphicsDevice _screen;

    public ServerEventsExecuter(){
        try {
            this._robot = new Robot();
            this._screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

        } catch (AWTException e) {
            System.err.println("Error creating robot");
            System.exit(1);
        }
    }

    // move mouse to the relative x,y positions on the screen
    public void moveMouse(double xRelativePosition, double yRelativePosition){
        Rectangle screenRect = this._screen.getDefaultConfiguration().getBounds();
        double xPosition = screenRect.getWidth() * xRelativePosition;
        double yPosition = screenRect.getHeight() * yRelativePosition;
        this._robot.mouseMove((int)xPosition, (int)yPosition);
    }

    // click mouse
    public void clickMouse(){
        this._robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this._robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    // keyboard key press
    public void keyboardPress(char keyChar){
        this._robot.keyPress(keyChar);
        this._robot.keyRelease(keyChar);        
    }
}
