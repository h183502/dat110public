package no.hvl.dat110.rmiinterface;

/**
 * For demonstration purpose in dat110 course
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComputeInterface extends Remote {
	
	public int addNumbers(int a, int b) throws RemoteException;

}
