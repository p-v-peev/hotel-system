package Connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import GUIUpdater.Updater;

public class Reader extends Thread {
	
	private ObjectInputStream input = null;
	private Updater updater = null;
	
	public Reader(Socket connection,Updater updater) throws IOException {
		setInput(connection);
		setUpdater(updater);
	}
	//Make and set input stream
		public void setInput(Socket connection) throws IOException {
			this.input = new ObjectInputStream(connection.getInputStream());
		}
	//Set updater for main window
		public void setUpdater(Updater updater) {
			this.updater = updater;
		}
	//Reads an object from input stream
		private Object readFromInput(){
			try {
				return input.readObject();
			}catch (SocketException e){
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error occurred while reading the message.");
				return null;
			}
		}
	//Close input stream
		public void closeStreams(){
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Can't close the input stream");
				}
			}
		}
	//Run method for this Thread
		public void run(){
			
			Object inputObject = null;
			String command = null;
			
			do {
				inputObject = readFromInput();
				if (inputObject instanceof String) {
					command = (String) inputObject;
					if (command.equals("log in failed"))
						updater.showWrongNamePassword();
					else if (command.equals("OK")) {
						updater.hideWindow();
						updater.showMainWindow();
					}
					else if (command.equals("Room"))
						readRoom();
					else if (command.equals("Dialog"))
						updater.showDialog((String) readFromInput());
				}
				
			} while (inputObject != null);
		}
	//Read information for some room send by server
		private void readRoom(){
			String number = (String) readFromInput();
			String type = (String) readFromInput();
			String numberOfBeds = (String) readFromInput();
			String view = (String) readFromInput();
			String prise = (String) readFromInput();
			updater.addItems(number, type, numberOfBeds, view, prise);
		}
}