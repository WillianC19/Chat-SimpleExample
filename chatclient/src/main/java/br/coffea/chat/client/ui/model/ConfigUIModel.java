package br.coffea.chat.client.ui.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConfigUIModel {

	private StringProperty server = new SimpleStringProperty();
	private IntegerProperty port = new SimpleIntegerProperty();
	
	public void setServer(String server) {
		this.serverProperty().set(server);
	}
	
	public String getServer() {
		return this.serverProperty().get();
	}
	
	public void setPort(int port) {
		this.portProperty().set(port);
	}
	
	public int getPort() {
		return this.portProperty().get();
	}
	
	public StringProperty serverProperty() {
		return server;
	}
	
	public IntegerProperty portProperty() {
		return port;
	}
}
