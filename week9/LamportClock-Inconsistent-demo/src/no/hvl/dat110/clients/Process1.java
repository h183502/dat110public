package no.hvl.dat110.clients;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import no.hvl.dat110.process.Config;
import no.hvl.dat110.process.iface.ProcessInterface;
import no.hvl.dat110.util.Util;

public class Process1 extends Thread {
	
	private double finalbalance = -1;
	
	public Process1() {
		
	}
	
	public void run() {
		try { 
			// Get the registry  - running on local machine's IP
			Registry registry = LocateRegistry.getRegistry(Config.PORT);
			// Look up the registry for the remote object
			ProcessInterface p1 = (ProcessInterface) registry.lookup("process1");				
			
			System.out.println(p1.getProcessID()+": Initial Balance "+p1.getBalance());
			
			p1.requestDeposit(100);
			
			p1.requestInterest(0.01);
			
			//p1.requestWithdrawal(200);
			
			Thread.sleep(1000);
			
			Util.printClock(p1);
			
			this.setFinalbalance(p1.getBalance());
			
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
