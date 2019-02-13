package no.hvl.dat110.transport.tests;

import org.junit.Test;

import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.transport.rdt1.TransportReceiver;
import no.hvl.dat110.transport.rdt1.TransportSender;

public class TestRDT1 {

	@Test
	public void test() {
		
		TestTransport ts = new TestTransport();
		
		ts.setupNetwork(new Adversary());
		
		ts.setupTransport(new TransportSender(), new TransportReceiver());
		
		ts.runTest();
				
		ts.assertRDT();
		
	}

}
