import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
public class ServerEventsExecuter {
    private Robot _robot;

    public ServerEventsExecuter(){
        try {
            this._robot = new Robot();
        } catch (AWTException e) {
            System.err.println("Error creating robot");
            System.exit(1);
        }
    }
    public void pressKey(int keyCode){
        this._robot.keyPress(keyCode);
        this._robot.keyRelease(keyCode);
        System.out.println(KeyEvent.getKeyText(keyCode));
    }

    public void moveMouse(double xPropotionalPosition, double yPropotionalPosition){
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        Rectangle screenRect = screen.getDefaultConfiguration().getBounds();
        double xPosition = screenRect.getWidth() * xPropotionalPosition;
        double yPosition = screenRect.getHeight() * yPropotionalPosition;
        this._robot.mouseMove((int)xPosition, (int)yPosition);
    }

    public void clickMouse(){
        this._robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this._robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void keyboardPress(int keyCode){
        this._robot.keyPress(keyCode);
        this._robot.keyRelease(keyCode);        
    }
}
