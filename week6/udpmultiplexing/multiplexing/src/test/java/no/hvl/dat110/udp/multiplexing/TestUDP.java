package no.hvl.dat110.udp.multiplexing;

import static org.junit.jupiter.api.Assertions.*;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TestUDP {

	private static String TESTHOST = "localhost";
	private static int TESTPORT = 8080;
	private static String TESTMESSAGE = "TEST";
	
	@Test
	void test() throws SocketException, UnknownHostException {

		UDPSender sender = new UDPSender(TESTHOST, TESTPORT);
		UDPReceiver receiver = new UDPReceiver(TESTPORT);

		Thread tsender = new Thread() {

			@Override
			public void run() {

				sender.send(TESTMESSAGE.getBytes());
				sender.close();

			}
		};

		Thread treceiver = new Thread() {

			@Override
			public void run() {

				byte[] data = new byte[255];
				int len = receiver.receive(data);

				String message = (new String(Arrays.copyOfRange(data, 0, len)));

				assertEquals(message, TESTMESSAGE);
				receiver.close();
			}
		};

		treceiver.start();
		
		tsender.start();
		
		try {
			tsender.join();
			treceiver.join();

		} catch (InterruptedException ex) {

			System.out.println("Main thread " + ex.getMessage());
			ex.printStackTrace();
		}

		assertTrue(true);
	}
}
