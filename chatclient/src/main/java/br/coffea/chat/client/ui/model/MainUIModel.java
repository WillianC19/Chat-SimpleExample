package br.coffea.chat.client.ui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MainUIModel {

	private BooleanProperty connected = new SimpleBooleanProperty();
	private StringProperty name = new SimpleStringProperty();
	private StringProperty message = new SimpleStringProperty();
	private StringProperty messages = new SimpleStringProperty();
	private StringProperty windowTitle = new SimpleStringProperty();
	
	public boolean isConnected() {
		return connectedProperty().get();
	}
	
	public void setConnected(boolean connected) {
		this.connectedProperty().set(connected);
	}
	
	public String getName() {
		return nameProperty().get();
	}
	
	public void setName(String name) {
		this.nameProperty().set(name);
	}
	
	public String getMessage() {
		return messageProperty().get();
	}
	
	public void setMessage(String message) {
		this.messageProperty().set(message);
	}
	
	public String getMessages() {
		return messagesProperty().get();
	}
	
	public void setMessages(String messages) {
		this.messagesProperty().set(messages);
	}
	
	public String getWindowTitle() {
		return windowTitleProperty().get();
	}
	
	public void setWindowTitle(String windowTitle) {
		this.windowTitleProperty().set(windowTitle);
	}
	
	public BooleanProperty connectedProperty() {
		return connected;
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public StringProperty messageProperty() {
		return message;
	}
	
	public StringProperty messagesProperty() {
		return messages;
	}
	
	public StringProperty windowTitleProperty() {
		return windowTitle;
	}
	
	public void appendMessage(String message) {
		setMessages(getMessages() + message + "\n");
	}
	
}
