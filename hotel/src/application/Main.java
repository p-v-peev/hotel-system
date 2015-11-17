package application;
	
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Connector.Reader;
import Connector.Writer;
import GUIUpdater.Updater;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
	
	private final int port = 12511;
	private Text failed = null;
	private TextField name = null;
	private PasswordField password = null;
	private Writer writer = null;
	private Stage reffStage = null;
	
	@Override
	public void start(Stage stage) {
		try {
			
			//Makes everything ready for communicate with server
			Socket connection = new Socket("localhost", port);
			writer = new Writer(connection);
			MainWindow window = new MainWindow(writer);
			Updater updater = new Updater(window, this);
			Reader reader = new Reader(connection, updater);
			reader.start();
			window.setReader(reader);
			reffStage = stage;
			
			//Show Log In form
			failed = new Text("Wrong name\\password");
			name = new TextField();
			password = new PasswordField();
			Button logIn = new Button("Log In");
			
			failed.setStyle("-fx-fill: #F45050;");
			failed.setVisible(false);
			name.setPromptText("Enter your first name");
			password.setPromptText("Enter your password");
			name.setOnMouseClicked(e -> failed.setVisible(false));
			password.setOnMouseClicked(e -> failed.setVisible(false));
			name.setStyle("-fx-max-width: 250px");
			password.setStyle("-fx-max-width: 250px");
			logIn.setStyle("-fx-max-width: 100px;");
			logIn.setOnMouseClicked(e -> logIn());
			
			VBox root = new VBox(failed, name, password, logIn);
			root.setStyle("-fx-spacing: 10; -fx-alignment: center; -fx-max-width: 250px;");
			
			stage.setTitle("Welcome to YM hotel support system");
			stage.setOnCloseRequest(e -> {
				writer.writeToOutput("exit");
				writer.closeStreams();
				reader.closeStreams();
				reader.interrupt();
			});
			
			Scene scene = new Scene(root, 600, 400);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();
			root.requestFocus();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("Can't connect to this port");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO exception occurred");
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	//Send to server information to log in
		private void logIn(){
			String firstName = name.getText();
			String userPassword = password.getText();
			
			if (firstName.length() > 0 && userPassword.length() > 0) {
				writer.writeToOutput("Log In");
				writer.writeToOutput(firstName);
				writer.writeToOutput(userPassword);
			}
			else
				new DialogWindow("Please fill all fields.");
		}
	//Hide this stage
		public void hideStage(){
			reffStage.hide();
		}
	//Clear name and password fields
		public void clearFields(){
			name.clear();
			password.clear();
		}
	//Set failed text visible
		public void setFailedVisible(){
			failed.setVisible(true);
		}
}
