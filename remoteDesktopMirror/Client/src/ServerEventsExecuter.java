import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

    public void moveMouse(int x, int y){
        this._robot.mouseMove(x, y);
    }
}
