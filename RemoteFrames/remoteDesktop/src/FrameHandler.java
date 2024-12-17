import java.awt.*;

public class FrameHandler {
    private Frame clientFrame;
    private Label clientLabel;

    public FrameHandler(){
        this.clientFrame = new Frame("Client");
        this.clientFrame.setSize(300, 300);

        this.clientLabel = new Label("Connecting to server", Label.CENTER);
        this.clientFrame.add(this.clientLabel);

        this.clientFrame.setVisible(true);
    }

    public void setText(String text){
        this.clientLabel.setText(text);
    }
}
