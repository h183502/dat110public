package no.hvl.dat110.transport.rdt3;

import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.transport.rdt2.SegmentRDT21;;

public class AdversaryRDT3 extends Adversary {

	private static double CORRUPTPB = 0.2;
	private static double LOSSPB = 0.2;
	
	@Override
	public Datagram process (Datagram datagram) {
		
		double rnd = Math.random();
		
		if (rnd <= CORRUPTPB) { // transmission error
			
			SegmentRDT21 segment = (SegmentRDT21) datagram.getSegment();
			segment.setChecksum(((byte)1)); 
			
			System.out.println("*");
			
		} else if (rnd <= CORRUPTPB + LOSSPB ) { // loss
		
			datagram = null; 
			System.out.println("-");
			
		} else { // success
			
			System.out.println("+");
			
		}
		
		return datagram;
			
	}
}
