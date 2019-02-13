package no.hvl.dat110.transport.rdt1;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TransportReceiverRDT1 extends TransportReceiver implements ITransportProtocolEntity {

	private LinkedBlockingQueue<Segment> inqueue;

	public TransportReceiverRDT1() {
		super("TransportReceiver");
		inqueue = new LinkedBlockingQueue<Segment>();
		
	}
	
	// network service will call this method when segments arrive
	public final void rdt_recv(Segment segment) {

		System.out.println("[Transport:Receiver ] rdt_recv: " + segment.toString());

		try {
			inqueue.put(segment);
		} catch (InterruptedException ex) {

			System.out.println("Transport receiver  " + ex.getMessage());
			ex.printStackTrace();
		}

	}

	

	public void doProcess() {

		try {
			Segment segment = inqueue.poll(2, TimeUnit.SECONDS);

			if (segment != null) {
				deliver_data(segment.getData());
			}
		} catch (InterruptedException ex) {
			System.out.println("Transport receiver - deliver data " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
