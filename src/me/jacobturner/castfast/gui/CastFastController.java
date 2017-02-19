package me.jacobturner.castfast.gui;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.jacobturner.castfast.CastFastEmail;
import me.jacobturner.castfast.CastFastFile;
import me.jacobturner.castfast.CastFastMP3;
import me.jacobturner.castfast.CastFastS3;
import me.jacobturner.castfast.CastFastShow;
import me.jacobturner.castfast.CastFastYAML;

public class CastFastController {
	@FXML
	private Button uploadButton;
	@FXML
	private Button optionsButton;
	@FXML
	private Button showsButton;
	@FXML
	private Button refreshButton;
	@FXML
	private Button exitButton;
	@FXML
	private Button browseButton;
	@FXML
	private DatePicker dateSelector;
	@FXML
	private ChoiceBox<String> showSelector;
	@FXML
	private TextField filePath;

	private FileChooser fileBrowse = new FileChooser();
	private String dateChosen = LocalDate.now().toString();
	private ArrayList<String> showList;
	private String showSelected;
	private String currentPath;

	public void initialize() {
		CastFastFile.checkShowsDir();
		showList = CastFastFile.getShowNames();
		CastFastFile.checkPodcastDir(showList);
		dateSelector.setValue(LocalDate.now());
		dateSelector.setOnAction(event -> {
			dateChosen = dateSelector.getValue().toString().replace("/", "-");
		});
		fileBrowse.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
		browseButton.setOnAction(event -> {
			File fileToOpen = fileBrowse.showOpenDialog(browseButton.getScene().getWindow());
			if (fileToOpen != null) {
				currentPath = fileToOpen.toString();
				filePath.setText(currentPath);
			}
		});
		Collections.sort(showList);
		showSelector.getItems().addAll(showList);
		showSelector.setOnAction(event -> {
			showSelected = showSelector.getValue();
		});
		optionsButton.setOnAction(event -> {
			try {
				Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(optionsButton.getScene().getWindow());
				BorderPane aboutWindow = (BorderPane)FXMLLoader.load(getClass().getResource("CastFastOptionsGUI.fxml"));
				Scene scene = new Scene(aboutWindow);
				dialog.setScene(scene);
				dialog.show();
			} catch(Exception error) {
				error.printStackTrace();
				outputMessage(error.getMessage(), AlertType.ERROR);
			}
		});
		showsButton.setOnAction(event -> {
			try {
				Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(optionsButton.getScene().getWindow());
				BorderPane aboutWindow = (BorderPane)FXMLLoader.load(getClass().getResource("CastFastShowsGUI.fxml"));
				Scene scene = new Scene(aboutWindow);
				dialog.setScene(scene);
				dialog.show();
			} catch(Exception error) {
				error.printStackTrace();
				outputMessage(error.getMessage(), AlertType.ERROR);
			}
		});
		exitButton.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
	}

	@FXML
	public void uploadProcess() {
		try {
			CastFastShow showData = CastFastYAML.readShow(showSelected);
			String newFile = CastFastMP3.updateFile(currentPath, dateChosen, showData);
			URL uploadedFile = CastFastS3.uploadFile(showSelected, newFile);
			CastFastEmail.sendEmail(showData.getEmail(), "Show successfully uploaded", uploadedFile.toString(), showSelected);
			outputMessage("Show successfully uploaded! Please check your email for a link to the show.", AlertType.INFORMATION);
		} catch (Exception error) {
			error.printStackTrace();
			outputMessage(error.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void refresh() {
		showList = CastFastFile.getShowNames();
		Collections.sort(showList);
		showSelector.getItems().clear();
		showSelector.getItems().addAll(showList);
		CastFastFile.checkPodcastDir(showList);
	}
	
	public void outputMessage(String message, AlertType alertType) {
		Alert alert = new Alert(alertType, message);
		alert.showAndWait();
	}
}
