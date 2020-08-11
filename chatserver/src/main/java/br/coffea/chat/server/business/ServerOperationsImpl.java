package br.coffea.chat.server.business;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

import br.coffea.chat.common.ClientInfo;
import br.coffea.chat.common.DuplicateNameException;
import br.coffea.chat.common.rmi.ClientCallback;
import br.coffea.chat.common.rmi.ServerOperations;

public class ServerOperationsImpl extends UnicastRemoteObject implements ServerOperations {

	private static final long serialVersionUID = 1L;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private Set<ClientInfo> clients = new HashSet<>();
	private ReentrantLock lock = new ReentrantLock(); 
	
	protected ServerOperationsImpl() throws RemoteException {}

	@Override
	public void connect(ClientInfo clientInfo) throws RemoteException, DuplicateNameException {
		boolean added;
		lock.lock();
		try {
			added = clients.add(clientInfo);
		} finally {
			lock.unlock();
		}
		
		if (!added) {
			throw new DuplicateNameException("The name " + clientInfo.getName() + " already exists in the chat");
		}
		
		String message = clientInfo.getName() + " joined in chat";
		String formattedMessage = formatMessageFromServerToClients(message);
		broadcastMessage(formattedMessage);
	}

	@Override
	public void disconnect(ClientInfo clientInfo) throws RemoteException {
		lock.lock();
		try {
			clients.remove(clientInfo);
		} finally {
			lock.unlock();
		}
		
		String message = clientInfo.getName()  + " left the chat";
		String formattedMessage = formatMessageFromServerToClients(message);
		broadcastMessage(formattedMessage);
		
	}

	@Override
	public void sendMessage(ClientInfo clientInfo, String message) throws RemoteException {
		String formattedMessage = formatMessageFromClientToClients(message, clientInfo.getName());
		broadcastMessage(formattedMessage);
	}
	
	private String formatMessageFromServerToClients(String message) {
		String formattedMessage = String.format("(%s) %s", sdf.format(new Date()), message);
		return formattedMessage;
	}
	
	private String formatMessageFromClientToClients(String message, String clientName) {
		String formattedMessage = String.format("(%s) [%s] %s", sdf.format(new Date()), clientName, message);
		return formattedMessage;
	}
	
	public void broadcastMessage(String message) {
		lock.lock();
		try {
			if (clients.size() > 0) {
				
				ExecutorService execService = Executors.newFixedThreadPool(clients.size());
				try {
					clients.forEach(clientInfo -> {
						final ClientCallback callback = clientInfo.getCallback();
						execService.execute(new FutureTask<>( () -> {
							callback.onIncommingMessage(message);
							return null;
						}));
						
					});
				} finally {
					execService.shutdown();
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	public Set<ClientInfo> getClients() {
		lock.lock();
		try {
			return clients;
		} finally {
			lock.unlock();
		}
	}
	
}
