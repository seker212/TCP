import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
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
    private static Set<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(500);
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

        /**
         * Constructs a handler thread, squirreling away the socket. All the interesting
         * work is done in the run method. Remember the constructor is called from the
         * server's main method, so this has to be as short as possible.
         */
        public Handler(Socket socket) {
            this.socket = socket;
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
                ArrayList<Byte> inputABytes = new ArrayList<Byte>();
                
                while (true) {
                    byte inputByte = in.readByte();
                    inputABytes.add(inputByte);
                    if (inputABytes.size() == 9){

                        // save first bit of data String
                        // if least significat bit of first byte is 1
                        byte firstDataBit = 0;
                        if ((inputABytes.get(0).byteValue() & 1) == 1){
                            firstDataBit = 1;
                        }
                        
                        //move first 9 bytes of header 1 bit right
                        for (int i = 8; i >= 0; i--){
                            //wypycha najmniej znaczacy bit
                            byte tmpByte = (byte) (inputABytes.get(i).byteValue() >>> 1); //FIXME: >>> works as if it's >>
                            //if least significant bit of i-1 == 1
                            if (i > 0 && ((inputABytes.get(i-1).byteValue() & 1) == 1)){
                                tmpByte += 0b10000000;
                            }
                            inputABytes.set(i, tmpByte);
                        }

                        //read dataLength
                        long dataLength = 0;
                        for (int i = 1; i < 9; i++) {
                            dataLength += inputABytes.get(i);
                            if (i < 8)
                            dataLength <<= 8;
                        }
                        
                        //read data and sessionID
                        byte[] dataAndSessionID = new byte[(int) dataLength + 1]; 
                        for (int i = 0; i < dataAndSessionID.length; i++) {
                            dataAndSessionID[i] = in.readByte();
                        }

                        //move data and session ID by 1 bit right (usuwa dopelnienie)
                        for (int i = (dataAndSessionID.length-1); i >= 0; i--) {
                            dataAndSessionID[i] >>>= 1; //FIXME: >>> works as if it's >>
                            if (i > 0 && ((dataAndSessionID[i-1] & 1) == 1)){
                                dataAndSessionID[i] += 0b10000000;
                            }
                        }
                        if (firstDataBit == 1){
                            dataAndSessionID[0] += 0b10000000;
                        }

                        // header command = new header(inputABytes.get(0) , new String(dataAndSessionID, 0, (int)dataLength), dataAndSessionID[dataAndSessionID.length-1]);
                        System.out.println(inputABytes.get(0)); 
                        System.out.println(new String(dataAndSessionID, 0, (int)dataLength));
                        System.out.println(dataAndSessionID[dataAndSessionID.length-1]);
                        inputABytes.clear();
                    }
                    
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println(name + " is leaving");
                    names.remove(name);
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + " has left");
                    }
                }
                try { socket.close(); } catch (IOException e) {}
            }
        }
        
            
        
        }
}