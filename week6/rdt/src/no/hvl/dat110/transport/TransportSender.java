package no.hvl.dat110.transport;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.NetworkService;

public abstract class TransportSender extends Stopable implements ITransportProtocolEntity {

	private NetworkService ns;
	
	public TransportSender(String name) {
		super(name);
	}

	public void register(NetworkService ns) {
		this.ns = ns;
		ns.register(this);
	}
	
	public final void deliver_data(byte[] data) {

		// should never be called in the current setting
		throw new RuntimeException("deliver_data called in transport sender");
	}
	
	// udt_send should always just send the segment via the underlying network service
	public final void udt_send(Segment segment) {
		System.out.println("[Transport:Sender   ] udt_send: " + segment.toString());
		ns.udt_send(new Datagram(segment));
	}
	
	// TODO: consider adding the indataqueue into the base class
}
