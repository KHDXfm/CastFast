package me.jacobturner.castfast.gui;

import java.util.ArrayList;
import java.util.Collections;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.jacobturner.castfast.CastFastSQL;

public class CastFastShowsController {
	@FXML
	private ChoiceBox<String> showSelector;
	@FXML
	private TextField showName;
	@FXML
	private TextField djs;
	@FXML
	private TextField djEmail;
	@FXML
	private TextField dateAndTime;
	@FXML
	private Button addButton;
	@FXML
	private Button updateButton;
	@FXML
	private Button closeButton;
	
	private CastFastSQL sqlFile = new CastFastSQL();
	private ArrayList<String> showList = sqlFile.getNames();
	
	public void initialize() {
		Collections.sort(showList);
		showSelector.getItems().add("<new show>");
		showSelector.getItems().addAll(showList);
		showSelector.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> ov, Number old_toggle, Number new_toggle) {
				String selectedShow = showSelector.getSelectionModel().getSelectedItem();
				if (selectedShow == null) {
					showName.setText("");
					djs.setText("");
					djEmail.setText("");
					dateAndTime.setText("");
					addButton.setDisable(false);
					updateButton.setDisable(true);
				} else {
					ArrayList<String> selectedShowData = sqlFile.getShow(selectedShow);
					showName.setText(selectedShowData.get(0));
					djs.setText(selectedShowData.get(1));
					djEmail.setText(selectedShowData.get(2));
					dateAndTime.setText(selectedShowData.get(3));
					addButton.setDisable(true);
					updateButton.setDisable(false);
				}
			}
		});
		closeButton.setOnAction(event -> {
			sqlFile.close();
			try {
				Stage stage = (Stage)closeButton.getScene().getWindow();
			    stage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	@FXML
	public void addShow() {
		sqlFile.addShow(showName.getText(), djs.getText(), djEmail.getText(), dateAndTime.getText());
		showSelector.getItems().add(showName.getText());
	}
	
	@FXML
	public void updateShow() {
		showSelector.getItems().remove(showSelector.getSelectionModel().getSelectedItem());
		sqlFile.updateShow(showName.getText(), djs.getText(), djEmail.getText(), dateAndTime.getText());
		showSelector.getItems().add(showName.getText());
	}
}
