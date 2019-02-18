package no.hvl.dat110.transport.rdt2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.transport.*;

public class TransportReceiverRDT21 extends TransportReceiver implements ITransportProtocolEntity {

	public enum RDT2ReceiverStates {
		WAITING;
	}
	
	private RDT2ReceiverStates state;
	
	private LinkedBlockingQueue<SegmentRDT2> insegqueue;

	public TransportReceiverRDT21() {
		super("TransportReceiver");
		state = RDT2ReceiverStates.WAITING;
		insegqueue = new LinkedBlockingQueue<SegmentRDT2>();
	}
	
	// network service will call this method when segments arrive
	public void rdt_recv(Segment segment) {

		System.out.println("[Transport:Receiver ] rdt_recv: " + segment.toString());

		try {
			
			insegqueue.put((SegmentRDT2)segment);
			
		} catch (InterruptedException ex) {

			System.out.println("Transport receiver  " + ex.getMessage());
			ex.printStackTrace();
		}

	}
	
	public void doProcess() {

		SegmentRDT2 segment = null;

		switch (state) {

		case WAITING:

			try {

				segment = insegqueue.poll(2, TimeUnit.SECONDS);

			} catch (InterruptedException ex) {
				System.out.println("TransportReceiver RDT2 - doProcess " + ex.getMessage());
				ex.printStackTrace();
			}
			
			if (segment != null) {

				SegmentType acktype = SegmentType.NAK;

				if (segment.isCorrect()) {

					// deliver data to the transport layer
					deliver_data(segment.getData());

					// send an ack to the sender
					acktype = SegmentType.ACK;
					
				} 
				
				udt_send(new SegmentRDT2(acktype));
			}

			break;
		default:
			break;
		}
	}
}
