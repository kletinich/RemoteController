import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) throws Exception {
        Frame frame = new Frame("Screen mirror");
        frame.setSize(500,500);

        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

        Robot robot = new Robot(screen);

        Rectangle screenRect = screen.getDefaultConfiguration().getBounds();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        
        Canvas canvas = new Canvas(){
            BufferedImage screenCapture;

            @Override
            public void paint(Graphics g){
                screenCapture = robot.createScreenCapture(screenRect);
                g.drawImage(screenCapture, 0, 0, getWidth(), getHeight(), null);

                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

                double scaleX = (double) getWidth() / screenRect.width;
                double scaleY = (double) getHeight() / screenRect.height;

                int mouseX = (int) ((mouseLocation.x - screenRect.x) * scaleX);
                int mouseY = (int) ((mouseLocation.y - screenRect.y) * scaleY);

                g.setColor(Color.RED); 
                g.fillOval(mouseX, mouseY, 10, 10); 
            }

            @Override
            public void update(Graphics g){
                paint(g);
            }
        };

        frame.add(canvas);
        frame.setVisible(true);

        while(true){
            canvas.repaint();
            Thread.sleep(100);
        }

        
    }
}
