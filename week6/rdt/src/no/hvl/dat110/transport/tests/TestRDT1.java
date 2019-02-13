package no.hvl.dat110.transport.tests;

import org.junit.Test;

import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.transport.rdt1.TransportReceiverRDT1;
import no.hvl.dat110.transport.rdt1.TransportSenderRDT1;

public class TestRDT1 {

	@Test
	public void test() {
		
		TestTransport ts = new TestTransport();
		
		ts.setupNetwork(new Adversary());
		
		ts.setupTransport(new TransportSenderRDT1(), new TransportReceiverRDT1());
		
		ts.runTest();
				
		ts.assertRDT();
		
	}

}
