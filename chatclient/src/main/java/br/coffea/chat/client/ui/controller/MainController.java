package br.coffea.chat.client.ui.controller;

import java.io.IOException;

import br.coffea.chat.client.business.ServerInvoker;
import br.coffea.chat.client.business.ServerRequestHandler;
import br.coffea.chat.client.config.ClientConfigFile;
import br.coffea.chat.client.ui.model.MainUIModel;
import br.coffea.chat.common.ChatException;
import br.coffea.chat.common.utils.FXUtils;
import br.coffea.chat.common.utils.StageAwareController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements ServerRequestHandler, StageAwareController {

	public static final String TITLE = "Coffea Enterprise";
	private MainUIModel mainModel = new MainUIModel();
	private ServerInvoker serverInvoker;
	
	@FXML private TextField txtName;
	@FXML private Button btnConnect;
	@FXML private Button btnDisconnect;
	@FXML private TextArea txtMessages;
	@FXML private TextField txtMessage;
	@FXML private Button btnSend;
	
	@FXML
	private void initialize() {
		btnConnect.disableProperty().bind(mainModel.connectedProperty());
		txtName.disableProperty().bind(mainModel.connectedProperty());
		btnDisconnect.disableProperty().bind(mainModel.connectedProperty().not());
		btnSend.disableProperty().bind(mainModel.connectedProperty().not());
		txtMessage.disableProperty().bind(mainModel.connectedProperty().not());
		
		mainModel.nameProperty().bindBidirectional(txtName.textProperty());
		mainModel.messageProperty().bindBidirectional(txtMessage.textProperty());
		mainModel.messagesProperty().bindBidirectional(txtMessages.textProperty());
	}
	
	@FXML public void connect() {
		try {
			String name = mainModel.getName();
			
			if (name != null && !name.trim().isEmpty()) {
				String server = ClientConfigFile.getServer();
				int port = ClientConfigFile.getPort();
				
				serverInvoker = new ServerInvoker(server, port, name, this);
				serverInvoker.connect();
				mainModel.setConnected(true);
				mainModel.setWindowTitle(String.format("%s [Connected in %s:%s]", TITLE, ClientConfigFile.getServer(), ClientConfigFile.getPort()));
				txtMessage.requestFocus();
			} else {
				txtName.requestFocus();
			}
		} catch (Exception e) {
			FXUtils.showExceptionDialog(e);
		}
	}
	
	@FXML public void disconnect() {
		try {
			if (serverInvoker != null) {
				serverInvoker.disconnect();
			}
			
			mainModel.setConnected(false);
			mainModel.setName("");
			mainModel.setMessage("");
			mainModel.setWindowTitle(TITLE);
			txtName.requestFocus();
		} catch (ChatException e) {
			FXUtils.showExceptionDialog(e);
		}
	}

	@FXML public void sendMessage() {
		try {
			String message = mainModel.getMessage();
			if (message != null && !message.trim().isEmpty()) {
				serverInvoker.sendMessage(message);
				mainModel.setMessage("");
				txtMessage.requestFocus();
			} else {
				txtMessage.requestFocus();
			}
		} catch (ChatException e) {
			FXUtils.showExceptionDialog(e);
		}
	}
	
	@Override
	public void onStageDefined(Stage stage) {
		stage.titleProperty().bind(mainModel.windowTitleProperty());
		mainModel.setWindowTitle(TITLE);
		stage.setOnCloseRequest(event -> exit());
	}
	
	@FXML public void exit() {
		if (serverInvoker != null) {
			try {
				serverInvoker.disconnect();
			} catch(Exception e) {
			}
		}
		Platform.exit();
		System.exit(0);
	}
	
	@Override
	public void onServerShutdown() {
		Platform.runLater(() -> {
			serverInvoker = null;
			disconnect();
			FXUtils.showErrorDialog("The connection to the server has been terminated");
		});
	}

	@Override
	public void onMessageReceived(String message) {
		mainModel.appendMessage(message);
		txtMessages.positionCaret(txtMessages.getText().length());
	}
	
	@FXML public void openPreferencesWindow() throws IOException {
		Stage configStage = new Stage();
		configStage.setTitle("Preferences");
		configStage.initModality(Modality.APPLICATION_MODAL);
		configStage.setResizable(false);
		
		FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/Config.fxml"));
		Pane root = loader.load();
		FXUtils.initLayout(loader, configStage);
		
		Scene scene = new Scene(root, 300, 120);
		configStage.setScene(scene);
		configStage.show();
	}
}
