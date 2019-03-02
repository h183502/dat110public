package no.hvl.dat110.process;

/**
 * @author tdoy
 * Based on Section 6.2: Distributed Systems - van Steen and Tanenbaum (2017)
 * For demo/teaching purpose at dat110 class
 */

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.hvl.dat110.process.iface.OperationType;
import no.hvl.dat110.process.iface.ProcessInterface;
import no.hvl.dat110.util.Util;

public class Process extends UnicastRemoteObject implements ProcessInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Message> queue;					// queue for this process
	private int processID;
	private int counter;
	private double balance = 1000;					// default balance
	private List<String> replicas;					// list of other processes including self known to this process 

	protected Process(int id) throws RemoteException {
		super();
		processID = id;
		counter = 0;
		queue = Collections.synchronizedList(new ArrayList<Message>());	
		replicas = Util.getProcessReplicas();
	}
	
	private void updateDeposit(double amount) throws RemoteException {
		incrementclock();
		balance += amount;
	}
	
	private void updateInterest(double interest) throws RemoteException {
		incrementclock();
		double intvalue = balance*interest;
		balance += intvalue;
	}
	
	private void updateWithdrawal(double amount) throws RemoteException {
		incrementclock();
		balance -= amount;
	}
	
	private void incrementclock() {
		counter++;
	}
	
	// sort queue first on the counter and then the processID to break any clock tie
	private void sortQueue() {
		// TODO 
	}
	
	// client initiated method
	@Override
	public void requestInterest(double interest) throws RemoteException {
		// 		
		incrementclock();
		Message message = new Message();
		message.setOptype(OperationType.INTEREST);		// set the type of message - deposit or interest
		message.setClock(counter);						// set the timestamp of message
		message.setProcessID(processID);				// set the process ID
		message.setInterest(interest); 					// add interest to calculate as part of message
		
		queue.add(message); 							// add to queue
		sortQueue();									// sort the queue according to timestamp and processID

		// multicast clock + message to other processes
		multicastMessage(message);

	}
	
	// client initiated method
	@Override
	public void requestDeposit(double amount) throws RemoteException {
		// 		
		incrementclock();
		Message message = new Message();
		message.setOptype(OperationType.DEPOSIT);		// set the type of message - deposit or interest
		message.setClock(counter);						// set the timestamp of message
		message.setProcessID(processID); 				// set the process ID
		message.setDepositamount(amount); 				// add amount to deposit as part of message
		
		queue.add(message); 							// add to queue
		sortQueue();									// sort the queue according to timestamp and processID
		
		// multicast clock + message to other processes
		multicastMessage(message);

	}
	
	// client initiated method
	@Override
	public void requestWithdrawal(double amount) throws RemoteException {
		// TODO

	}
	
	@Override
	public void sendAcknowledgement(Message message) throws RemoteException {
		
		int index = exist(message);
		if(index != -1) {
			queue.get(index).setAcknowledged(true);	
		}				
	}
	
	public void applyOperation() throws RemoteException {
		
		sortQueue();		// sort.
		for(int i=0; i<queue.size(); i++) {

			Message message = queue.get(i);
			//boolean ack = message.isAcknowledged();

			OperationType optype = message.getOptype();

			switch(optype) {
			
				case DEPOSIT: 
					this.updateDeposit(message.getDepositamount());
					break;
					
				case INTEREST: 
					this.updateInterest(message.getInterest());
					break;
					
				case WITHDRAWAL: 
					// TODO
					break;
					
				default: break;
			}
		}
		
	}
	
	@Override
	public void multicastOperation(Message message) throws RemoteException {
		
		if(exist(message) == -1)
			queue.add(message);							// upon receipt of a message, add clock to queue	
		sortQueue();									// sort the queue according to timestamp and processID
		
		int sndclock = message.getClock();				// check the clock of the sending process
		
		counter = Math.max(counter, sndclock);			// get the clock that is higher and update the local clock
		
		incrementclock();								// increment the local clock
		
		// multicast acknowledgement to other processes including self
		// TODO - use implementation idea from multicastMessage() method
		multicastAcknowledgement(message);
	}
	
	private void multicastAcknowledgement(Message message) {
		// TODO - use implementation from multicastMessage() as inspiration
	}
	
	// multicast message to other processes including self
	private void multicastMessage(Message message) throws AccessException, RemoteException {
		
		for(int i=0; i<replicas.size(); i++) {
			String stub = replicas.get(i);
			try {
				ProcessInterface p = Util.registryHandle(stub);
				
				p.multicastOperation(message);				
				Thread.sleep(100);
			} catch (NotBoundException | InterruptedException e) {

				e.printStackTrace();
			}
		}
	}
	
	private int exist(Message message) {
		for(int i=0; i<queue.size(); i++) {
			Message c = queue.get(i);
			if(message.getOptype()==c.getOptype() && message.getClock()==c.getClock() && message.getProcessID()
					== c.getProcessID())
				return i;
		}
		
		return -1;
	}
	
	@Override
	public double getBalance() throws RemoteException {
		return balance;
	}
	
	@Override
	public int getProcessID() throws RemoteException {
		return processID;
	}
	
	@Override
	public List<Message> getQueue() throws RemoteException {
		return queue;
	}

	@Override
	public void doSomething() throws RemoteException {
		// to simulate new events
		try {
			Thread.sleep(500);
			incrementclock();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	
}
