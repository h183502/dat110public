package no.hvl.dat110.rmiserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import no.hvl.dat110.rmiinterface.ComputeInterface;

/**
 * For demonstration purpose in dat110 course
 */

public class ComputeImpl extends UnicastRemoteObject implements ComputeInterface{
	
	//Export of the ComputeImpl object to the remote server happens using the UnicastRemoteObject
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComputeImpl() throws RemoteException {
		super();
	}

	public int addNumbers(int a, int b) {
		
		System.out.println("Client is blocked...waiting for result");
		
		int sum = a + b;
		
		try {
			Thread.sleep(10000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		return sum;
	}


}
