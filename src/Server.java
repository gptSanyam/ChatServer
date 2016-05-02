
import java.net.*;
import java.io.*;
import java.lang.*;

public class Server {
	
	static Socket clientSocket;
	
	static Connection[] connArr = new Connection[5];
	
    public static void main(String[] args) throws IOException {
      
        	@SuppressWarnings("resource")
			ServerSocket serverSocket =
                new ServerSocket(Integer.parseInt("54321"));
            
        	System.out.println("Listening connection now...");
        	
        	int uid = 0;
        	
        	while(true){
        		clientSocket = serverSocket.accept();
        		System.out.println("Got connection request from "+uid);
        		//Instantiate Connection
        		connArr[uid] = new Connection();
        		
        		(new Thread(new ReaderThread(uid))).start();
            	(new Thread(new WriterThread(uid))).start();
            	
            	uid++;
        	}
        	
        } 
}
