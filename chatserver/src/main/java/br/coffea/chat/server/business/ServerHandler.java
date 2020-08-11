package br.coffea.chat.server.business;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import br.coffea.chat.common.ChatException;
import br.coffea.chat.common.ClientInfo;
import br.coffea.chat.common.rmi.ClientCallback;
import br.coffea.chat.common.rmi.ServerOperations;

public class ServerHandler {

	private static final String CONFIG_FILE = "server_config.txt";
	private static final String PROP_PORT = "port";
	private static final int DEFAULT_PORT = 1909;
	private Registry registry;
	private Properties props;
	private boolean started;
	private ServerOperationsImpl serverOperations;
	
	public ServerHandler() throws ChatException, IOException {
		props = new Properties();
		Path file = Paths.get(CONFIG_FILE);
		
		if (Files.exists(file)) {
			try (InputStream in = Files.newInputStream(file)) {
				props.load(in);
			}
		} else {
			props.setProperty(PROP_PORT, String.valueOf(DEFAULT_PORT));
		}
		
		try {
			registry = LocateRegistry.createRegistry(getServerPort());
		} catch (RemoteException e) {
			throw new ChatException("Error to create registry", e);
		}
	}
	
	public int startServer() throws ChatException {
		try {
			serverOperations = new ServerOperationsImpl();
			registry.bind(ServerOperations.SERVER_OBJ_NAME, serverOperations);
			started = true;
			return getServerPort();
		} catch (Exception e) {
			throw new ChatException("Error starting server");
		}
	}
	
	public void stopServer() throws ChatException {
		try {
			if(!started) {
				return;
			}
			
			Set<ClientInfo> clients = serverOperations.getClients();
			if (clients.size() > 0) {
				ExecutorService execService = Executors.newFixedThreadPool(clients.size());
				try {
					clients.forEach(clientInfo -> {
						
						ClientCallback callback = clientInfo.getCallback();
						execService.execute(new FutureTask<>( () -> {
							callback.onServerShutdown();
							return null;
						}));
						
					});
				} finally {
					execService.shutdown();
				}
			}
			
			registry.unbind(ServerOperations.SERVER_OBJ_NAME);
			
			started = false;
		} catch (Exception e) {
			throw new ChatException("Error stopping server", e);
		}
	}
	
	private int getServerPort() throws ChatException {
		String portStr = props.getProperty(PROP_PORT);
		
		if (portStr == null) {
			throw new ChatException("Server port not defined");
		}
		
		try {
			int port = Integer.parseInt(portStr);
			
			if (port < 1 || port > 65635) {
				throw new ChatException("Server port is not in a valid range");
			}
			return port;
		} catch (NumberFormatException e) {
			throw new ChatException("Server port is not a valid number");
		}
	}
	
}
