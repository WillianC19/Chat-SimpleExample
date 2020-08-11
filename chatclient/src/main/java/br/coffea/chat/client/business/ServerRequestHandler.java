package br.coffea.chat.client.business;

public interface ServerRequestHandler {

	void onServerShutdown();
	
	void onMessageReceived(String message);
}
