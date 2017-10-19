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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.jacobturner.castfast.CastFastEmail;
import me.jacobturner.castfast.CastFastFile;
import me.jacobturner.castfast.CastFastMP3;
import me.jacobturner.castfast.CastFastOptions;
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

	CastFastOptions options = new CastFastOptions();
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
			Dialog<ButtonType> dialog = new Dialog<>();
			dialog.setTitle("CastFast Options");
			dialog.setHeaderText("CastFast Options");
			ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			TextField stationName = new TextField(options.getValue("station_name"));
			TextField accessKey = new TextField(options.getValue("access_key"));
			PasswordField secretKey = new PasswordField();
			secretKey.setText(options.getValue("secret_key"));
			TextField bucketName = new TextField(options.getValue("bucket_name"));
			TextField regionName = new TextField(options.getValue("region_name"));
			TextField smtpServer = new TextField(options.getValue("smtp_server"));
			TextField port = new TextField(options.getValue("port"));
			TextField emailAddress = new TextField(options.getValue("email_address"));
			TextField username = new TextField(options.getValue("username"));
			PasswordField password = new PasswordField();
			password.setText(options.getValue("password"));
			CheckBox useSSLTLS = new CheckBox();
			useSSLTLS.setSelected(Boolean.valueOf(options.getValue("use_ssl_tls")));
			grid.add(new Label("Station Name"), 0, 0);
			grid.add(stationName, 1, 0);
			grid.add(new Label("Access Key"), 0, 1);
			grid.add(accessKey, 1, 1);
			grid.add(new Label("Secret Key"), 0, 2);
			grid.add(secretKey, 1, 2);
			grid.add(new Label("Bucket Name"), 0, 3);
			grid.add(bucketName, 1, 3);
			grid.add(new Label("Region Name"), 0, 4);
			grid.add(regionName, 1, 4);
			grid.add(new Label("SMTP Server"), 0, 5);
			grid.add(smtpServer, 1, 5);
			grid.add(new Label("Port"), 0, 6);
			grid.add(port, 1, 6);
			grid.add(new Label("Email Address"), 0, 7);
			grid.add(emailAddress, 1, 7);
			grid.add(new Label("Username"), 0, 8);
			grid.add(username, 1, 8);
			grid.add(new Label("Password"), 0, 9);
			grid.add(password, 1, 9);
			grid.add(new Label("Use SSL/TLS"), 0, 10);
			grid.add(useSSLTLS, 1, 10);
			dialog.getDialogPane().setContent(grid);
			dialog.showAndWait().ifPresent(result -> {
				if (result == saveButtonType) {
					options.setValue("station_name", stationName.getText());
					options.setValue("access_key", accessKey.getText());
					options.setValue("secret_key", secretKey.getText());
					options.setValue("bucket_name", bucketName.getText());
					options.setValue("region_name", regionName.getText());
					options.setValue("smtp_server", smtpServer.getText());
					options.setValue("port", port.getText());
					options.setValue("email_address", emailAddress.getText());
					options.setValue("username", username.getText());
					options.setValue("password", password.getText());
					options.setValue("use_ssl_tls", useSSLTLS.selectedProperty().getValue().toString());
				}
			});
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
