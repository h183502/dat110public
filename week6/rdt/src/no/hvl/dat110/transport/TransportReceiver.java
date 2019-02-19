package no.hvl.dat110.transport;

import no.hvl.dat110.application.ReceiverProcess;
import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.NetworkService;

public abstract class TransportReceiver extends Stopable implements ITransportProtocolEntity {

	private ReceiverProcess receiver;
	private NetworkService ns;
	
	public TransportReceiver(String name) {
		super(name);
	}
	
	public void register(NetworkService ns) {
		ns.register(this);
		this.ns = ns;
	}
	
	public void register(ReceiverProcess receiver) {
		this.receiver = receiver;
	}

	public final void rdt_send(byte[] data) {

		// should never used in the current setting
		throw new RuntimeException("rdt_send called in transport receiver");
	}
	
	// udt_send should always just deliver the data to the receiver
	public final void deliver_data(byte[] data) {
		receiver.deliver_data(data);
	}

	// udt_send should always just send the segment via the underlying network service
	public final void udt_send(Segment segment) {
		System.out.println("[Transport:Receiver ] udt_send: " + segment.toString());
		ns.udt_send(new Datagram(segment));
	}
	
	// TODO: consider adding the recv segment queue into the base class
}
