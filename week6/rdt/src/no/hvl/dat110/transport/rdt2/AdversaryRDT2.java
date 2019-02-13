package no.hvl.dat110.transport.rdt2;

import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.transport.Segment;

public class AdversaryRDT2 extends Adversary {

	private static double CORRUPTPB = 0.4;
	
	@Override
	public Datagram process (Datagram datagram) {
		
		if (Math.random() < CORRUPTPB) {
			
			SegmentRDT2 segment = (SegmentRDT2) datagram.getSegment();
			segment.setChecksum(((byte)0));
			
			System.out.println("-");
			
		} else {
			System.out.println("+");
		}
		
		return datagram;
			
	}
}
