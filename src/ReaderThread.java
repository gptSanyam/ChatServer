import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReaderThread implements Runnable{
	int userid;

		public ReaderThread(int uid) {
		// TODO Auto-generated constructor stub
			userid = uid;
	}

		@Override
		public void run() {
			
			// TODO Auto-generated method stud
			
			try (
					BufferedReader in = new BufferedReader(
				        new InputStreamReader(Server.clientSocket.getInputStream()));
			){
					String inputLine="Nothing yet!";
					while ((inputLine = in.readLine()) != null){
						String[] strArr = inputLine.split(":");
						Integer dest  = Integer.parseInt(strArr[0]);
						String message = this.userid+": "+strArr[1].trim();
						
						//Server.msg = inputLine;
						//System.out.println("Read: "+inputLine);
						//Server.written = true;
						System.out.println(userid+" adding message to queue "+dest);
						Server.connArr[dest].msgQ.add(message); // should use a concurrent DS
						System.out.println(userid + " calling notify on "+ dest);
						Server.connArr[dest].mwn.doNotify();;
						
					}
					}catch (IOException e) {
				e.printStackTrace();
			}
		}
    }