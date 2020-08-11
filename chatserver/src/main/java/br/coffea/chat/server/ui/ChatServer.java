package br.coffea.chat.server.ui;

import br.coffea.chat.common.utils.FXUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ChatServer extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Server Coffea Enterprise");
		primaryStage.setResizable(false);
		
		FXMLLoader loader = new FXMLLoader(ChatServer.class.getResource("/ServerWindow.fxml"));
		Pane root = loader.load();
		Scene scene = new Scene(root, 300, 100);
		
		FXUtils.initLayout(loader, primaryStage);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
