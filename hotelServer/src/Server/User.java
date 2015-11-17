package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

import DatabaseConnector.Connector;

public class User extends Thread {
	
	private Connector connector = null;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
	private int idNumber = 0;
	
	public User(Socket connection, Connector connector) throws IOException {
		setStreams(connection);
		this.connector = connector;
	}
	//Make and set output and input streams for this user
		private void setStreams(Socket connection){
			try {
				output = new ObjectOutputStream(connection.getOutputStream());
				input = new ObjectInputStream(connection.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	//Close input and output streams
		private void closeStreams(){
			try {
				if(input != null)
					input.close();
				if(output != null)
					output.close();
				connector.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Немога да затворя потоците");
				return;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Немога да затворя връзката с базата данни");
				e.printStackTrace();
			}
		}

	//Set the value for idNumber for the employee on this thread
		public void setIdNumber(int idNumber) {
			this.idNumber = idNumber;
		}
	//Get the value of idNumber 
			public int getIdNumber() {
				return this.idNumber;
			}
	//Run method for this thread
	public void run(){
		Object inputObject = null;
		String command = null;
		
		while (true) {
			inputObject = readFromInput();
			if (inputObject instanceof String){
			
				command = (String) inputObject;
				
				if (command.equals("Log In")) {
					logIn();
				}
				else if (command.equals("Search")){
					search();
				}
				else if (command.equals("Reserve")) {
					reserve();
				}
				else if (command.equals("Free")) {
					connector.freeRoom(Integer.parseInt((String) readFromInput()));
				}
			}
			
				if (command.equals("exit"))
					break;
		}
		
		closeStreams();
		System.out.println("User is disconected.");
	}
	//Send user info to database
	private void logIn() {
		String firstName = (String) readFromInput();
		String password = (String) readFromInput();
		
		if (firstName != null  && password != null)
			connector.checkForEmployee(firstName, password);
	}
	//Send searching info to database
	private void search() {
		String roomType = (String) readFromInput();
		int roomPrice = Integer.parseInt((String) readFromInput());
		String roomView = (String) readFromInput();
		connector.getFreeRooms(roomType, roomPrice, roomView);
	}
	//Send info to make reservation in database
	private void reserve(){
		int roomNumber = Integer.parseInt((String) readFromInput());
		String date = (String) readFromInput();
		String firstName = (String) readFromInput();
		String lastName = (String) readFromInput();
		String pin = (String) readFromInput();
		connector.reserveRoom(roomNumber, pin, firstName, lastName, date);
	}
}
