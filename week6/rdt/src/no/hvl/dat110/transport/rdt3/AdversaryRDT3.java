package no.hvl.dat110.transport.rdt3;

import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.transport.rdt2.SegmentRDT21;;

public class AdversaryRDT3 extends Adversary {

	private static double CORRUPTPB = 0.3;
	private static double LOSSPB = 0.3;
	
	@Override
	public Datagram process (Datagram datagram) {
		
		double rnd = Math.random();
		
		if (rnd <= CORRUPTPB) {
			
			SegmentRDT21 segment = (SegmentRDT21) datagram.getSegment();
			segment.setChecksum(((byte)1)); // Now also corrupt ack/naks
			
			assert (segment.isCorrect() == false);
			System.out.println("*");
			
		} else if (rnd <= CORRUPTPB + LOSSPB ) {
		
			datagram = null; // loss
			System.out.println("-");
			
		} else {
			
			System.out.println("+");
			assert ((SegmentRDT21)datagram.getSegment()).isCorrect();
			
		}
		
		return datagram;
			
	}
}
