import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A multithreaded chat room server. When a client connects the server requests a screen
 * name by sending the client the text "SUBMITNAME", and keeps requesting a name until
 * a unique one is received. After a client submits a unique name, the server acknowledges
 * with "NAMEACCEPTED". Then all messages from that client will be broadcast to all other
 * clients that have submitted a unique screen name. The broadcast messages are prefixed
 * with "MESSAGE".
 *
 * This is just a teaching example so it can be enhanced in many ways, e.g., better
 * logging. Another is to accept a lot of fun commands, like Slack.
 */
public class Server {

    // All client names, so we can check for duplicates upon registration.
    private static Set<String> names = new HashSet<>();

     // The set of all the print writers for all the clients, used for broadcast.
    private static clientList openClients = new clientList();
    private static clientList room = new clientList();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    /**
     * The client handler task.
     */
    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        int clientListNR = -1;
        // byte sessionID = newSessionID();
        byte sessionID = 127;

        /**
         * Constructs a handler thread, squirreling away the socket. All the interesting
         * work is done in the run method. Remember the constructor is called from the
         * server's main method, so this has to be as short as possible.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        private byte newSessionID(){
            Random random = new Random();
            byte rdsessionID = (byte) random.nextInt();
            return rdsessionID;
        }
        /**
         * Services this thread's client by repeatedly requesting a screen name until a
         * unique one has been submitted, then acknowledges the name and registers the
         * output stream for the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        public void run() {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                header command = new header();
                   
                
                while (true) {
                    command.readHeader(in);

                    if (command.getOperationID() == 1)
                    {
                        out.write(new header(1, 1, "", sessionID).getBinHeader());
                        if (clientListNR == -1){
                            clientListNR = openClients.add(new clientOut(out, "", sessionID));
                        }else if (clientListNR == 0 || clientListNR == 1){
                            openClients.replace(clientListNR, new clientOut(out, "", sessionID));
                        }
                    }
                    if (command.getSessionID() == sessionID){
                        if (command.getOperationID() == 2)
                        {
                            if(names.contains(command.getData())){
                                out.write(new header(2, 2, command.getData(), sessionID).getBinHeader());
                            }else{
                                name = command.getData();
                                names.add(name);
                                openClients.replace(clientListNR, new clientOut(out, name, sessionID));
                                out.write(new header(2, 1, name, sessionID).getBinHeader());
                            }
                        }
                        else if (command.getOperationID() == 3)
                        {
                            if (openClients.isActive()){
                                int otherI = -1;
                                if (clientListNR == 0)
                                    otherI = 1;
                                else if (clientListNR == 1)
                                    otherI = 0;

                                openClients.get(otherI)._out.write(new header(9, 0, name + " has sent you an invitation", openClients.get(otherI)._sessionID).getBinHeader());
                            }else{
                                out.write(new header(3, 3, "", sessionID).getBinHeader());
                            }
                        }
                        else if (command.getOperationID() == 4)
                        {
                            //TODO: send 'header(3,1,"",sessionID)'?

                            if (openClients.isActive()){
                                room = openClients;
                                room.get(0)._out.write(new header(9, 0, room.get(1).getName() + " has joined the room", room.get(0).getSessionID()).getBinHeader());
                                room.get(1)._out.write(new header(9, 0, room.get(0).getName() + " has joined the room", room.get(1).getSessionID()).getBinHeader());
                            }else{
                                out.write(new header(3, 3, "", sessionID).getBinHeader());
                            }
                        }
                        else if (command.getOperationID() == 5)
                        {
                            if (openClients.isActive()){
                                int otherI = -1;
                                    if (clientListNR == 0)
                                        otherI = 1;
                                    else if (clientListNR == 1)
                                        otherI = 0;
                                openClients.get(otherI)._out.write(new header(3, 2, "", openClients.get(otherI)._sessionID).getBinHeader());
                            }else{
                                out.write(new header(3, 3, "", sessionID).getBinHeader());
                            }
                        }
                        else if (command.getOperationID() == 6)
                        {
                            if (room.isActive()){
                                room.get(0)._out.write(new header(9, 0, name + ": " + command.getData(), room.get(0).getSessionID()).getBinHeader());
                                room.get(1)._out.write(new header(9, 0, name + ": " + command.getData(), room.get(1).getSessionID()).getBinHeader());
                            }else{
                                out.write(new header(9, 0, name + ": " + command.getData(), sessionID).getBinHeader());
                            }
                        }
                        else if (command.getOperationID() == 7)
                        {
                            room.reset();
                            if(openClients.isActive()){
                                int otherI = -1;
                                if (clientListNR == 0)
                                    otherI = 1;
                                else if (clientListNR == 1)
                                    otherI = 0;
                                openClients.get(otherI)._out.write(new header(9, 0, name + " has left the room", openClients.get(otherI)._sessionID).getBinHeader());
                            }
                            out.write(new header(9,0,"You have left the room", sessionID).getBinHeader());
                        }
                        else if (command.getOperationID() == 8)
                        {
                            if (room.isActive()){
                                room.reset();
                                if(openClients.isActive()){
                                    int otherI = -1;
                                    if (clientListNR == 0)
                                        otherI = 1;
                                    else if (clientListNR == 1)
                                        otherI = 0;
                                    openClients.get(otherI)._out.write(new header(9, 0, name + " has left the room", sessionID).getBinHeader());
                                }
                                out.write(new header(9,0,"You have left the room", sessionID).getBinHeader());
                            }
                            openClients.remove(clientListNR);
                            out.write(new header(8,1,"",sessionID).getBinHeader());
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    if (room.isActive())
                        room.reset();
                    openClients.remove(clientListNR);
                }
                if (name != null) {
                    System.out.println(name + " is leaving");
                    names.remove(name);
                    /* if(openClients.isActive()){
                        int otherI = -1;
                        if (clientListNR == 0)
                            otherI = 1;
                        else if (clientListNR == 1)
                            otherI = 0;
                        openClients.get(otherI)._out.write(new header(9, 0, name + " has left the room", openClients.get(otherI)._sessionID).getBinHeader());
                    } */
                }
                try { socket.close(); } catch (IOException e) {}
            }
        }
            
        
        }

    private static class clientList{
        private clientOut[] cList;
        private boolean[] checktable; 
        private int clientNumber;

        public clientList(){
            this.cList = new clientOut[2];
            this.checktable = new boolean[]{false, false};//false == empty; true == taken
            this.clientNumber = 0;
        }

        public boolean isEmpty(){
            return clientNumber == 0;
        }

        public int size(){
            return clientNumber;
        }

        public int add(clientOut cOut){
            for (int i = 0; i < 2; i++) {
                if (checktable[i] == false){
                    cList[i] = cOut;
                    checktable[i] = true;
                    if (isActive()){
                        try {
                            if (i == 0){
                                cList[1]._out.write(new header(9, 0, "Client " + cOut._name + " is available", cList[1]._sessionID).getBinHeader());
                            }else if (i == 1)
                                cList[0]._out.write(new header(9, 0, "Client " + cOut._name + " is available", cList[0]._sessionID).getBinHeader());
                        } catch (Exception e) {
                            //TODO: handle exception
                        }
                    }
                    return i;
                }
            }
            return -1;
        }
        
        public void replace(int index, clientOut cOut){
            cList[index] = cOut;
        }

        public void remove(int index){
            checktable[index] = false;
        }

        public void reset(){
            checktable = new boolean[]{false, false};
        }

        public clientOut get(int index) throws NullPointerException{
            if (checktable[index] == false){
                throw new NullPointerException();
            }
            return cList[index];
        }

        public clientOut[] getList(){
            return cList;
        }

        public boolean isActive(){
            return checktable[0] && checktable[1];
        }
    }
    
    private static class clientOut{
        private DataOutputStream _out;
        private String _name;
        private byte _sessionID;

        public clientOut(DataOutputStream out, String name, byte sessionID){
            this._out = out;
            this._name = name;
            this._sessionID = sessionID;
        }

        /**
         * @return the _name
         */
        public String getName() {
            return _name;
        }
        /**
         * @return the _out
         */
        public DataOutputStream getOut() {
            return _out;
        }
        /**
         * @return the _sessionID
         */
        public byte getSessionID() {
            return _sessionID;
        }

    }
}