package me.jacobturner.castfast.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.jacobturner.castfast.CastFastOptions;

public class CastFastOptionsController {
	@FXML
	private TextField stationName;
	@FXML
	private TextField accessKey;
	@FXML
	private PasswordField secretKey;
	@FXML
	private TextField bucketName;
	@FXML
	private TextField smtpServer;
	@FXML
	private TextField portNumber;
	@FXML
	private TextField emailAddress;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private CheckBox useSSLTLS;
	@FXML
	private Button saveButton;
	
	public void initialize() {
		CastFastOptions options = new CastFastOptions();
		stationName.setText(options.getValue("station_name"));
		accessKey.setText(options.getValue("access_key"));
		secretKey.setText(options.getValue("secret_key"));
		bucketName.setText(options.getValue("bucket_name"));
		smtpServer.setText(options.getValue("smtp_server"));
		portNumber.setText(options.getValue("port"));
		emailAddress.setText(options.getValue("email_address"));
		username.setText(options.getValue("username"));
		password.setText(options.getValue("password"));
		useSSLTLS.setSelected(Boolean.valueOf(options.getValue("use_ssl_tls")));
		saveButton.setOnAction(event -> {
			options.setValue("station_name", stationName.getText());
			options.setValue("access_key", accessKey.getText());
			options.setValue("secret_key", secretKey.getText());
			options.setValue("bucket_name", bucketName.getText());
			options.setValue("smtp_server", smtpServer.getText());
			options.setValue("port", portNumber.getText());
			options.setValue("email_address", emailAddress.getText());
			options.setValue("username", username.getText());
			options.setValue("password", password.getText());
			options.setValue("use_ssl_tls", useSSLTLS.selectedProperty().getValue().toString());
			options.close();
			try {
				Stage stage = (Stage)saveButton.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
