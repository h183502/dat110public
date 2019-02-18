package no.hvl.dat110.transport.tests;

import org.junit.Test;

import no.hvl.dat110.transport.rdt2.AdversaryRDT21;
import no.hvl.dat110.transport.rdt2.TransportReceiverRDT2;
import no.hvl.dat110.transport.rdt2.TransportSenderRDT2;

public class TestRDT21Adversary21 {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setupNetwork(new AdversaryRDT21());

		ts.setupTransport(new TransportSenderRDT2(), new TransportReceiverRDT2());

		ts.runTest();

		ts.assertRDT();
	}

}
