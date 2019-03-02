package no.hvl.dat110.clients;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import no.hvl.dat110.process.Config;
import no.hvl.dat110.process.iface.ProcessInterface;
import no.hvl.dat110.util.Util;

public class Process3 extends Thread {
	
	private double finalbalance = -3;
	
	public Process3() {
		
	}
	
	public void run() {
		try { 
			// Get the registry  - running on local machine's IP
			Registry registry = LocateRegistry.getRegistry(Config.PORT);
			// Look up the registry for the remote object
			ProcessInterface p3 = (ProcessInterface) registry.lookup("process3");				

			System.out.println(p3.getProcessID()+": Initial Balance "+p3.getBalance());
			//p3.requestWithdrawal(200);
			
			p3.requestInterest(0.01);
			p3.requestDeposit(100);
			
			Thread.sleep(3000);
			
			Util.printClock(p3);
			
			this.setFinalbalance(p3.getBalance());
			
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
