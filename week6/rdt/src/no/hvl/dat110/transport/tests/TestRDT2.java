package no.hvl.dat110.transport.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import no.hvl.dat110.network.Adversary;
import no.hvl.dat110.transport.rdt2.TransportReceiverRDT2;
import no.hvl.dat110.transport.rdt2.TransportSenderRDT2;

public class TestRDT2 {

	@Test
	public void test() {
		TestTransport ts = new TestTransport();

		ts.setupNetwork(new Adversary());

		ts.setupTransport(new TransportSenderRDT2(), new TransportReceiverRDT2());

		ts.runTest();

		ts.assertRDT();
	}

}
