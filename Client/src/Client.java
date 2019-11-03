import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A simple Swing-based client for the chat server. Graphically it is a frame with a text
 * field for entering messages and a textarea to see the whole dialog.
 *
 * The client follows the following Chat Protocol. When the server sends "SUBMITNAME" the
 * client replies with the desired screen name. The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are already in use. When the
 * server sends a line beginning with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all chatters connected to the
 * server. When the server sends a line beginning with "MESSAGE" then all characters
 * following this string should be displayed in its message area.
 */
public class Client {

    String serverAddress;
    DataInputStream in;
    DataOutputStream out;
    JFrame frame = new JFrame("Chat");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);
    byte _sessionID = 0;
    String _name = "";

    /**
     * Constructs the client by laying out the GUI and registering a listener with the
     * textfield so that pressing Return in the listener sends the textfield contents
     * to the server. Note however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED message from
     * the server.
     */
    public Client(String serverAddress) {
        this.serverAddress = serverAddress;

        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.pack();

        // Send textField on enter then clear to prepare for next message
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String textMsg = textField.getText();
                try {
                    if (textMsg.startsWith("\\"))
                    {
                        if (textMsg.substring(1) == "search")
                        {
                            out.write(new header(3,false, "", _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1, 9) == "sendinv ")
                        {
                            out.write(new header(5,false, textMsg.substring(9), _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1) == "accept")
                        {
                            out.write(new header(7,false, "", _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1) == "refuse")
                        {
                            out.write(new header(8,false, "", _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1) == "leave")
                        {
                            out.write(new header(13,false, "", _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1) == "exit")
                        {
                            out.write(new header(14,false, "", _sessionID).getBinHeader());
                        }
                    }else
                    {
                        out.write(new header(11, false, textMsg, _sessionID).getBinHeader());
                    }
                } catch (Exception f) {
                    //TODO: handle exception
                }
                textField.setText("");
            }
        });
    }

    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    private void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            header command = new header();

            while (command.readHeader(in)) {
                //TODO: Command handling

                if (command.getOperationID() == 0){
                    _sessionID = command.getSessionID();
                    _name = getName();
                    out.write(new header(1, false, _name, _sessionID).getBinHeader());
                }
                if (command.getSessionID() == _sessionID){
                    if(command.getOperationID() == 1)
                    {
                        if (command.getAnswer() && command.getData() == _name){
                            this.frame.setTitle("Chat - " + _name);
                            textField.setEditable(true);
                        }else if(!command.getAnswer() && command.getData() == _name)/* name taken */{
                            _name = getName();
                            out.write(new header(1, false, _name, _sessionID).getBinHeader());
                        }
                    }
                    else if(command.getOperationID() == 2)
                    {
    
                    }
                    else if(command.getOperationID() == 3)
                    {
                        
                    }
                    else if(command.getOperationID() == 4)
                    {
    
                    }
                    else if(command.getOperationID() == 5)
                    {
    
                    }
                    else if(command.getOperationID() == 6)
                    {
    
                    }
                    else if(command.getOperationID() == 7)
                    {
    
                    }
                    else if(command.getOperationID() == 8)
                    {
    
                    }
                    else if(command.getOperationID() == 9)
                    {
    
                    }
                    else if(command.getOperationID() == 10)
                    {
    
                    }
                    else if(command.getOperationID() == 11)
                    {
    
                    }
                    else if(command.getOperationID() == 12)
                    {
    
                    }
                    else if(command.getOperationID() == 13)
                    {
    
                    }
                    else if(command.getOperationID() == 14)
                    {
                        // socket.close();
    
                    }
                    else if(command.getOperationID() == 15)
                    {
                    }

                }
                


            }
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        Client client = new Client(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}