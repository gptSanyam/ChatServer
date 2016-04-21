import java.io.BufferedReader;
import java.io.IOException;

public class ClientWriterThread implements Runnable{

	BufferedReader br;
	public ClientWriterThread(BufferedReader in) {
		// TODO Auto-generated constructor stub
		System.out.println("Started new writer thread.");
		br = in;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println(br.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
