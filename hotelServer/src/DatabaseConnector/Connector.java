package DatabaseConnector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import Server.User;

public class Connector {
	private Connection connection = null;
	private CallableStatement statement = null;
	private ResultSet result = null;
	private User user = null;
	
		public Connector() throws SQLException {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "server", "server");
				System.out.println("User is connected to database");
		}
	//Try to close connection to the database
		public void closeConnection() throws SQLException{
			if(!connection.isClosed())
				connection.close();
		}
	//Set user for this connection to database
		public void setUser(User user){
			this.user = user;
		}
	//Check for employee
		public void checkForEmployee(String firstName, String password){
			try {
				statement = connection.prepareCall("{CALL chaeck_for_employee (?, ?, ?)};");
				statement.setString(1, firstName);
				statement.setString(2, password);
				statement.registerOutParameter(3, Types.INTEGER);
				statement.execute();
				int employeeId;
				
				if ((employeeId = statement.getInt(3)) != 0){
					user.setIdNumber(employeeId);
					user.writeToOutput("OK");
				}
				else
					user.writeToOutput("log in failed");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error occurred while sending command to the database.");
			}
		}
	//Return number of rooms that are free
		public void getFreeRooms(String roomType, int roomPrice, String roomView){
			try {
				statement = connection.prepareCall("{CALL find_free_room (?, ?, ?)};");
				statement.setString(1, roomType);
				statement.setInt(2, roomPrice);
				statement.setString(3, roomView);
				statement.execute();
				
				result = statement.getResultSet();
				
				boolean b = false;
				
				while (result.next()) {
					user.writeToOutput("Room");
					user.writeToOutput(String.valueOf(result.getInt("roomNumber")));
					user.writeToOutput(result.getString("type"));
					user.writeToOutput(String.valueOf(result.getInt("numberOfbeds")));
					user.writeToOutput(result.getString("outLook"));
					user.writeToOutput(String.valueOf(result.getInt("pricePerNight")));
					b = true;
				}
				if (!b) {
					user.writeToOutput("Dialog");
					user.writeToOutput("There are no free rooms that match your criteria.");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error occurred while sending command to the database.");
			}
			
		}
	//Reserve room 
		public void reserveRoom(int roomNumber, String pin, String firstName, String lastName, String date){
			try {
				statement = connection.prepareCall("{CALL check_for_client(?, ?)}");
				statement.setString(1, pin);
				statement.registerOutParameter(2, Types.NVARCHAR);
				statement.execute();
				
				String a = statement.getString(2);
				
				
				if (a == null) {
					statement = connection.prepareCall("{CALL add_client(?, ?, ?)}");
					statement.setString(1, pin);
					statement.setString(2, firstName);
					statement.setString(3, lastName);
					statement.execute();
				}
				
				statement = connection.prepareCall("{CALL add_to_room_history(?, ?, ?, ?)}");
				statement.setInt(1, roomNumber);
				statement.setDate(2, Date.valueOf(date));
				statement.setString(3, pin);
				statement.setInt(4, user.getIdNumber());
				statement.execute();
				
				statement = connection.prepareCall("{CALL reserve_room(?)}");
				statement.setInt(1, roomNumber);
				statement.execute();
				
				user.writeToOutput("Dialog");
				user.writeToOutput("Reservation is complete.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	//Free room
		public void freeRoom(int roomNumber){
			if (checkForRoom(roomNumber) == 0 ){
				user.writeToOutput("Dialog");
				user.writeToOutput("We do not have room with this number.");
			}
			else {
				try {
					statement = connection.prepareCall("{CALL free_room(?)}");
					statement.setInt(1, roomNumber);
					statement.execute();
					
					user.writeToOutput("Dialog");
					user.writeToOutput("Room is free.");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
	//Check for room
		private int checkForRoom(int roomNumber){
			try {
				statement = connection.prepareCall("{CALL check_for_room(?, ?)}");
				statement.setInt(1, roomNumber);
				statement.registerOutParameter(2, Types.INTEGER);
				statement.execute();
				
				return statement.getInt(2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
}
	