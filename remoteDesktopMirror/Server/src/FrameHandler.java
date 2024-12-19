import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class FrameHandler {
    private Frame _frame;
    private Canvas _canvas;

    private BufferedImage _screenCapture;

    private Server _server;

    public FrameHandler(Server server){
        this._server = server;

        this._frame = new Frame("Client mirror");
        this._frame.setSize(500, 500);

        this._frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                _server.closeServer();
            }
        });

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

        this._frame.add(this._canvas);
        this._frame.setVisible(true);
    }

    public void repaint(BufferedImage updatedScreenCapture){
        this._screenCapture = updatedScreenCapture;
        this._canvas.repaint();
    }
}
