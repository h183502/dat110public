package no.hvl.dat110.rmiserver;

/**
 * For demonstration purpose in dat110 course
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import no.hvl.dat110.rmiinterface.ComputeInterface;


public class ComputeServer {
	
	public static void main(String args[]) {

		
		try {
			// Make a new instance of the implementation class
			ComputeImpl comp1 = new ComputeImpl();
			
			//Export the object of implementation class (remote object)
			ComputeInterface stub = (ComputeInterface) UnicastRemoteObject.exportObject(comp1, 0);
			
			// bind the remote object (stub) in the registry
			Registry registry = LocateRegistry.getRegistry();
			
			registry.bind("ComputeInterface", stub);
			
			System.out.println("ComputeServer is ready");
		}catch(Exception e) {
			System.err.println("ComputeServer: "+e.getMessage());
			e.printStackTrace();
		}
	}

}
