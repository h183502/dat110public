package no.hvl.dat110.clients;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import no.hvl.dat110.process.Config;
import no.hvl.dat110.process.iface.ProcessInterface;
import no.hvl.dat110.util.Util;

public class Process2 extends Thread {
	
	private double finalbalance = -2;
	
	public Process2() {
		
	}
	
	public void run() {
		try { 
			// Get the registry  - running on local machine's IP
			Registry registry = LocateRegistry.getRegistry(Config.PORT);
			// Look up the registry for the remote object
			ProcessInterface p2 = (ProcessInterface) registry.lookup("process2");				
			
			System.out.println(p2.getProcessID()+": Initial Balance "+p2.getBalance());
			p2.requestInterest(0.01);
			
			//p2.requestWithdrawal(200);

			p2.requestDeposit(100);
			
			Thread.sleep(2000);
			
			Util.printClock(p2);
			
			this.setFinalbalance(p2.getBalance());
			
		 }catch (RemoteException | NotBoundException | InterruptedException e) { 
			 e.printStackTrace(); 
		 }
	}
	
	public double getFinalbalance() {
		return finalbalance;
	}

	public void setFinalbalance(double finalbalance) {
		this.finalbalance = finalbalance;
	}

}
