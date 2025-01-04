import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.TreeMap;


public class FrameHandler {
    private Frame _frame;
    private Canvas _canvas;

    private BufferedImage _screenCapture;

    private Server _server;

    private int _keyCode;
    TreeMap<String, Double> _mousePropotionalPosition;

    private boolean _press;
    private boolean _click;
    private boolean _inFrameBounds;

    public FrameHandler(Server server){
        this._server = server;
        this._keyCode = 0;
        this._press = false;
        this._inFrameBounds = false;
        this._mousePropotionalPosition = new TreeMap<>();
        this._mousePropotionalPosition.put("x", 0.0);
        this._mousePropotionalPosition.put("y", 0.0);

        // init the components of the class
        this.initFrame();
        this.initCanvas();

        this._frame.setVisible(true);
    }

    private void initFrame(){
        this._frame = new Frame("Client mirror");
        this._frame.setSize(500, 500);

        this._frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                _server.closeServer();
            }
        });
    }

    private void initCanvas(){
        this._canvas = new Canvas(){
            BufferedImage screen;

            @Override
            public void paint(Graphics g){
                screen = _screenCapture;

                g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
            }

            @Override
            public void update(Graphics g){
                paint(g);
            }
        };

        this._canvas.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                _mousePropotionalPosition = calculatePropotionalPosition(e.getX(), e.getY());
                _click = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                _mousePropotionalPosition = calculatePropotionalPosition(e.getX(), e.getY());
                _press = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                _press = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                _inFrameBounds = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                _press = false;
                _click = false;
                _inFrameBounds = false;
            }
        });

        this._canvas.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                _keyCode = e.getKeyCode();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                _keyCode = e.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                _keyCode = 0;
            }
            
        });

        this._frame.add(this._canvas);
    }

    public void repaint(BufferedImage updatedScreenCapture){
        this._screenCapture = updatedScreenCapture;
        this._canvas.repaint();
    }

    public int getKeyCode(){
        return this._keyCode;
    }

    public TreeMap<String, Double> getMousePropotionalPosition(){
        return this._mousePropotionalPosition;
    }

    public boolean isPressed(){
        return this._press;
    }

    public boolean isClicked(){
        return this._click;
    }

    public boolean inFrameBounds(){
        return this._inFrameBounds;
    }

    // calculate the propotional x and y position inside the frame (example: (0.5, 0.5) is the center of the frame)
    public TreeMap<String, Double> calculatePropotionalPosition(int xPosition, int yPosition){
        double xPropotionalPosition = (double)xPosition / this._frame.getWidth();
        double yPropotionalPosition = (double)yPosition / this._frame.getHeight();

        TreeMap<String, Double> propotionalPosition = new TreeMap<>();
        propotionalPosition.put("x", xPropotionalPosition);
        propotionalPosition.put("y", yPropotionalPosition);

        return propotionalPosition;
    }
}
