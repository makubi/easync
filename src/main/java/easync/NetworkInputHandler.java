package easync;

import java.io.BufferedReader;
import java.io.IOException;

public class NetworkInputHandler extends Thread {
	
	private NetworkAccess networkHandler;
	
	public NetworkInputHandler(NetworkAccess networkHandler) {
		this.networkHandler = networkHandler;
	}

	@Override
	public void run() {
		String line;
		BufferedReader input = networkHandler.getInput();
		try {
			while((line = input.readLine()) != null) {
				System.out.println(line);
				
				if(line.equals(NetworkCommands.CMD_SEND_FILE)) {
					String filepath = input.readLine();
					int bufferSize = Integer.parseInt(input.readLine());
					int chunks = Integer.parseInt(input.readLine());
					networkHandler.receivingFile(filepath, bufferSize, chunks);
				}
				/*
				String[] data = line.split(":");
				String command = data[0];
				String status = null;
				String message = null;
				
				if(data.length > 1)
					status = data[1];
				
				if(data.length > 2)
					message = data[2];
				*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
