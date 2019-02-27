package no.hvl.dat110.network.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import no.hvl.dat110.network.Channel;
import no.hvl.dat110.network.NetworkService;

import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt1.Adversary;
import no.hvl.dat110.network.*;

public class TestNetwork {

	@Test
	public void test() {
		
		Channel channel = new Channel("test channel", new Adversary());
		
		//TODO: fixme - 2xchannel
		NetworkService ns = new NetworkService("network service",channel,channel);
		
		ns.start();
		
		String message = "1234";
		Datagram datagram = new Datagram(new Segment (message.getBytes()));
		
		ns.doStop();
		
		ns.udt_send(datagram);
		
		try {
			Thread.sleep(5000); // leave time for reception
			
		} catch (InterruptedException ex) {

			System.out.println("Test network " + ex.getMessage());
			ex.printStackTrace();

		}
	}

}
