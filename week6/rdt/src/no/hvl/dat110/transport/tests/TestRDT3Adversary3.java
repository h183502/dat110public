package no.hvl.dat110.transport.tests;

import org.junit.Test;

import no.hvl.dat110.transport.rdt2.AdversaryRDT21;
import no.hvl.dat110.transport.rdt2.AdversaryRDT2;
import no.hvl.dat110.transport.rdt3.AdversaryRDT3;
import no.hvl.dat110.transport.rdt2.TransportReceiverRDT21;
import no.hvl.dat110.transport.rdt2.TransportSenderRDT21;

public class TestRDT3Adversary3 {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setupNetwork(new AdversaryRDT3());

		ts.setupTransport(new TransportSenderRDT21(), new TransportReceiverRDT21());

		ts.runTest();

		ts.assertRDT();
	}

}
