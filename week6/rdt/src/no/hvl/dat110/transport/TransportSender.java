package no.hvl.dat110.transport;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.application.Stopable;

public class TransportSender extends Stopable implements ITransportProtocolEntity {

	protected NetworkService ns;

	protected LinkedBlockingQueue<Segment> outqueue;

	public TransportSender() {
		super("TransportSender");
		outqueue = new LinkedBlockingQueue<Segment>();
	}
	
	public TransportSender(NetworkService ns) {
		this();
		this.ns = ns;
		ns.register(this);
	}

	public void register(NetworkService ns) {
		this.ns = ns;
		ns.register(this);
	}
	
	public final void rdt_send(byte[] data) {

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

	public final void deliver_data(byte[] data) {

		// should never used in the current setting
		throw new RuntimeException("deliver_data called in transport sender");
	}
	
	public final void udt_send(Segment segment) {
		ns.udt_send(new Datagram(segment));
	}
	
	public void doProcess() {

		try {
			Segment segment = outqueue.poll(2, TimeUnit.SECONDS);

			if (segment != null) {
				System.out.println("[Transport:Sender   ] udt_send: " + segment.toString());
				udt_send(segment);
			}

		} catch (InterruptedException ex) {
			System.out.println("Transport sender thread " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
