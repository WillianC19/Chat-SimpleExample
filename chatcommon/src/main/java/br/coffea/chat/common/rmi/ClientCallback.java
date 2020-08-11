package br.coffea.chat.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {

	public void onIncommingMessage(String message) throws RemoteException;
	
	public void onServerShutdown() throws RemoteException;
	
}
