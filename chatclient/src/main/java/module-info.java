module chatclient {
	
	exports br.coffea.chat.client.ui;
	exports br.coffea.chat.client.config;
	exports br.coffea.chat.client.ui.controller;
	opens br.coffea.chat.client.ui.controller;
	
	
	
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires transitive chatcommon;
	requires java.rmi;
}