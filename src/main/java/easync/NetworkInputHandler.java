package easync;

import java.io.BufferedReader;
import java.io.IOException;

public class NetworkInputHandler extends Thread {
	
	private BufferedReader input;
	
	public NetworkInputHandler(BufferedReader input) {
		this.input = input;
	}
	
	@Override
	public void run() {
		String line;
		try {
			while((line = input.readLine()) != null)
				System.out.println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
