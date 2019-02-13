package no.hvl.dat110.application;

import no.hvl.dat110.network.Network;
import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt1.TransportReceiver;
import no.hvl.dat110.transport.rdt1.TransportSender;

public class Main {

	public static void main(String[] args) {

		Network network = new Network(new Adversary());
		network.doRun();
		
		TransportSender tsender = new TransportSender(network.getService(0)); 
		TransportReceiver treceiver = new TransportReceiver(network.getService(1));
		
		tsender.start();
		treceiver.start();
		
		SenderProcess sender = new SenderProcess(tsender);
		ReceiverProcess receiver = new ReceiverProcess(treceiver);
		
		sender.doRun();
		
		try {
			
			Thread.sleep(10000); // allow for reception of outstanding messages
			
			tsender.doStop();
			treceiver.doStop();
			
			network.doStop();
			
		} catch (InterruptedException ex) {

			System.out.println("Main thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
