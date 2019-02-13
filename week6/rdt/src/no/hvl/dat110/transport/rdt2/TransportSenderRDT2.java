package no.hvl.dat110.transport.rdt2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.Segment;
import no.hvl.dat110.transport.rdt1.TransportSender;

public class TransportSenderRDT2 extends TransportSender {

	protected LinkedBlockingQueue<SegmentRDT2> recvqueue;
	private RDT2SenderStates state;

	public TransportSenderRDT2() {
		super();
		recvqueue = new LinkedBlockingQueue<SegmentRDT2>();
		state = RDT2SenderStates.WAITDATA;
	}

	public TransportSenderRDT2(NetworkService ns) {
		super(ns);
		recvqueue = new LinkedBlockingQueue<SegmentRDT2>();
		state = RDT2SenderStates.WAITDATA;
	}
	
	@Override 
	public void rdt_send(byte[] data) {
	
		try {
			outqueue.put(new SegmentRDT2(data));
		} catch (InterruptedException ex) {
			System.out.println("TransportSender thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	@Override
	public void rdt_recv(Segment segment) {

		System.out.println("[Transport:Receiver ] rdt_recv: " + segment.toString());

		try {
			recvqueue.put((SegmentRDT2)segment);
		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
		// do not do anything in the transport sender entity
	}

	private SegmentRDT2 datasegment = null;

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

	private void doWaitData() {
		try {
			datasegment = (SegmentRDT2)outqueue.poll(2, TimeUnit.SECONDS);

			if (datasegment != null) {

				// something to send
				udt_send(datasegment);

				state = RDT2SenderStates.WAITACKNAK;
			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	private void doWaitAckNak() {
		try {

			SegmentRDT2 acksegment = (SegmentRDT2)recvqueue.poll(2, TimeUnit.SECONDS);

			if (acksegment != null) {

				SegmentType type = acksegment.getType();

				if (type == SegmentType.ACK) {

					System.out.println("[Transport:Sender   ] ACK ");
					datasegment = null;
					state = RDT2SenderStates.WAITDATA;
				} else {
					System.out.println("[Transport:Sender   ] NAK ");
					udt_send(datasegment);
				}
			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
