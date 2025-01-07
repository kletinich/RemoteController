import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

// A frame displayer of a login option to the server
public class LoginFrame {
    final int MIN_PORT_VALUE = 1;
    final int MAX_PORT_VALUE = 65535;

    private Frame _loginFrame;

    private Label _ipLabel;
    private Label _portLabel;

    private Label _ipErrorLabel;
    private Label _portErrorLabel;

    private Label _connectionLabel;

    private TextField _ipTextField;
    private TextField _portTextField;

    private Button _confirmButton;

    private String _ipValue;
    private int _portValue;

    private Client _client;

    public LoginFrame(Client client){

        this._ipValue = "";
        this._portValue = 0;

        this._client = client;

        this._loginFrame = new Frame("Connect to server");
        this._loginFrame.setBounds(400, 200, 400, 200);

        this._loginFrame.setLayout(null);

        this._loginFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });

        this.initTextFields();
        this.initLabels();
        this.initButton();

        this._loginFrame.setVisible(true);
    }

    // initialize the labels of the frame
    public void initLabels(){
        this._ipLabel = new Label("Server IP:");
        this._portLabel = new Label("Server port:");

        this._ipLabel.setBounds(40, 50, 70, 20);
        this._portLabel.setBounds(40, 100, 70, 20);

        this._ipErrorLabel = new Label();
        this._portErrorLabel = new Label();

        this._ipErrorLabel.setBounds(250, 50, 100, 20);
        this._portErrorLabel.setBounds(250, 100, 100, 20);

        this._ipErrorLabel.setForeground(Color.red);
        this._portErrorLabel.setForeground(Color.red);

        this._connectionLabel = new Label();
        this._connectionLabel.setBounds(200, 150, 200, 20);
        this._connectionLabel.setForeground(Color.red);

        this._loginFrame.add(this._ipLabel);
        this._loginFrame.add(this._portLabel);

        this._loginFrame.add(this._ipErrorLabel);
        this._loginFrame.add(this._portErrorLabel);

        this._loginFrame.add(this._connectionLabel);
    }

    // initialize the textfields of the frame
    public void initTextFields(){
        this._ipTextField = new TextField();
        this._portTextField = new TextField();

        this._ipTextField.setBounds(120, 50, 100, 20);
        this._portTextField.setBounds(120, 100, 100, 20);

        this._loginFrame.add(this._ipTextField);
        this._loginFrame.add(this._portTextField);
    }

    // initialize the button of the frame
    public void initButton(){
        this._confirmButton = new Button("Confirm");
        this._confirmButton.setBounds(40, 150, 100, 20);

        this._confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isValidPort = false;
                boolean isValidIP = false;

                // missing ip text
                if(_ipTextField.getText().trim().isEmpty()){
                    _ipErrorLabel.setText("Missing value");
                    _connectionLabel.setText("");
                    isValidIP = false;
                }

                else{
                    _ipErrorLabel.setText("");
                    
                    isValidIP = isValidIPAdress(_ipTextField.getText());

                    // not a valid ip value
                    if(!isValidIP){
                        _ipErrorLabel.setText("Not a valid IP address");
                        _connectionLabel.setText("");
                    }
                }

                // missing port value
                if(_portTextField.getText().trim().isEmpty()){
                    _portErrorLabel.setText("Missing value");
                    _connectionLabel.setText("");
                    isValidPort = false;
                }

                else{
                    _portErrorLabel.setText(""); 

                    isValidPort = isValidPort(_portTextField.getText());

                    if(!isValidPort){

                        //not a valid port value
                        _portErrorLabel.setText("Not a valid port");
                        _connectionLabel.setText("");
                    }
                }

                // valid ip and port values
                if(isValidIP && isValidPort){
                    _ipValue = _ipTextField.getText();
                    _portValue = Integer.parseInt(_portTextField.getText());

                    _client.setIP(_ipValue);
                    _client.setPort(_portValue);

                    // connecting to the server with the given ip an port values
                    _client.connectToServer();
                    _connectionLabel.setText("Trying to connect to the server");

                    // connected to the server
                    if(_client._isConnected()){
                        _connectionLabel.setText("");
                        _client.work();
                    }

                    // couldn't connect to the server
                    else{
                        _connectionLabel.setText("Can't connect to the server");
                    }

                }
            }
            
        });

        this._loginFrame.add(this._confirmButton);
    }

    // check if a given text represents a valid ipv4 value
    public boolean isValidIPAdress(String text){
        String IPV4_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

        return IPV4_PATTERN.matcher(text).matches();
    }

    // check if a given text is a valid port value
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
        this._loginFrame.setVisible(visible);
    }

    public void setConnectionLabelText(String text){
        this._connectionLabel.setText(text);
    }
}
