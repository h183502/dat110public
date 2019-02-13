package no.hvl.dat110.transport.rdt2;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.transport.Segment;
import no.hvl.dat110.transport.rdt1.TransportReceiver;

public class TransportReceiverRDT2 extends TransportReceiver {

	private RDT2ReceiverStates state;

	public TransportReceiverRDT2() {
		super();
		state = RDT2ReceiverStates.WAITING;
	}
	
	public TransportReceiverRDT2(NetworkService ns) {
		super(ns);
		state = RDT2ReceiverStates.WAITING;
	}

	@Override
	public void udt_send(Segment segment) {
		System.out.println("[Transport:Sender   ] udt_send: " + segment.toString());
		ns.udt_send(new Datagram(segment));
	}

	public void doProcess() {

		SegmentRDT2 segment = null;

		switch (state) {

		case WAITING:

			try {

				segment = (SegmentRDT2)inqueue.poll(2, TimeUnit.SECONDS);

			} catch (InterruptedException ex) {
				System.out.println("TransportReceiver RDT2 - doProcess " + ex.getMessage());
				ex.printStackTrace();
			}
			
			if (segment != null) {

				Segment acksegment;

				if (segment.isCorrect()) {

					// deliver data to the transport layer
					deliver_data(segment.getData());

					// send an ack to the sender
					acksegment = new SegmentRDT2(SegmentType.ACK);
					
				} else {
					// send an ack to the sender
					acksegment = new SegmentRDT2(SegmentType.NAK);
				}

				udt_send(acksegment);
			}

			break;
		default:
			break;
		}
	}
}
