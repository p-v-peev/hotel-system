package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerDataWindow extends Stage{
	
	public CustomerDataWindow(MainWindow mainWindow, String roomNumber) {
		
		setTitle("Reservation window");
		initModality(Modality.APPLICATION_MODAL);
		
		TextField firstName = new TextField();
		TextField lastName = new TextField();
		TextField pin = new TextField();
		TextField lastDate = new TextField();
		Button makeReservation = new Button("Make reservation");
		
		firstName.setPromptText("Enter client's first name");
		firstName.setStyle("-fx-max-width: 200px");
		
		lastName.setPromptText("Enter client's last name");
		lastName.setStyle("-fx-max-width: 200px");
		
		pin.setPromptText("Enter client's personal identification number");
		pin.setStyle("-fx-max-width: 200px");
		
		lastDate.setPromptText("Enter last day of client's residence");
		lastDate.setStyle("-fx-max-width: 200px");
		
		makeReservation.setStyle("-fx-max-widt: 90px");
		
		makeReservation.setOnMouseClicked(e -> {
			String date = lastDate.getText();
			String fName = firstName.getText();
			String lName = lastName.getText();
			String PIN = pin.getText();
			
			if (date.length() > 0 && fName.length() > 0 && lName.length() > 0 && PIN.length() > 0) {
				mainWindow.getWriter().writeToOutput("Reserve");
				mainWindow.getWriter().writeToOutput(roomNumber);
				mainWindow.getWriter().writeToOutput(date);
				mainWindow.getWriter().writeToOutput(fName);
				mainWindow.getWriter().writeToOutput(lName);
				mainWindow.getWriter().writeToOutput(PIN);
				hide();
			}
			else
				new DialogWindow("Please fill all fields.");
		});
		
		VBox root = new VBox(10, firstName, lastName, pin, lastDate, makeReservation);
		root.setStyle("-fx-alignment: center");
		
		Scene scene = new Scene(root, 400, 300);
		setScene(scene);
		showAndWait();
	}
}
