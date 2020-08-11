package br.coffea.chat.client.business;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.coffea.chat.common.ChatException;
import br.coffea.chat.common.ClientInfo;
import br.coffea.chat.common.DuplicateNameException;
import br.coffea.chat.common.rmi.ClientCallback;
import br.coffea.chat.common.rmi.ServerOperations;

public class ServerInvoker {

	private String lookupURL;
	private ServerOperations serverOperations;
	private ClientInfo clientInfo;
	private boolean connected = false;
	
	public ServerInvoker(String server, int port, String name, ServerRequestHandler handler) throws ChatException {
		try {
			lookupURL = "rmi://" + server + ":" + port + "/" + ServerOperations.SERVER_OBJ_NAME;
			
			ClientCallbackImpl callbackImpl = new ClientCallbackImpl(handler, this);
			ClientCallback callback = (ClientCallback) UnicastRemoteObject.toStub(callbackImpl);
			
			clientInfo = new ClientInfo(name, callback);
		} catch (RemoteException e) {
			throw new ChatException("Error to create callback object", e);
		}
	}
	
	public void connect() throws ChatException {
		try {
			serverOperations = (ServerOperations) Naming.lookup(lookupURL);
			serverOperations.connect(clientInfo);
			connected = true;
		} catch (DuplicateNameException e) {
			throw e;
		} catch (Exception e) {
			throw new ChatException("Error connecting to the server", e);
		}
	}
	
	public void disconnect() throws ChatException {
		try {
			if(connected) {
				serverOperations.disconnect(clientInfo);
				connected = false;
			}
		} catch (RemoteException e) {
			throw new ChatException("Error disconnecting to the server", e);
		}
	}
	
	public void sendMessage(String message) throws ChatException {
		try {
			serverOperations.sendMessage(clientInfo, message);
		} catch (RemoteException e) {
			throw new ChatException("Error sending message ", e);
		}
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
