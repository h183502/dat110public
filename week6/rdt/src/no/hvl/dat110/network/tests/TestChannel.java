package no.hvl.dat110.network.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import no.hvl.dat110.network.Channel;
import no.hvl.dat110.network.*;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt1.Adversary;

public class TestChannel {

	@Test
	public void test() {

		Channel channel = new Channel("Test channel", new Adversary());
		
		channel.start();

		byte[] data = { 1, 2, 3, 4 };
		
		Datagram datasend = new Datagram (new Segment(data));

		channel.send(datasend);

		Datagram datarecv = channel.receive();

		assertEquals(datasend, datarecv);

		channel.doStop();

		try {
			channel.join();
		} catch (InterruptedException ex) {

			System.out.println("Channel receive " + ex.getMessage());
			ex.printStackTrace();

		}
	}

}
