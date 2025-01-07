import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;


// BUG: IF THIS CLASS INTEGRATES TO THE CODE, THE FRAME HANDLER DOESNT RESPOND TO COMMANDS AND IMAGES

// A frame that displays options for server initialization
public class ServerInitFrame {
    final int MIN_PORT_VALUE = 1;
    final int MAX_PORT_VALUE = 65535;

    private Frame _serverInitFrame;

    private Label _ipLabel;
    private Label _portLabel;

    private Label _ipErrorLabel;
    private Label _portErrorLabel;

    private TextField _portTextField;

    private Button _localhostButton;
    private Button _publicHostButton;

    private Button _confirmButton;

    private boolean _localHostButtonPressed;
    private boolean _publicHostButtonPressed;

    private String _ipValue;
    private int _portValue;

    private Server _server;

    public ServerInitFrame(Server server){
        this._ipValue = "";
        this._portValue = 0;

        this._localHostButtonPressed = false;
        this._publicHostButtonPressed = false;

        this._server = server;

        this._serverInitFrame = new Frame("Initialize server");
        this._serverInitFrame.setBounds(400, 200, 500, 200);

        this._serverInitFrame.setLayout(null);

        this._serverInitFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });

        this.initLabels();
        this.initTextField();
        this.initButtons();

        this._serverInitFrame.setVisible(true);
    }

    public void initLabels(){
        this._ipLabel = new Label("Server IP:");
        this._portLabel = new Label("Server port:");
        this._ipErrorLabel = new Label();
        this._portErrorLabel = new Label();

        this._ipLabel.setBounds(40, 50, 70, 20);
        this._portLabel.setBounds(40, 100, 70, 20);
        this._ipErrorLabel.setBounds(370, 50, 100, 20);
        this._portErrorLabel.setBounds(370, 100, 100, 20);

        this._serverInitFrame.add(this._ipLabel);
        this._serverInitFrame.add(this._portLabel);
        this._serverInitFrame.add(this._ipErrorLabel);
        this._serverInitFrame.add(this._portErrorLabel);
    }

    public void initButtons(){
        this._localhostButton = new Button("127.0.0.1");
        try {
            InetAddress bindAddress = InetAddress.getLocalHost();
            this._publicHostButton = new Button(bindAddress.getHostAddress());
        } catch (IOException e) {
            this._publicHostButton = new Button("Can't get host address");
        }

        this._confirmButton = new Button("Confirm");
        
        this._localhostButton.setBounds(140, 50, 100, 20);
        this._publicHostButton.setBounds(250, 50, 100, 20);
        this._confirmButton.setBounds(40, 150, 100, 20);

        this._localhostButton.setBackground(Color.lightGray);
        this._publicHostButton.setBackground(Color.lightGray);
        this._confirmButton.setBackground(Color.lightGray);

        this._localhostButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _localHostButtonPressed = true;
                _publicHostButtonPressed = false;
                _ipErrorLabel.setText("");
                _localhostButton.setBackground(Color.green);
                _publicHostButton.setBackground(Color.lightGray);
            }
        });

        this._publicHostButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _localHostButtonPressed = false;
                _publicHostButtonPressed = true;
                _ipErrorLabel.setText("");
                _publicHostButton.setBackground(Color.green);
                _localhostButton.setBackground(Color.lightGray);
            }
        });

        this._confirmButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                boolean isValidPort = false;

                if(!_localHostButtonPressed && !_publicHostButtonPressed){
                    _ipErrorLabel.setText("Missing choice");
                    _ipErrorLabel.setForeground(Color.red);
                }

                if(_portTextField.getText().trim().isEmpty()){
                    _portErrorLabel.setText("Missing value");
                    _portErrorLabel.setForeground(Color.red);
                }

                else{
                    _portErrorLabel.setText("");

                    isValidPort = isValidPort(_portTextField.getText());

                    if(!isValidPort){
                        _portErrorLabel.setText("Not a valid port");
                    }                    
                }

                if(isValidPort && (_localHostButtonPressed || _publicHostButtonPressed)){
                    if(_localHostButtonPressed){
                        _server.setIP(true);
                    }

                    else{
                        _server.setIP(false);
                    }

                    _portValue = Integer.parseInt(_portTextField.getText());
                    _server.setPort(_portValue);
                    _server.startServer();
                }
            }
        });

        this._serverInitFrame.add(this._localhostButton);
        this._serverInitFrame.add(this._publicHostButton);
        this._serverInitFrame.add(this._confirmButton);
    }

    public void initTextField(){
        this._portTextField = new TextField();
        this._portTextField.setBounds(140, 100, 100, 20);

        this._serverInitFrame.add(this._portTextField);
    }

    public boolean isValidPort(String text){
        try{
            int port = Integer.parseInt(text);

            if(port > MIN_PORT_VALUE && port <= MAX_PORT_VALUE){
                return true;
            }

            return false;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public void toggleFrameVisibility(boolean visible){
        this._serverInitFrame.setVisible(visible);
    }
}
