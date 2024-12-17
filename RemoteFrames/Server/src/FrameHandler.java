import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FrameHandler {
    private Frame serverFrame;
    private Label serverLabel;
    private Button serverButton;

    private String text;

    private Server _server;

    public FrameHandler(Server server){
        this.text = "";

        this._server = server;

        this.serverFrame = new Frame("Server");
        this.serverFrame.setSize(300, 300);
        this.serverFrame.setLayout(new FlowLayout());

        this.serverLabel = new Label("Initializing server", Label.CENTER);
        this.serverFrame.add(this.serverLabel);

        this.serverButton = new Button("Close connection");
        this.serverButton.setBounds(0, 0, 20, 20);
        this.serverFrame.add(this.serverButton);

        this.serverFrame.setVisible(true);

        serverFrame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                text = "Mouse clicked";
            }

            @Override
            public void mousePressed(MouseEvent e) {
                text = "Mouse pressed";
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                text = "Mouse released";
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                text = "Mouse entered";
            }

            @Override
            public void mouseExited(MouseEvent e) {
                text = "Mouse exited";
            }
            
        });

        this.serverButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                _server.closeConnection();
            }
            
        });
    }

    public void setText(String text){
        this.serverLabel.setText(text);
    }

    public String getText(){
        return this.text;
    }
}
