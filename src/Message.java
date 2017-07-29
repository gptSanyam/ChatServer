/**
 * Created by sanyamgupta on 29/07/17.
 */
public abstract class Message {

    String sender;
    String destination;
    String message;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString(){
        return "Message - Sender: "+getSender()+" , Destination: "+getDestination()+" , Text: "+getMessage();
    }

}
