import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * header object
 * @param operationID - 4 bits, operation number, range form 0 to 15
 * @param Answer - 3 bits, range form 0 to 7
 * @param dataLength 64 bits [8 bytes], length of data field in bytes
 * @param data - data in form of String
 * @param sessionID - 8 bits [1 byte], random for each client-server session
 */
public class header {
    private byte _operationAndAnswer;   // 4 bits op + 3 bits answer
    private long _dataLength;           // 3 bits
    private String _data;               // x bytes
    private byte _sessionID;            // 1 byte

    /**
     * Creates a new header filled with 0s and empty data field
     */
    public header(){
        this._operationAndAnswer = 0;
        this._dataLength = 0;
        this._data = "";
        this._sessionID = 0;
    }
    
    /**
     * Creastes a new header with given parameters
     * @param operationID operation number, range form 0 to 15
     * @param answer answer code, range form 0 to 7
     * @param data required;  mainly a String holding a message
     * @param sessionID reqiured; A random number given by a server for communication
     * @return header object
     */
    public header(byte operationID, byte answer, String data, byte sessionID){
        int tmp_convert = operationID << 3;
        tmp_convert += answer;
        
        this._operationAndAnswer = (byte) tmp_convert;
        this._dataLength = data.length();
        this._data = data;
        this._sessionID = sessionID;
    }

    public header(int operationID, int answer, String data, int sessionID){
        byte opID_byte = (byte)operationID;
        byte sessionID_byte = (byte) sessionID;
        int tmp_convert = opID_byte << 3;
        tmp_convert += answer;

        this._operationAndAnswer = (byte) tmp_convert;
        this._dataLength = data.length();
        this._data = data;
        this._sessionID = sessionID_byte;
    }

    /**
     * Creastes a new header with given parameters
     * @param operationAndAnswer one parmeter for operation number and answer. Binary should start with '0' followed by 4 bits of operation number and 3 bits of answer
     * @param data required;  mainly a String holding a message
     * @param sessionID reqiured; A random number given by a server for communication
     */
    public header(byte operationAndAnswer, String data, byte sessionID){
        this._operationAndAnswer = operationAndAnswer;
        this._dataLength = data.length();
        this._data = data;
        this._sessionID = sessionID;
    }

    /**
     * Gets information about operation type form header
     * @return operation number form 0 to 15
     */
    public int getOperationID(){
        return (_operationAndAnswer >> 3);
    }

    /**
     * Gets information about answer form header
     * @return int
     */
    public int getAnswer(){
        int a = 0;
        if ((_operationAndAnswer & 1) == 1)
            a += 0b001;
        if ((_operationAndAnswer & 2) == 2)
            a += 0b010;
        if ((_operationAndAnswer & 4) == 4)
            a += 0b100;
        
        return a;
        
    }

    /**
     * Gets information about size of data string form header
     * @return data string size in bytes
     */
    public long getDataLength(){
        return _dataLength;
    }

    /**
     * Gets information data form header
     * @return String of data, mainly a message
     */
    public String getData(){
        return _data;
    }

    /**
     * Gets session identifier form header
     * @return 8 bit identification number of a session
     */
    public byte getSessionID(){
        return _sessionID;
    }

    /**
     * Prints formated representation of header object on console using {@code System.out.print()} 
     */
    public void printSystem(){
        System.out.println("Operation number: " + getOperationID());
        System.out.println("Answer: " + getAnswer());
        System.out.println("Data length: " + _dataLength);
        System.out.println("Data: " + _data);
        System.out.println("Session identifier: " + _sessionID);
    }

    /**
     * Chages header into byte array for output
     * @return Array of bytes with a single 0 as a complement at the end
     */
    public byte[] getBinHeader(){
        ArrayList<Byte> array_out = new ArrayList<Byte>();

        long tmp_dataLength = _dataLength;
        for (int i = 0;i < 8;i++){
            array_out.add((byte) tmp_dataLength);
            tmp_dataLength >>>= 8;
        }

        array_out.add(_operationAndAnswer);

        Collections.reverse(array_out);

        byte[] dataString = _data.getBytes();
        for (var el : dataString){
            array_out.add(el); 
        }

        array_out.add(_sessionID);

        byte[] out = new byte[array_out.size()];
        for (int i=0;i<array_out.size();i++){
            out[i] = array_out.get(i).byteValue();
        }

        //moving bytes
        for (int i = 0; i < out.length; i++){
            out[i] <<= 1;
            //if most significant bit of out[i+1] == 1
            if (i != out.length - 1 && ((out[i+1] >> 7) & 1) == 1){
                out[i] += 0b1;
            }
        }
        

        return out;
    }

    /**
     * Reads binary input stream and fills header object with it.
     * @param inputStream Data stream of header instance
     * @return true of header isn't empty after reading input; false if header is empty
     * @throws IOException
     */
    public boolean readHeader(DataInputStream inputStream) throws IOException{
        ArrayList<Byte> inputABytes = new ArrayList<Byte>();
        while (inputABytes.size() < 9){
        byte inputByte = inputStream.readByte();
        inputABytes.add(inputByte);
        }

        if (inputABytes.size() == 9){

            // save first bit of data String
            // if least significat bit of first byte is 1
            byte firstDataBit = 0;
            if ((inputABytes.get(8).byteValue() & 1) == 1){
                firstDataBit = 1;
            }
            
            //move first 9 bytes of header 1 bit right
            for (int i = 8; i >= 0; i--){
                //wypycha najmniej znaczacy bit
                byte tmpByte = (byte) (inputABytes.get(i).byteValue() >> 1);
                
                //FIX
                if ((tmpByte >> 7 & 1) == 1){
                    tmpByte -= 0b10000000;
                }


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
                dataAndSessionID[i] = inputStream.readByte();
            }

            //move data and session ID by 1 bit right (usuwa dopelnienie)
            for (int i = (dataAndSessionID.length-1); i >= 0; i--) {
                dataAndSessionID[i] >>= 1;
                
                //FIX
                if ((dataAndSessionID[i] >> 7 & 1) == 1){
                    dataAndSessionID[i] -= 0b10000000;
                }

                if (i > 0 && ((dataAndSessionID[i-1] & 1) == 1)){
                    dataAndSessionID[i] += 0b10000000;
                }
            }
            if (firstDataBit == 1){
                dataAndSessionID[0] += 0b10000000;
            }
            
            this._operationAndAnswer = inputABytes.get(0);
            this._dataLength = dataLength;
            this._data = new String(dataAndSessionID, 0, (int)dataLength);
            this._sessionID = dataAndSessionID[dataAndSessionID.length-1];

            inputABytes.clear();
        }
        if (
            _operationAndAnswer == 0 &&
            _dataLength == 0 &&
            _data.isEmpty() &&
            _sessionID == 0
        ){ 
            return false; 
        }else{
            return true;
        }

    }
    

}