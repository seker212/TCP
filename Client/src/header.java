import java.util.ArrayList;
import java.util.Collections;

/**
 * header
 * For test the operationID is 5 bit long instead of 4 bits and starts with '0'
 */
public class header {
    private byte _operationAndAnswer;
    private long _dataLength;
    private String _data;
    private byte _sessionID;

    /**
     * 
     * @param operationID range form 0 to 15
     * @param answear 0 for client, 1 for server answear
     * @param data_length number of characters (bytes) of data param
     * @param data required
     * @param sessionID 
     */

    public header(byte operationID, boolean answear, String data, byte sessionID){
        int tmp_convert = operationID << 3;
        if (answear)
            tmp_convert += 0b111;
        
        this._operationAndAnswer = (byte) tmp_convert;
        this._dataLength = data.length();
        this._data = data;
        this._sessionID = sessionID;

 
    }

    /**
     * Chages header into byte array for output
     * @return byte[]
     */
    public byte[] getBinHeader(){
        ArrayList<Byte> array_out = new ArrayList<Byte>();

        long tmp_dataLength = _dataLength;
        for (int i = 0;i < 8;i++){
            array_out.add((byte) tmp_dataLength);
            tmp_dataLength >>= 8;
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

        //TODO: activate for loop to move 
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
    

}