import java.io.IOException;
import java.io.PrintWriter;

public class WriterThread implements Runnable{
	int userid;

		public WriterThread(int uid) {
		// TODO Auto-generated constructor stub
			userid = uid;
	}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try (
				PrintWriter out =
				        new PrintWriter(Server.clientSocket.getOutputStream(), true);
			){
				while(true){
					
					Server.connArr[this.userid].mwn.doWait();
					while(!Server.connArr[this.userid].msgQ.isEmpty()){
						String msgToSend =  Server.connArr[this.userid].msgQ.remove();
						System.out.println(this.userid+" writing: "+msgToSend);
						
						out.println(msgToSend);
					}
					
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }