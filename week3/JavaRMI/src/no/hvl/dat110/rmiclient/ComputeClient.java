package no.hvl.dat110.rmiclient;

/**
 * For demonstration purpose in dat110 course
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import no.hvl.dat110.rmiinterface.ComputeInterface;


public class ComputeClient {
	
	public static void main(String args[]) {

		
		try {
			
			// FromCommand Line: we use this if we want to accept arguments from the command line
			int a = Integer.parseInt(args[0]);
			int b = Integer.parseInt(args[1]);
			
			// From IDE: you can set the variables statically if you want to run it from your IDE
			//int a = 25;
			//int b = 60;
			
			// Get the registry  (you need to specify the ip address/port of the registry if you're running from a different host)
			Registry registry = LocateRegistry.getRegistry();
			
			// Look up the registry for the remote object
			ComputeInterface ci = (ComputeInterface) registry.lookup("ComputeInterface");
			
			int sum = ci.addNumbers(a, b);
			System.out.println("Sum of "+a+" and "+b+" = "+ sum);
		} catch(Exception e) {
			System.err.println("Error in RMI "+e.getMessage());
			e.getStackTrace();
		}
	}
}
