package no.hvl.dat110.process;

import java.rmi.AccessException;
import java.rmi.NotBoundException;

/**
 * @author tdoy
 * Based on Section 6.2: Distributed Systems - van Steen and Tanenbaum (2017)
 * For demo/teaching purpose at dat110 class
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

import no.hvl.dat110.process.iface.OperationType;
import no.hvl.dat110.process.iface.ProcessInterface;
import no.hvl.dat110.util.Util;

public class Process extends UnicastRemoteObject implements ProcessInterface {

	private static final long serialVersionUID = 1L;

	private int processID;
	private double balance = 1000;					// default balance
	private List<String> replicas;					// list of other processes including self known to this process 

	private List<OperationType> optypes;

	protected Process(int id) throws RemoteException {
		super();
		processID = id;
		replicas = Util.getProcessReplicas();
		optypes = Arrays.asList(OperationType.values());
	}
	
	private void updateDeposit(double amount) throws RemoteException {
		
		balance += amount;
	}
	
	private void updateInterest(double interest) throws RemoteException {
		
		double intvalue = balance*interest;
		balance += intvalue;
	}
	
	// client initiated method
	@Override
	public void requestInterest(double interest) throws RemoteException {
		// 		
		
		Message message = new Message();
		message.setOptype(OperationType.INTEREST);		// set the type of message - deposit or interest
		message.setProcessID(processID);				// set the process ID
		message.setInterest(interest); 					// add interest to calculate as part of message
		
		this.applyOperation(message); 					// process operation
		// multicast message (query) to other processes
		multicastMessage(message);

	}
	
	// client initiated method
	@Override
	public void requestDeposit(double amount) throws RemoteException {
		// 		
		
		Message message = new Message();
		message.setOptype(OperationType.DEPOSIT);		// set the type of message - deposit or interest
		message.setProcessID(processID); 				// set the process ID
		message.setDepositamount(amount); 				// add amount to deposit as part of message
		
		this.applyOperation(message); 					// process operation
		// multicast message (query) to other processes
		multicastMessage(message);

	}
	
	private void applyOperation(Message message) throws RemoteException {
		// if the head of queue is acknowledged, pop it out and process it.

		OperationType optype = message.getOptype();

		switch(optype) {
			case DEPOSIT: {
				this.updateDeposit(message.getDepositamount());
			}
			break;
			case INTEREST: {
				this.updateInterest(message.getInterest());
			}
			break;

			default: break;
		}
			
	}
	
	@Override
	public void multicastOperation(Message message) throws RemoteException {
		
		this.applyOperation(message); 			// apply operation when query is received from another replica

	}
	
	// multicast message to other processes
	private void multicastMessage(Message message) throws AccessException, RemoteException {
		
		for(int i=0; i<replicas.size(); i++) {
			String stub = replicas.get(i);
			try {
				ProcessInterface p = Util.registryHandle(stub);
				if(p.getProcessID() != this.processID)
					p.multicastOperation(message);				
				Thread.sleep(100);
			} catch (NotBoundException | InterruptedException e) {

				e.printStackTrace();
			}
		}
	}
	
	@Override
	public double getBalance() throws RemoteException {
		return balance;
	}
	
	@Override
	public int getProcessID() throws RemoteException {
		return processID;
	}
	
}
