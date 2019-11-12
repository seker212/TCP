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
                        if (textMsg.substring(1).equals("sendinv"))
                        {
                            out.write(new header(3,0, "", _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1).equals("accept"))
                        {
                            out.write(new header(4,0, "", _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1).equals("refuse"))
                        {
                            out.write(new header(5,0, "", _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1).equals("leave"))
                        {
                            out.write(new header(7,0, "", _sessionID).getBinHeader());
                        }
                        else if (textMsg.substring(1).equals("exit"))
                        {
                            out.write(new header(8,0, "", _sessionID).getBinHeader());
                        }else{
                            messageArea.append("Command " + textMsg.substring(1) + " not found\n");
                        }
                    }else
                    {
                        out.write(new header(6, 0, textMsg, _sessionID).getBinHeader());
                    }
                } catch (Exception f) {
                    //TODO: handle exception
                }
                textField.setText("");
            }
        });
    }

    private String getName(String textString) {
        return JOptionPane.showInputDialog(
            frame,
            textString,
            "Client name selection",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    private void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            header answerHeader = new header();
            
            if (socket.isConnected())
                out.write(new header(1, 0, "", 0).getBinHeader());

            while (answerHeader.readHeader(in)) {

                if (answerHeader.getOperationID() == 1 && answerHeader.getSessionID() != _sessionID){
                    _sessionID = answerHeader.getSessionID();
                    if (_name.isEmpty()){
                        _name = getName("Write your name");
                        out.write(new header(2, 0, _name, _sessionID).getBinHeader());
                    }
                }
                if (answerHeader.getSessionID() == _sessionID){
                    if(answerHeader.getOperationID() == 2)
                    {
                        if (answerHeader.getData().equals(_name)){
                            if (answerHeader.getAnswer() == 1){
                                this.frame.setTitle("Chat - " + _name);
                                textField.setEditable(true);
                            }else if(answerHeader.getAnswer() == 2){
                                _name = getName("Name taken. Please try another one");
                                out.write(new header(2, 0, _name, _sessionID).getBinHeader());
                            }

                        }
                    }
                    else if(answerHeader.getOperationID() == 3)
                    {
                        if (answerHeader.getAnswer() == 1){
                            //TODO: Should here be sth?
                        }else if(answerHeader.getAnswer() == 2){
                            messageArea.append("Invitation refused" + "\n");
                        }else if(answerHeader.getAnswer() == 3){
                            messageArea.append("ERROR: No reachable client" + "\n");
                        }
                    }
                    else if(answerHeader.getOperationID() == 8)
                    {
                        try { socket.close(); } catch (Exception e) {}  //FIXME: java.net.SocketException: Socket closed
                    }
                    else if(answerHeader.getOperationID() == 9)
                    {
                        messageArea.append(answerHeader.getData() + "\n");
                    }

                }else{
                    out.write(new header(1, 0, "", 0).getBinHeader());
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