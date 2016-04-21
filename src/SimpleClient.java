import java.io.*;
import java.net.*;

public class SimpleClient {
    public static void main(String[] args) throws IOException {
        
        
        String hostName = "127.0.0.1";
        int portNumber = Integer.parseInt("54321");

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
        		
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        	
        	   
                
        ) {
        	(new Thread(new ClientWriterThread(in))).start();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}