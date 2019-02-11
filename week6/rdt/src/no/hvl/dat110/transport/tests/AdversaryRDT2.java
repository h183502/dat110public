package no.hvl.dat110.transport.tests;

import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.transport.Segment;

public class AdversaryRDT2 extends Adversary {

	private static double CORRUPTPB = 0.4;
	
	@Override
	public Datagram process (Datagram datagram) {
		
		if (Math.random() < CORRUPTPB) {
			
			System.out.println("-");
			Segment segment = datagram.getSegment();
			
			segment.setChecksum(((byte)0));
			
		} else {
			System.out.println("+");
		}
		
		return datagram;
			
	}
}
