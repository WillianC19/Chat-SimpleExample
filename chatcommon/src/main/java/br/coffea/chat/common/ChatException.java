package br.coffea.chat.common;

public class ChatException extends Exception {

	private static final long serialVersionUID = 1L;

	public ChatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ChatException(String message) {
		super(message);
	}
	
}
