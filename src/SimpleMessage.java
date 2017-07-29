/**
 * Created by sanyamgupta on 16/07/17.
 */
public class SimpleMessage extends Message{

    public SimpleMessage(String msg){
        if(msg.indexOf(';')>=0){
            String[] msgParts = msg.split(";");
            setSender(msgParts[0]);
            setDestination(msgParts[1]);
            setMessage(msgParts[2]);
        }else{
            System.out.println("Invalid message format");
        }

    }

}
