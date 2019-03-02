package no.hvl.dat110.process.iface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import no.hvl.dat110.process.Message;

public interface ProcessInterface extends Remote {
	
	public void requestInterest(double interest) throws RemoteException;
	
	public void requestDeposit(double amount) throws RemoteException;
	
	public double getBalance() throws RemoteException;
	
	public int getProcessID() throws RemoteException;
	
	public void multicastOperation(Message message) throws RemoteException;

}
