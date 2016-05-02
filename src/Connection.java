import java.util.LinkedList;
import java.util.Queue;

/**
 * protocol to send msg is "destination": "message"
 * @author sanyamgupta
 *
 */
public class Connection {
	WaitNotify mwn;
	Queue<String> msgQ; //Modify to a concurrent Queue
	boolean isOnline = false; // will be used later
	
	public Connection(){
		mwn = new WaitNotify();
		msgQ = new LinkedList<String>();
	}
	
}
