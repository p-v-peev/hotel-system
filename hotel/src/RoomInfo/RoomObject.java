package RoomInfo;

import application.CustomerDataWindow;
import application.MainWindow;
import javafx.scene.control.Button;

public class RoomObject {
	private String roomNumber = null;
	private String type = null;
	private String numberOfbeds = null;
	private String outLook = null;
	private String pricePernight = null;
	Button openConfiguration = null;
	private MainWindow window = null;
	
	public RoomObject(String number, String type, String numberOfbeds, String outLook, String pricePernight, MainWindow window) {
		setRoomNumber(number);
		setRoomNumber(String.valueOf(roomNumber));
		setType(type);
		setNumberOfbeds(numberOfbeds);
		setOutLook(outLook);
		setPricePernight(pricePernight);
		setWindow(window);
		openConfiguration = new Button("Make reservation");
		openConfiguration.setOnMouseClicked(e -> new CustomerDataWindow(window, roomNumber));
	}
	
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNumberOfbeds() {
		return numberOfbeds;
	}
	public void setNumberOfbeds(String numberOfbeds) {
		this.numberOfbeds = numberOfbeds;
	}
	public String getOutLook() {
		return outLook;
	}
	public void setOutLook(String outLook) {
		this.outLook = outLook;
	}
	public String getPricePernight() {
		return pricePernight;
	}
	public void setPricePernight(String pricePernight) {
		this.pricePernight = pricePernight;
	}

	public MainWindow getWindow() {
		return window;
	}

	public void setWindow(MainWindow window) {
		this.window = window;
	}

	public Button getOpenConfiguration() {
		return openConfiguration;
	}

	public void OpenConfiguration(Button openConfiguration) {
		this.openConfiguration = openConfiguration;
	}
}
