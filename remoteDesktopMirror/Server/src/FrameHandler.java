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
    private Point _mousePosition;

    private boolean _press;
    private boolean _click;
    private boolean _in;

    private TreeMap<String, Object> _keyAndMouseData;

    public FrameHandler(Server server){
        this._server = server;
        this._keyCode = 0;
        this._press = false;
        this._in = false;
        this._mousePosition = new Point();

        this._keyAndMouseData = new TreeMap<String,Object>();
        this._keyAndMouseData.put("mouse", this._mousePosition);
        this._keyAndMouseData.put("press", this._press);
        this._keyAndMouseData.put("click", this._click);
        this._keyAndMouseData.put("in", this._in);
        this._keyAndMouseData.put("keyboard", this._keyCode);

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
                _mousePosition.setLocation(e.getX(), e.getY()); // to do: get propotions to the other screen
                _click = true;
                _keyAndMouseData.replace("mouse", _mousePosition);
                _keyAndMouseData.replace("click", _click);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                _mousePosition.setLocation(e.getX(), e.getY()); // // to do: get propotions to the other screen
                _press = true;
                _keyAndMouseData.replace("mouse", _mousePosition);
                _keyAndMouseData.replace("press", _press);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                _press = false;
                _keyAndMouseData.replace("press", _press);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                _in = true;
                _keyAndMouseData.replace("in", _in);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                _press = false;
                _click = false;
                _in = false;
                _keyAndMouseData.replace("in", _in);
                _keyAndMouseData.replace("press", _press);
                _keyAndMouseData.replace("click", _click);
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

    public Point getMousePosition(){
        return this._mousePosition;
    }

    public TreeMap<String, Object> getMouseAndKeyboardData(){
        TreeMap<String, Object> keyAndMouseDataCopy = this._keyAndMouseData;
        this._keyAndMouseData.replace("click", false);
        return keyAndMouseDataCopy;
    }
}
