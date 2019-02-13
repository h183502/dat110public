package no.hvl.dat110.transport.rdt1;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.transport.*;
import no.hvl.dat110.network.Datagram;

public class TransportSenderRDT1 extends TransportSender implements ITransportProtocolEntity {

	protected LinkedBlockingQueue<Segment> outqueue;

	public TransportSenderRDT1() {
		super("TransportSender");
		outqueue = new LinkedBlockingQueue<Segment>();
	}
		
	public void rdt_send(byte[] data) {

		try {
			outqueue.put(new Segment(data));
		} catch (InterruptedException ex) {
			System.out.println("TransportSender thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void rdt_recv(Segment segment) {

		// do not do anything in the basic transport sender entity
	}

	public void doProcess() {

		try {
			Segment segment = outqueue.poll(2, TimeUnit.SECONDS);

			if (segment != null) {
				udt_send(segment);
			}

		} catch (InterruptedException ex) {
			System.out.println("Transport sender thread " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
