package no.hvl.dat110.transport.tests;

import org.junit.Test;

import no.hvl.dat110.transport.rdt2.AdversaryRDT21;
import no.hvl.dat110.transport.rdt2.AdversaryRDT2;
import no.hvl.dat110.transport.rdt2.TransportReceiverRDT21;
import no.hvl.dat110.transport.rdt2.TransportSenderRDT21;

public class TestRDT21Adversary21 {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setupNetwork(new AdversaryRDT21());

		ts.setupTransport(new TransportSenderRDT21(), new TransportReceiverRDT21());

		ts.runTest();

		ts.assertRDT();
	}

}
