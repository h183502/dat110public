package no.hvl.dat110.transport.rdt2;

import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.network.Datagram;

public class AdversaryRDT21 extends Adversary {

	private static double CORRUPTPB = 0.4;
	
	@Override
	public Datagram process (Datagram datagram) {
		
		if (Math.random() < CORRUPTPB) {
			
			SegmentRDT2 segment = (SegmentRDT2) datagram.getSegment();
			segment.setChecksum(((byte)1)); // Now also corrupt ack/naks
			
			System.out.println("-");
			
		} else {
			System.out.println("+");
		}
		
		return datagram;
			
	}
}
