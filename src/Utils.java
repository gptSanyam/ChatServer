import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created by sanyamgupta on 16/07/17.
 */
public class Utils {

    public static void transmogrify(byte[] buff, int size){
        for(int i = 0; i<size; i++){
            if(Character.isLetter(buff[i])){
                buff[i] = (byte) (buff[i] ^ ' ');
            }
        }
    }

    public static boolean authenticateUser(Message message){
        System.out.println("Authenticating user: "+message.getSender());
        return true; //for now
    }


    public static void main(String[] args) throws IOException {
        /*ByteBuffer b = ByteBuffer.allocate(8);
        b.putChar('a');
        b.putChar('b');
        b.putChar('1');
        transmogrify(b.array(), 6);
        System.out.write(b.array());*/
        String s = "blah";
        byte[] arr = s.getBytes(StandardCharsets.UTF_8);
        System.out.println(arr.length);

    }

}
