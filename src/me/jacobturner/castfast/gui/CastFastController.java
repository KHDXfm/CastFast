package me.jacobturner.castfast.gui;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import javax.mail.MessagingException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.jacobturner.castfast.CastFastEmail;
import me.jacobturner.castfast.CastFastFile;
import me.jacobturner.castfast.CastFastMP3;
import me.jacobturner.castfast.CastFastS3;
import me.jacobturner.castfast.CastFastSQL;

public class CastFastController {
	@FXML
	private Button uploadButton;
	@FXML
	private Button optionsButton;
	@FXML
	private Button exitButton;
	@FXML
	private Button browseButton;
	@FXML
	private DatePicker dateSelector;
	@FXML
	private ChoiceBox<String> showSelector;

	private CastFastSQL sqlFile = new CastFastSQL();
	private FileChooser fileBrowse = new FileChooser();
	private String dateChosen = LocalDate.now().toString();
	private ArrayList<String> showList = sqlFile.getNames();
	private String showSelected;
	private String currentPath;

	public void initialize() {
		CastFastFile.checkDir(showList);
		dateSelector.setValue(LocalDate.now());
		dateSelector.setOnAction(event -> {
			dateChosen = dateSelector.getValue().toString().replace("/", "-");
		});
		fileBrowse.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
		browseButton.setOnAction(event -> {
			File fileToOpen = fileBrowse.showOpenDialog(browseButton.getScene().getWindow());
			if (fileToOpen != null) {
				currentPath = fileToOpen.toString();
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
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		exitButton.setOnAction(event -> {
			sqlFile.close();
			Platform.exit();
			System.exit(0);
		});
	}

	@FXML
	public void uploadProcess() {
		ArrayList<String> showData = sqlFile.getShow(showSelected);
		String newFile = CastFastMP3.updateFile(currentPath, dateChosen, showData);
		URL uploadedFile = CastFastS3.uploadFile(showData, newFile, showSelected);
		try {
			CastFastEmail.sendEmail(showData.get(2), "Show successfully uploaded", uploadedFile.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
