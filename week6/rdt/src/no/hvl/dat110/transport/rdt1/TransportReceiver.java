package no.hvl.dat110.transport.rdt1;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.ITransportProtocolEntity;
import no.hvl.dat110.transport.Segment;
import no.hvl.dat110.transport.Stopable;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.application.*;

public class TransportReceiver extends Stopable implements ITransportProtocolEntity {

	protected LinkedBlockingQueue<Segment> inqueue;
	protected ReceiverProcess receiver;
	protected NetworkService ns;
	
	public TransportReceiver() {
		super("TransportReceiver");
		inqueue = new LinkedBlockingQueue<Segment>();
	}
	
	public TransportReceiver(NetworkService ns) {
		this();
		ns.register(this);
		this.ns = ns;
	}

	public void register(NetworkService ns) {
		ns.register(this);
		this.ns = ns;
	}
	
	public void register(ReceiverProcess receiver) {
		this.receiver = receiver;
	}

	public final void deliver_data(byte[] data) {
		receiver.deliver_data(data);
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

	public void udt_send(Segment data) {
		
		// do nothing in the basic transport receiver
	}
	
	public final void rdt_send(byte[] data) {

		// should never used in the current setting
		throw new RuntimeException("rdt_send called in transport receiver");
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
