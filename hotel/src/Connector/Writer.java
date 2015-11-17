package Connector;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Writer {
	
	private ObjectOutputStream output = null;
	
	public Writer(Socket connection) throws IOException {
		setOutput(connection);
	}
	//Make and set the output stream
		public void setOutput(Socket connection) throws IOException {
			this.output = new ObjectOutputStream(connection.getOutputStream());
		}
	//Write object to output stream	
		public void writeToOutput(Object object){
			try {
				output.writeObject(object);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error occurred while sending the message");
			}
		}
	//Close the output stream
		public void closeStreams(){
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Cant't close the output stream");
				}
			}
		}
}
