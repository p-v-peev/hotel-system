package GUIUpdater;

import javafx.application.Platform;
import RoomInfo.RoomObject;
import application.DialogWindow;
import application.Main;
import application.MainWindow;

public class Updater {
	
	private MainWindow mainWindow = null;
	private Main window = null;
	
	public Updater(MainWindow mainWindow, Main window) {
		setMainWindow(mainWindow);
		setWindow(window);
	}
	//Set the maiWindow
		public void setMainWindow(MainWindow mainWindow) {
			this.mainWindow = mainWindow;
		}
	//Set the window
		public void setWindow(Main window) {
			this.window = window;
		}
	//Show text for wrong username\password
		public void showWrongNamePassword() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					window.setFailedVisible();
					window.clearFields();
				}
			});
		}
	//Hide this window
			public void hideWindow() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						window.hideStage();
					}
				});
			}
	//Show the main window
		public void showMainWindow(){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					mainWindow.show();
					mainWindow.rootRecuestFocus();
				}
			});
		}
	//Add items to table
		public void addItems(String number, String type, String numberOfBeds, String view, String prise){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					mainWindow.addItemToList(new RoomObject(number, type, numberOfBeds, view, prise, mainWindow));
				}
			});
		}
	//Show dialog window with some text
		public void showDialog(String message){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					new DialogWindow(message);
				}
			});
		}
}
