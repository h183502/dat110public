package no.hvl.dat110.rmiserver;

/**
 * For demonstration purpose in dat110 course
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import no.hvl.dat110.rmiinterface.ComputeInterface;


public class ComputeServer {
	
	public static void main(String args[]) {
		
		try {
			// create registry and start it on port 9091
			Registry registry = LocateRegistry.createRegistry(9091);
			
			// Make a new instance of the implementation class
			ComputeInterface comp1 = new ComputeImpl();
			
			// bind the remote object (stub) in the registry			
			registry.bind("ComputeInterface", comp1);
			
			System.out.println("ComputeServer is ready");
		}catch(Exception e) {
			System.err.println("ComputeServer: "+e.getMessage());
			e.printStackTrace();
		}
	}

}
