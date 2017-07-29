/**
 * {
   “Sender”:8454997084,
   “Receiver”:9769737797,
   “Message”:”hello from the other side”,
   “MessageStatus”: MessageStatus.MSG_MSG}
 * @author sanyamgupta
 *
 */

public class MessageProtocol {
	
	private long sender;
	private long receiver;
	private String message;
	private MessageStatus messageStatus;
	
	MessageProtocol(int s, int r, String msg, MessageStatus msgStatus){
		this.sender = s;
		this.receiver = r;
		this.message = msg;
		this.messageStatus = msgStatus;
	}
	
	MessageProtocol(){
		
	}
	
	//setters
	public void setSender(long sender) {
		this.sender = sender;
	}

	public void setReceiver(long receiver) {
		this.receiver = receiver;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageStatus(MessageStatus messageStatus) {
		this.messageStatus = messageStatus;
	}
	
	//getters
	public long getReceiver(){
		return receiver;
	}
	public long getSender(){
		return sender;
	}
	public String getMessage(){
		return message;
	}
	public MessageStatus getMessageStatus(){
		return messageStatus;
	}
	
	
	public String toString(){
		return "sender: "+getSender()+" , receiver: "+getReceiver()+" , message: "+getMessage()+" , status: "+getMessageStatus();
	}
	
}
