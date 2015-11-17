package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogWindow extends Stage {
	
	public DialogWindow(String message) {
		
		initModality(Modality.APPLICATION_MODAL);
		setTitle("YM hotel support system");
		
		Label messageLabel = new Label(message);
		Button ok = new Button("Ok");
		VBox root = new VBox(5, messageLabel, ok);
		root.setStyle("-fx-alignment: center");
		
		ok.setOnMouseClicked(e -> hide());
		ok.setStyle("-fx-min-width: 90px");
		
		Scene scene = new Scene(root, 300, 100);
		setScene(scene);
		showAndWait();
		root.requestFocus();
	}
}
