package me.jacobturner.castfast.gui;

import java.util.ArrayList;
import java.util.Collections;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import me.jacobturner.castfast.CastFastFile;
import me.jacobturner.castfast.CastFastShow;
import me.jacobturner.castfast.CastFastShowHelper;
import me.jacobturner.castfast.CastFastYAML;

public class CastFastShowsController {
	@FXML
	private ChoiceBox<String> showSelector;
	@FXML
	private TextField showName;
	@FXML
	private TextField djs;
	@FXML
	private TextField djEmails;
	@FXML
	private TextField airs;
	@FXML
	private Button addButton;
	@FXML
	private Button updateButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button closeButton;

	private ArrayList<String> showList = CastFastFile.getShowNames();
	private boolean isLeftClick;
	
	public void initialize() {
		Collections.sort(showList);
		showSelector.getItems().add("<new show>");
		showSelector.getItems().addAll(showList);
		showSelector.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_toggle, Number new_toggle) {
				if ((int)new_toggle >= 0 && isLeftClick) {
					String selectedShow = showSelector.getItems().get((int)new_toggle);
					if (selectedShow.equals("<new show>")) {
						showName.setText("");
						djs.setText("");
						djEmails.setText("");
						airs.setText("");
						addButton.setDisable(false);
						updateButton.setDisable(true);
						deleteButton.setDisable(true);
					} else {
						try {
							CastFastShow selectedShowData = CastFastYAML.readShow(selectedShow);
							showName.setText(selectedShowData.getName());
							djs.setText(CastFastShowHelper.arrayToString(selectedShowData.getDJ()));
							djEmails.setText(CastFastShowHelper.arrayToString(selectedShowData.getEmail()));
							airs.setText(selectedShowData.getAirs());
							addButton.setDisable(true);
							updateButton.setDisable(false);
							deleteButton.setDisable(false);
						} catch (Exception error) {
							error.printStackTrace();
							outputMessage(error.getMessage(), AlertType.ERROR);
						}
					}
				}
			}
		});
		showSelector.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()) {
					isLeftClick = true;
				}
			}
		});
		closeButton.setOnAction(event -> {
			try {
				Stage stage = (Stage)closeButton.getScene().getWindow();
				stage.close();
			} catch (Exception error) {
				error.printStackTrace();
				outputMessage(error.getMessage(), AlertType.ERROR);
			}
		});
		showSelector.getSelectionModel().selectFirst();
		addButton.setDisable(false);
		updateButton.setDisable(true);
		deleteButton.setDisable(true);
	}
	
	@FXML
	public void addShow() {
		try {
			CastFastYAML.editShow(showName.getText(), showName.getText(),
					djs.getText().split(", "), djEmails.getText().split(", "),
					airs.getText());
			showSelector.getItems().add(showName.getText());
			showSelector.getSelectionModel().select(showName.getText());
		} catch (Exception error) {
			error.printStackTrace();
			outputMessage(error.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void deleteShow() {
		String tempShowName = showName.getText();
		CastFastFile.deleteShow(tempShowName);
		showSelector.getItems().remove(tempShowName);
		showSelector.getSelectionModel().select("<new show>");
		showName.setText("");
		djs.setText("");
		djEmails.setText("");
		airs.setText("");
		addButton.setDisable(false);
		updateButton.setDisable(true);
		deleteButton.setDisable(true);
	}
	
	@FXML
	public void updateShow() {
		String oldShow = showSelector.getSelectionModel().getSelectedItem();
		showSelector.getItems().remove(oldShow);
		try {
			CastFastYAML.editShow(oldShow, showName.getText(),
					djs.getText().split(", "), djEmails.getText().split(", "),
					airs.getText());
			showSelector.getItems().add(showName.getText());
			showSelector.getSelectionModel().select(showName.getText());
		} catch (Exception error) {
			error.printStackTrace();
			outputMessage(error.getMessage(), AlertType.ERROR);
		}
	}
	
	public void outputMessage(String message, AlertType alertType) {
		Alert alert = new Alert(alertType, message);
		alert.showAndWait();
	}
}
