package no.hvl.dat110.transport.rdt2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.Segment;
import no.hvl.dat110.transport.SegmentType;
import no.hvl.dat110.transport.TransportSender;

public class TransportSenderRDT2 extends TransportSender {

	protected LinkedBlockingQueue<Segment> recvqueue;
	private RDT2SenderStates state;

	public TransportSenderRDT2() {
		super();
		recvqueue = new LinkedBlockingQueue<Segment>();
		state = RDT2SenderStates.WAITDATA;
	}

	public TransportSenderRDT2(NetworkService ns) {
		super(ns);
		recvqueue = new LinkedBlockingQueue<Segment>();
		state = RDT2SenderStates.WAITDATA;
	}
	
	public void rdt_recv(Segment segment) {

		try {
			recvqueue.put(segment);
		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
		// do not do anything in the transport sender entity
	}

	private Segment datasegment = null;

	public void doProcess() {

		switch (state) {

		case WAITDATA:

			doWaitData();

			break;

		case WAITACKNAK:

			doWaitAckNak();
			
			break;
			
		default:
			break;
		}

	}

	private void doWaitAckNak() {
		try {

			Segment acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

			if (acksegment != null) {

				SegmentType type = acksegment.getType();

				if (type == SegmentType.ACK) {

					System.out.println("[Transport:Sender   ] ACK ");
					datasegment = null;
					state = RDT2SenderStates.WAITDATA;
				} else {
					System.out.println("[Transport:Sender   ] NAK ");
					ns.udt_send(new Datagram(datasegment));
				}
			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void doWaitData() {
		try {
			datasegment = outqueue.poll(2, TimeUnit.SECONDS);

			if (datasegment != null) {

				// something to send
				System.out.println("[Transport:Sender   ] udt_send: " + datasegment.toString());
				ns.udt_send(new Datagram(datasegment));

				state = RDT2SenderStates.WAITACKNAK;
			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
