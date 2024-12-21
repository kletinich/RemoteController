import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class FrameHandler {
    private Frame _frame;
    private Canvas _canvas;

    private BufferedImage _screenCapture;

    private Server _server;

    private String _text;
    private int _keyCode;

    public FrameHandler(Server server){
        this._server = server;
        this._text = "";
        this._keyCode = 0;

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

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this._canvas.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
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

    public String getText(){
        return this._text;
    }

    public int getKeyCode(){
        return this._keyCode;
    }
}
