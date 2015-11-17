package application;

import Connector.Reader;
import Connector.Writer;
import RoomInfo.RoomObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Stage {

	private Writer writer = null;
	private Reader reader = null;
	private BorderPane root = null;
	private String roomType = null;
	private int prise = 0;
	private String view = null;
	private String date = null;
	private String last = null;
	private ObservableList<RoomObject> tableData= null;

	public MainWindow(Writer writer) {
		setTitle("YM hotel support system");
		
		setOnCloseRequest(e -> {
			writer.writeToOutput("exit");
			writer.closeStreams();
			reader.closeStreams();
			reader.interrupt();
		});
		
		setWriter(writer);
		
		tableData = FXCollections.observableArrayList();
		TableView<RoomObject> table = new TableView<>(tableData);
		table.setPadding(new Insets(15, 10, 10, 10));
		makeColumns(table);
		

		root = new BorderPane();
		root.setLeft(makeChoiseBar());
		root.setCenter(table);
		
		Scene scene = new Scene(root, 900, 600);
		setMaxWidth(900);
		setScene(scene);
		//show();
	}

	//Set writer to write to output stream
		public Writer getWriter() {
			return writer;
		}
	//Get writer if needed somewhere
		public void setWriter(Writer writer) {
			this.writer = writer;
		}
	//Set instance of reader objecy from Main
		public void setReader(Reader reader) {
			this.reader = reader;
		}
	//Make root to request focus
		public void rootRecuestFocus(){
			root.requestFocus();
		}
	//Getter to dthe date
		public String getDate() {
			return date;
		}
	//Getter to the lastDate
		public String getLast() {
			return last;
		}
	//Make left choise bar
		private VBox makeChoiseBar() {
			Button search = new Button("Search for room");
			search.setOnMouseClicked(e -> {
				tableData.clear();
				sendSearchQuery();
			});
			Button freeRoom = new Button("Free room");
			TextField roomNumber = new TextField();
			
			roomNumber.setPromptText("Enter room's number to free.");
			
			freeRoom.setOnMouseClicked(e ->{
				if (roomNumber.getText().length() > 0) {
					writer.writeToOutput("Free");
					writer.writeToOutput(roomNumber.getText());
					roomNumber.clear();
				}
				else 
					new DialogWindow("Please enter room's number.");
			} );
			VBox freeBox = new VBox(3, roomNumber, freeRoom);
			freeBox.setStyle("-fx-alignment: center");
			VBox leftBox = new VBox(20, makeTypeBox(),makePriceBox(), makeViewBox(), search, freeBox);
			leftBox.setPadding(new Insets(15, 15, 10, 10));
			leftBox.setStyle("-fx-alignment: top-center");
			return leftBox;
		}
	//Make type choise box
		private VBox makeTypeBox(){
			ToggleGroup typeGroup = new ToggleGroup();
			RadioButton disabledRoom = new RadioButton("Disabled room");
			disabledRoom.setToggleGroup(typeGroup);
			RadioButton singleRoom = new RadioButton("Single room");
			singleRoom.setToggleGroup(typeGroup);
			singleRoom.setSelected(true);
			roomType = singleRoom.getText();
			RadioButton doubleRoom = new RadioButton("Double room");
			doubleRoom.setToggleGroup(typeGroup);
			RadioButton twinRoom = new RadioButton("Twin room");
			twinRoom.setToggleGroup(typeGroup);
			RadioButton trippleRoom = new RadioButton("Triple room");
			trippleRoom.setToggleGroup(typeGroup);
			RadioButton queenRoom = new RadioButton("Queen room");
			queenRoom.setToggleGroup(typeGroup);
			RadioButton kingRoom = new RadioButton("King room");
			kingRoom.setToggleGroup(typeGroup);
			RadioButton suite = new RadioButton("Suite");
			suite.setToggleGroup(typeGroup);
			
			typeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(
						ObservableValue<? extends Toggle> observable,
						Toggle oldValue, Toggle newValue) {
					roomType = ((RadioButton) newValue.getToggleGroup().getSelectedToggle()).getText();
				}
			});
			return new VBox(1, singleRoom, doubleRoom, twinRoom, trippleRoom, queenRoom, kingRoom, suite, disabledRoom);
		}
	//Make price choise box
		private VBox makePriceBox() {
			ScrollBar upperLimit = new ScrollBar();
			upperLimit.setMax(200);
			upperLimit.setValue(upperLimit.getMax()/2);
			Label priseView = new Label(String.valueOf(upperLimit.getValue()));
		
			prise = (int)(upperLimit.getValue());
			
			upperLimit.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(
						ObservableValue<? extends Number> observable,
						Number oldValue, Number newValue) {
					prise = (newValue.intValue());
					priseView.setText(String.valueOf(prise));
				}
			});
			
			VBox price = new VBox(5, priseView, upperLimit);
			price.setStyle("-fx-alignment: center");
			return price;
		}
	// Маке view choise box
		private VBox makeViewBox(){
			ToggleGroup viewGroup = new ToggleGroup();
			RadioButton north = new RadioButton("North");
			north.setToggleGroup(viewGroup);
			RadioButton south = new RadioButton("South");
			south.setToggleGroup(viewGroup);
			south.setSelected(true);
			view = south.getText();
			
			viewGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

				@Override
				public void changed(
						ObservableValue<? extends Toggle> observable,
						Toggle oldValue, Toggle newValue) {
					view = ((RadioButton) newValue.getToggleGroup().getSelectedToggle()).getText();
				}
			});
			
			return new VBox(5, south, north);
		}
	//Send searching data to the server
		private void sendSearchQuery(){
				writer.writeToOutput("Search");
				writer.writeToOutput(roomType);
				writer.writeToOutput(String.valueOf(prise));
				writer.writeToOutput(view);
		}
	//Add items to tableData list
		public void addItemToList(RoomObject item){
			tableData.add(item);
		}
	//Make columns for the table
		@SuppressWarnings("unchecked")
		private void makeColumns(TableView<RoomObject> table){
			//Room number column
			TableColumn<RoomObject, String> roomNumber = new TableColumn<>("Room number");
			roomNumber.setMinWidth(100);
			roomNumber.setStyle("-fx-alignment: center");
			roomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
			
		//Room type column
			TableColumn<RoomObject, String> roomType = new TableColumn<>("Room type");
			roomType.setMinWidth(100);
			roomType.setStyle("-fx-alignment: center");
			roomType.setCellValueFactory(new PropertyValueFactory<>("type"));
			
		//Number of beds column
			TableColumn<RoomObject, String> numberOfBeds = new TableColumn<>("Number of beds");
			numberOfBeds.setMinWidth(100);
			numberOfBeds.setStyle("-fx-alignment: center");
			numberOfBeds.setCellValueFactory(new PropertyValueFactory<>("numberOfbeds"));
		
		//Outlook column
			TableColumn<RoomObject, String> outLook = new TableColumn<>("Outlook");
			outLook.setMinWidth(100);
			outLook.setStyle("-fx-alignment: center");
			outLook.setCellValueFactory(new PropertyValueFactory<>("outLook"));
		
		//Price column
			TableColumn<RoomObject, String> price = new TableColumn<>("Price");
			price.setMinWidth(100);
			price.setStyle("-fx-alignment: center");
			price.setCellValueFactory(new PropertyValueFactory<>("pricePernight"));
			
		//Reserve column
			TableColumn<RoomObject, Button> reserve = new TableColumn<>("Reservation");
			reserve.setMinWidth(200);
			reserve.setStyle("-fx-alignment: center");
			reserve.setCellValueFactory(new PropertyValueFactory<>("openConfiguration"));
			table.getColumns().addAll(roomNumber, roomType, numberOfBeds, outLook, price, reserve);
		}
}

