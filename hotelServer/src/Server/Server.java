package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import DatabaseConnector.Connector;


public class Server {
	
	private static ServerSocket server = null;
	private final static int port = 12511;
	
	public static void main(String[] args) {
		try {
			startServer(port);
			acceptUsers();
		} catch (IOException e) {
			System.out.println("Can't start server on this port.");
			e.printStackTrace();
		}
	}
	
	private static void startServer(int port) throws IOException{
		server = new ServerSocket(port);
		System.out.println("Server is running.");
	}
	
private static void acceptUsers(){
		
		while (true) {
			try {
				Socket connection = server.accept();
				Connector connector = new Connector();
				User user = new User(connection, connector);
				connector.setUser(user);
				user.start();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Can't connect this user.");
			} catch (SQLException e) {
				System.out.println("Can't connect this user to databace.");
				e.printStackTrace();
			}
		}
	}
}

