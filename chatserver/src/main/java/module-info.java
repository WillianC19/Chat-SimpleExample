module chatserver {
	exports br.coffea.chat.server.ui;
	exports br.coffea.chat.server.business;
	exports br.coffea.chat.server.ui.controller;
	opens br.coffea.chat.server.ui.controller;
	
	requires transitive chatcommon;
	requires java.rmi;
	requires javafx.base;
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
}