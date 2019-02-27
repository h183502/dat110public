package no.hvl.dat110.transport.tests;

import org.junit.Test;

import no.hvl.dat110.transport.rdt3.AdversaryRDT3;
import no.hvl.dat110.transport.rdt3.TransportSenderRDT3;
import no.hvl.dat110.transport.rdt3.TransportReceiverRDT3;

public class TestRDT3Adversary3 {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setRunningTime(30000);
		
		ts.setupNetwork(new AdversaryRDT3());

		ts.setupTransport(new TransportSenderRDT3(), new TransportReceiverRDT3());

		ts.runTest();

		ts.assertRDT();
	}

}
