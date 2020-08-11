package br.coffea.chat.server.ui.controller;

import br.coffea.chat.common.ChatException;
import br.coffea.chat.common.utils.FXUtils;
import br.coffea.chat.common.utils.StageAwareController;
import br.coffea.chat.server.business.ServerHandler;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ServerController implements StageAwareController {

	@FXML
	Button btnStart;
	@FXML
	Button btnStop;
	@FXML
	Label lblStatus;

	private ServerHandler serverHandler;
	private BooleanProperty serverStarted = new SimpleBooleanProperty();

	@FXML
	private void initialize() {
		btnStart.disableProperty().bind(serverStarted);
		btnStop.disableProperty().bind(serverStarted.not());
		
		try {
			serverHandler = new ServerHandler();
		} catch (Exception e) {
			FXUtils.showExceptionDialog(e);
		}
	}
	
	@FXML
	public void onStart() {
		try {
			int port = serverHandler.startServer();
			lblStatus.setText("Server started on port: " + port);
			serverStarted.set(true);
		} catch (ChatException e) {
			FXUtils.showExceptionDialog(e);
		}
		
	}

	@FXML
	public void onStop() {
		try {
			serverHandler.stopServer();
			lblStatus.setText("Server stopped");
			serverStarted.set(false);
		} catch (ChatException e) {
			FXUtils.showExceptionDialog(e);
		}
	}

	@Override
	public void onStageDefined(Stage stage) {
		stage.setOnCloseRequest(event -> {
			if (serverHandler != null) {
				try {
					serverHandler.stopServer();
				} catch (Exception e) {
				}
			}
			
			Platform.exit();
			System.exit(0);
		});
	}

}
