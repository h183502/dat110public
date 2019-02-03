package no.hvl.dat110.rmiclient;

/**
 * For demonstration purpose in dat110 course
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

import no.hvl.dat110.rmiinterface.ComputeInterface;


public class ComputeClient {
	
	public static void main(String args[]) {

		
		try {
			
			Random r = new Random();
			int a = r.nextInt(500);
			int b = r.nextInt(200);
			
			
			// Get the registry  (you need to specify the ip address/port of the registry if you're running from a different host)
			Registry registry = LocateRegistry.getRegistry(9091);
			
			// Look up the registry for the remote object
			ComputeInterface ci = (ComputeInterface) registry.lookup("ComputeInterface");
			
			int sum = ci.addNumbers(a, b);
			System.out.println("Result is ready...");
			Thread.sleep(1000);
			System.out.println("Sum of "+a+" and "+b+" = "+ sum);

			
		} catch(Exception e) {
			System.err.println("Error in RMI "+e.getMessage());
			e.getStackTrace();
		}
	}
}
