package no.hvl.dat110.transport.tests;

import org.junit.Test;

import no.hvl.dat110.transport.rdt4.AdversaryRDT4;
import no.hvl.dat110.transport.rdt4.TransportSenderRDT4;
import no.hvl.dat110.transport.rdt4.TransportReceiverRDT4;
import no.hvl.dat110.transport.tests.TestTransport;

public class TestRDT4Adversary4 {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setRunningTime(30000);

		ts.setupNetwork(new AdversaryRDT4());

		ts.setupTransport(new TransportSenderRDT4(), new TransportReceiverRDT4());

		ts.runTest();

		ts.assertRDT();
	}

}
